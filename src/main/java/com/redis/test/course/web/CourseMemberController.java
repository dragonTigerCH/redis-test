package com.redis.test.course.web;

import com.redis.test.course.service.CourseMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/course-member")
public class CourseMemberController {

    private final CourseMemberService courseMemberService;

    @PostMapping
    public Long create(
            @RequestParam Long memberSeq,
            @RequestParam Long courseClassSeq
    ){
        return courseMemberService.save(memberSeq,courseClassSeq);
    }


}
