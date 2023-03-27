package com.redis.test.course.web;

import com.redis.test.course.service.CourseClassService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/course-class")
public class CourseClassController {

    private final CourseClassService courseClassService;

    @PostMapping
    public Long save(
            @RequestParam Long courseSeq,
            @RequestParam Integer limitPeople
    ) {
        return courseClassService.save(courseSeq,limitPeople);
    }

}
