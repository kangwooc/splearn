package tobyspring.splearn.splearn.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tobyspring.splearn.splearn.application.provided.MemberRegister;
import tobyspring.splearn.splearn.application.required.EmailSender;
import tobyspring.splearn.splearn.application.required.MemberRepository;
import tobyspring.splearn.splearn.domain.member.Member;
import tobyspring.splearn.splearn.domain.member.MemberRegisterRequest;
import tobyspring.splearn.splearn.domain.member.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class MemberService implements MemberRegister {
    private final MemberRepository memberRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member register(MemberRegisterRequest request) {
        // check
        Member member = Member.register(request, passwordEncoder);
        // repository
        memberRepository.save(member);
        // post process
        emailSender.send(member.getEmail(), "등록을 완료해주세요.", "아래 링크를 클릭하여 등록을 완료해주세요.");

        return member;
    }
}
