package com.redis.test.course.domain.model;

import com.redis.test.member.domain.model.Member;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Slf4j
public class CourseMember {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private CourseClass courseClass;


}
