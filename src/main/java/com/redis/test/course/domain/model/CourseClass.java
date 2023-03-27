package com.redis.test.course.domain.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Slf4j
@Builder
@AllArgsConstructor
public class CourseClass {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Integer limitPeople;

    @ManyToOne(fetch = FetchType.LAZY)
    private Course course;

    @OneToMany(mappedBy = "courseClass")
    private List<CourseMember> courseMemberList = new ArrayList<>();

    public void limitedNumberOfStudents(){
        int currentNumberOfStudents = (int) this.courseMemberList.stream().count();
        log.info("현재 수강 신청 인원수 :: {}",currentNumberOfStudents);
        if (this.limitPeople < currentNumberOfStudents + 1)
            throw new RuntimeException("수강 신청 정원이 모두 찼습니다. currentNumberOfStudents = " + currentNumberOfStudents);
    }

}
