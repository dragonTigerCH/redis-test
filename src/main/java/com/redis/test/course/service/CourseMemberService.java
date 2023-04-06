package com.redis.test.course.service;

import com.redis.test.course.domain.model.CourseClass;
import com.redis.test.course.domain.model.CourseMember;

public interface CourseMemberService {

    Long save(Long memberSeq, Long courseClassSeq);

}
