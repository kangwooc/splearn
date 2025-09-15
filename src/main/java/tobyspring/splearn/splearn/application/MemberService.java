package tobyspring.splearn.splearn.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import tobyspring.splearn.splearn.application.provided.MemberRegister;
import tobyspring.splearn.splearn.application.required.EmailSender;
import tobyspring.splearn.splearn.application.required.MemberRepository;
import tobyspring.splearn.splearn.domain.member.*;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class MemberService implements MemberRegister {
    private final MemberRepository memberRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member register(MemberRegisterRequest request) {

        checkDuplicateEmail(request);
        // check
        Member member = Member.register(request, passwordEncoder);
        // repository
        memberRepository.save(member);
        // post process
        sendWelcomeEmail(member);

        return member;
    }

    private void sendWelcomeEmail(Member member) {
        emailSender.send(member.getEmail(), "등록을 완료해주세요.", "아래 링크를 클릭하여 등록을 완료해주세요.");
    }

    private void checkDuplicateEmail(MemberRegisterRequest request) {
        if (memberRepository.findByEmail(new Email(request.email())).isPresent()) {
            throw new DuplicateEmailException("이미 사용중인 이메일입니다. " + request.email());
        }
    }
}
