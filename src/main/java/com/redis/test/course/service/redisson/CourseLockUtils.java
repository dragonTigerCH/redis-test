package com.redis.test.course.service.redisson;

import com.redis.test.course.domain.model.CourseClass;
import com.redis.test.course.domain.model.CourseMember;
import com.redis.test.course.domain.repository.CourseClassRepository;
import com.redis.test.course.domain.repository.CourseMemberRepository;
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
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class CourseLockUtils {

    private final RedissonClient redissonClient;
    private final CourseMemberService courseMemberService;
    private final CourseMemberRepository courseMemberRepository;
    private final CourseClassRepository courseClassRepository;

    public Long saveLock(Long memberSeq, Long courseClassSeq) throws InterruptedException {

            RLock rLock = redissonClient.getLock("postLock");
        try {
            boolean success = rLock.tryLock(5,2, TimeUnit.SECONDS);
            if (!success){
                log.info("락 획득 실패");
                throw new IllegalArgumentException();
            }
            return courseMemberService.save(memberSeq,courseClassSeq);
        } finally {
            rLock.unlock();
        }
    }



}
