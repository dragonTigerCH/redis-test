package com.redis.test.course.web;

import com.redis.test.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/course")
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    public Long save(
            @RequestParam String name
    ){
        return courseService.save(name);
    }


}
