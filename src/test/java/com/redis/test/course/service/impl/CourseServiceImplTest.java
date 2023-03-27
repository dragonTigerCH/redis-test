package com.redis.test.course.service.impl;

import com.redis.test.course.domain.model.CourseMember;
import com.redis.test.course.domain.repository.CourseMemberRepository;
import com.redis.test.course.service.CourseClassService;
import com.redis.test.course.service.CourseMemberService;
import com.redis.test.course.service.CourseService;
import com.redis.test.member.domain.model.Member;
import com.redis.test.member.domain.repository.MemberRepository;
import com.redis.test.member.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootTest
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

    @Test
    @DisplayName("수강신청 테스트")
    void 수강신청_테스트() throws Exception {

        //given

        ExecutorService executorService = Executors.newFixedThreadPool(99);
        CountDownLatch countDownLatch = new CountDownLatch(99);
        System.out.println("[1구간]현재 쓰레드 개수 :: "+countDownLatch.getCount());

        Long courseSeq = courseService.save("테스트 과정");
        Long savedCourseClassSeq = courseClassService.save(courseSeq,99);

        //when

        for (int i = 0; i < 1000; i++) {
            memberService.save("DragonTiger"+i);
        }
        List<Member> memberList = memberRepository.findAll();

        for (Member member : memberList) {
            try {
                executorService.submit(() -> {
                    courseMemberService.save(member.getSeq(),savedCourseClassSeq);
                });
                } finally {
                    countDownLatch.countDown();
                    System.out.println("[2구간]현재 쓰레드 개수 :: "+countDownLatch.getCount());
                }
        }
        System.out.println("[3구간]현재 쓰레드 개수 :: "+countDownLatch.getCount());
        countDownLatch.await();
        System.out.println("[4구간]현재 쓰레드 개수 :: "+countDownLatch.getCount());

        List<CourseMember> courseMembers = courseMemberRepository.findAll();

        //then
        Assertions.assertThat(courseMembers.size()).isEqualTo(99);

    }

}