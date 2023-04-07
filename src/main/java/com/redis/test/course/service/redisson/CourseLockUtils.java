package com.redis.test.course.service.redisson;

import com.redis.test.course.domain.repository.CourseClassRepository;
import com.redis.test.course.domain.repository.CourseMemberRepository;
import com.redis.test.course.service.CourseMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

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

            RLock rLock = redissonClient.getLock("courseMemberLock");
        try {
            boolean success = rLock.tryLock(5000,1000, TimeUnit.MILLISECONDS);
            if (!success) {
                log.info("락 획득 실패");
                throw new IllegalArgumentException();
            }
            return courseMemberService.save(memberSeq,courseClassSeq);
        } finally {
            rLock.unlock();
        }
    }



}
