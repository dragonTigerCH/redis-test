package com.redis.test.course.domain.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    public boolean limitedNumberOfStudents() {
        int currentNumberOfStudents = (int) this.courseMemberList.stream().count();
        if (this.limitPeople < currentNumberOfStudents + 1) {
            log.info("수강 신청 정원이 모두 찼습니다.");
            return false;
        }
        log.info("현재 수강 신청 인원수 :: {}", currentNumberOfStudents);
        return true;
    }


}
