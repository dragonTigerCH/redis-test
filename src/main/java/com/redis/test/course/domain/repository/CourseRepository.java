package com.redis.test.course.domain.repository;

import com.redis.test.course.domain.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course,Long> {
}
