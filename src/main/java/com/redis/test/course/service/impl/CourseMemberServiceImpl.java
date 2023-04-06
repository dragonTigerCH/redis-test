package com.redis.test.course.service.impl;


import com.redis.test.course.domain.model.CourseClass;
import com.redis.test.course.domain.model.CourseMember;
import com.redis.test.course.domain.repository.CourseClassRepository;
import com.redis.test.course.domain.repository.CourseMemberRepository;
import com.redis.test.course.service.CourseMemberService;
import com.redis.test.member.domain.model.Member;
import com.redis.test.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseMemberServiceImpl implements CourseMemberService {

    private final CourseMemberRepository courseMemberRepository;
    private final MemberRepository memberRepository;
    private final CourseClassRepository courseClassRepository;


    @Transactional
    @Override
    public Long save(Long memberSeq, Long courseClassSeq) {

        Member member = memberRepository.findById(memberSeq)
                .orElseThrow(() -> new RuntimeException("로그인한 사용자를 찾을 수 없습니다. memberSeq :: " + memberSeq));
        CourseClass courseClass = courseClassRepository.findById(courseClassSeq)
                .orElseThrow(() -> new RuntimeException("해당 과정 클래스를 찾을 수 없습니다. courseClassSeq :: " + courseClassSeq));

        if (!courseClass.limitedNumberOfStudents()) {
            throw new RuntimeException("땡");
        }

        CourseMember courseMember = courseMemberRepository.save(
                CourseMember
                        .builder()
                        .member(member)
                        .courseClass(courseClass)
                        .build()
        );
        log.info("사용자 수강신청 정보가 생성되었습니다. 시퀀스 :: {} ",courseMember.getSeq());

        return courseMember.getSeq();
    }
}
