package com.example.jpa.service;

import com.example.jpa.domain.Member;
import com.example.jpa.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
//데이터 변경은 항상 transaction 안에서 실행되어야 한다.
@Transactional(readOnly = true) //읽기 시에는 readOnly true.
@RequiredArgsConstructor // final을 가진 것들을 생성자로 만들어줌
public class MemberService {

    private final MemberRepository memberRepository;
    //setter injection - 변경 가능하기 때문에 좋지 않음
//    @Autowired
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    //생성자 injection - 요즘 선호하는 방식
//    private final MemberRepository memberRepository;
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }



    /**
     * 회원 가입
     */
    @Transactional  //기본값 false
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 중복 회원 조회
     * @param member
     */
    public void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName()); //멀티쓰레드를 고려하여 unique 제약 조건을 주는게 좋다
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다."); //member의 수로 확인하는게 더 좋음
        }
    }
    //회원 전체 조회

    /**
     * 전체 회원 존재
     * @return
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * 회원 조회
     * @param memberId
     * @return
     */
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);

    }
}
