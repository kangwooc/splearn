package tobyspring.splearn.splearn.adapter.webapi;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tobyspring.splearn.splearn.adapter.webapi.dto.MemberRegisterResponse;
import tobyspring.splearn.splearn.application.member.provided.MemberRegister;
import tobyspring.splearn.splearn.domain.member.Member;
import tobyspring.splearn.splearn.domain.member.MemberRegisterRequest;

@RestController
@RequiredArgsConstructor
public class MemberApi {
    private final MemberRegister memberRegister;

    // register api -> /members POST
    @PostMapping("/api/members")
    public MemberRegisterResponse register(@Valid @RequestBody MemberRegisterRequest memberRegisterRequest) {
        Member member = memberRegister.register(memberRegisterRequest);
        return MemberRegisterResponse.of(member);
    }
}
