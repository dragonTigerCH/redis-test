package com.redis.test.course.service.redisson;

import com.redis.test.course.service.CourseMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RFuture;
import org.redisson.api.RLock;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.LongCodec;
import org.redisson.client.protocol.RedisStrictCommand;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class CourseLockUtils {

    private final RedissonClient redissonClient;
    private final CourseMemberService courseMemberService;

    public Long saveLock(Long memberSeq, Long courseClassSeq) throws InterruptedException {

        RLock lock = redissonClient.getLock(String.format("PARTICIPANT:%d", memberSeq));
        try {
            boolean success = lock.tryLock(4, 5, TimeUnit.SECONDS);
            log.info("성공 {}",success);
            if (!success){
                log.info("락 획득 실패");
                throw new IllegalArgumentException();
            }
            Thread.sleep(50);
            Long savedCourseMember = courseMemberService.save(memberSeq, courseClassSeq);
            return savedCourseMember;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
    public boolean acquireLock(String lockName, long timeout) {
        String lockKey = "locks:" + lockName;
        String luaScript = "if redis.call('exists', KEYS[1]) == 0 then\n" +
                "redis.call('hset', KEYS[1], ARGV[1], ARGV[2]);\n" +
                "return 1;\n" +
                "end;\n" +
                "return 0;";
        RScript scriptObj = redissonClient.getScript();
        RFuture<Long> future = scriptObj.evalAsync(RScript.Mode.READ_WRITE, luaScript, RScript.ReturnType.INTEGER, Collections.singletonList(lockKey), timeout / 1000);
        try {
            Long result = future.get(timeout, TimeUnit.MILLISECONDS);
            log.info("result :: {}",result);
            return result != null && result == 1;
        } catch (Exception e) {
            return false;
        }
    }

    public void releaseLock(String lockName) {
        String lockKey = "locks:" + lockName;
        redissonClient.getBucket(lockKey).delete();
    }



}
