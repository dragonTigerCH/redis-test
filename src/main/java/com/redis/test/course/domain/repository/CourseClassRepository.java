package com.redis.test.course.domain.repository;

import com.redis.test.course.domain.model.CourseClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseClassRepository extends JpaRepository<CourseClass,Long> {
}
