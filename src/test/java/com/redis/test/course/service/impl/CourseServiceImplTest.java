package com.redis.test.course.service.impl;

import com.redis.test.course.domain.model.CourseMember;
import com.redis.test.course.domain.repository.CourseMemberRepository;
import com.redis.test.course.service.CourseClassService;
import com.redis.test.course.service.CourseMemberService;
import com.redis.test.course.service.CourseService;
import com.redis.test.course.service.redisson.CourseLockUtils;
import com.redis.test.member.domain.model.Member;
import com.redis.test.member.domain.repository.MemberRepository;
import com.redis.test.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootTest
@Slf4j
class CourseServiceImplTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CourseMemberRepository courseMemberRepository;
    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseClassService courseClassService;
    @Autowired
    private CourseMemberService courseMemberService;
    @Autowired
    private CourseLockUtils courseLockUtils;

    @Test
    @DisplayName("수강신청 테스트")
    void 수강신청_테스트() throws InterruptedException {

        //given

        ExecutorService executorService = Executors.newFixedThreadPool(100);
        CountDownLatch countDownLatch = new CountDownLatch(100);
        final String worker = Thread.currentThread().getName();
        log.info("[1구간]현재 쓰레드 개수 :: "+countDownLatch.getCount());

        Long courseSeq = courseService.save("테스트 과정");
        Long savedCourseClassSeq = courseClassService.save(courseSeq,11);

        List<Long> memberIdLists = new ArrayList<>();
        List<Long> courseMemberIdLists = new ArrayList<>();
        for (int i = 1; i < 101; i++) {
            Long savedMembers = memberService.save("DragonTiger" + i);
            memberIdLists.add(savedMembers);
        }

        List<Member> memberList = memberRepository.findAllById(memberIdLists);

        //when
        for (Member member : memberList) {
                executorService.submit(() -> {
                try {
//                    Long savedLockCourseMember = courseMemberService.save(member.getSeq(), savedCourseClassSeq);
                    Long savedLockCourseMember = courseLockUtils.saveLock(member.getSeq(), savedCourseClassSeq);
                    log.info("저장된 CourseMemberSeq :: {}", savedLockCourseMember);
                    courseMemberIdLists.add(savedLockCourseMember);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    countDownLatch.countDown();
                    log.info("[2구간]현재 쓰레드 개수 :: " + countDownLatch.getCount());
                }
                });


        }
        log.info("현재 쓰레드 :: {}",worker);
        log.info("[3구간]현재 쓰레드 개수 :: {}",countDownLatch.getCount());
        countDownLatch.await();
        List<CourseMember> courseMembers = courseMemberRepository.findAllById(courseMemberIdLists);
        log.info("리스트 :: {}",courseMemberIdLists);

        //then
        Assertions.assertThat(courseMembers.size()).isEqualTo(11);

    }

}