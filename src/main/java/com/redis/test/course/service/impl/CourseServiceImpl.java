package com.redis.test.course.service.impl;

import com.redis.test.course.domain.model.Course;
import com.redis.test.course.domain.repository.CourseRepository;
import com.redis.test.course.service.CourseService;
import com.redis.test.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    @Transactional
    @Override
    public Long save(String name) {

        Course course = courseRepository.save(
                Course.builder()
                        .name(name)
                        .build()
        );
        log.info("과정이 생성되었습니다. 과정이름 :: {}",course.getName());

        return course.getSeq();
    }
}
