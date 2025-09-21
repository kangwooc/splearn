package tobyspring.splearn.splearn.adapter.webapi.dto;

import tobyspring.splearn.splearn.domain.member.Member;

public record MemberRegisterResponse(
        Long memberId,
        String emailAddress
) {
    public static MemberRegisterResponse of(Member member) {
        return new MemberRegisterResponse(member.getId(), member.getEmail().address());
    }
}
