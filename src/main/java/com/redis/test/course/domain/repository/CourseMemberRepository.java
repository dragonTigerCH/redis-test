package com.redis.test.course.domain.repository;

import com.redis.test.course.domain.model.CourseMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseMemberRepository extends JpaRepository<CourseMember,Long> {
    List<CourseMember> findAllByCourseClassSeq(Long seq);
}
