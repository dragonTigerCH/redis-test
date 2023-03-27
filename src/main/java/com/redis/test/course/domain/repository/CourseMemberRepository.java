package com.redis.test.course.domain.repository;

import com.redis.test.course.domain.model.CourseClass;
import com.redis.test.course.domain.model.CourseMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseMemberRepository extends JpaRepository<CourseMember,Long> {
}
