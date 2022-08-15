package com.example.jpa.api;

import com.example.jpa.domain.Member;
import com.example.jpa.domain.Order;
import com.example.jpa.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.SpringServletContainerInitializer;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    @GetMapping("/api/v2/members")
    public Result membersV2() {
         List<Member> findMembers = memberService.findMembers();
         List<MemberDto> collect = findMembers.stream()
                 .map(m-> new MemberDto(m.getName()))
                 .collect(Collectors.toList());
         return new Result(collect);
    }
    /**
     * @name membersV1
     * @return List<Member> //array 반환시 확장성이 떨어진다.
     * @description 회원조회
     */
    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }
    /**
     * @name saveMemberV1
     * @param member
     * @return
     * @description 회원등록
     */
    @PostMapping("/api/v1/members")
    //도메인을 그대로 쓰면 도메인 수정 시 api 스펙이 변경되므로 별도로 사용하는게 좋다
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2 (@PathVariable("id") Long id,
                                                @RequestBody @Valid UpdateMemberRequest request) {
        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }
    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }
    @Data
    static class UpdateMemberRequest {
        private String name;
    }
    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }
    @Data
    static class CreateMemberRequest {
        private String name;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}
