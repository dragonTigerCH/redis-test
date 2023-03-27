package com.redis.test.course.service.impl;


import com.redis.test.course.domain.model.Course;
import com.redis.test.course.domain.model.CourseClass;
import com.redis.test.course.domain.repository.CourseClassRepository;
import com.redis.test.course.domain.repository.CourseRepository;
import com.redis.test.course.service.CourseClassService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseClassServiceImpl implements CourseClassService {

    private final CourseClassRepository courseClassRepository;
    private final CourseRepository courseRepository;
    @Transactional
    @Override
    public Long save(Long courseSeq,Integer limitPeople) {

        Course course = courseRepository.findById(courseSeq)
                .orElseThrow(() -> new RuntimeException("해당 과정을 찾을 수 없습니다."));

        CourseClass courseClass = courseClassRepository.save(
                CourseClass.builder()
                        .course(course)
                        .limitPeople(limitPeople)
                        .build()
        );
        log.info("courseClassSeq {} courseClassLimitPeople {}",courseClass.getSeq(),courseClass.getLimitPeople());

        return courseClass.getSeq();
    }
}
