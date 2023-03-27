package com.redis.test.member.service.impl;

import com.redis.test.member.domain.model.Member;
import com.redis.test.member.domain.repository.MemberRepository;
import com.redis.test.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public Long save(String name) {

        Member member = memberRepository.save(
                Member.builder().name(name).build()
        );
        log.info("사용자가 생성되었습니다. 이름 :: {}",member.getName());

        return member.getSeq();
    }
}
