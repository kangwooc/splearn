package tobyspring.splearn.splearn.application.member;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import tobyspring.splearn.splearn.application.member.provided.MemberFinder;
import tobyspring.splearn.splearn.application.member.provided.MemberRegister;
import tobyspring.splearn.splearn.application.member.required.EmailSender;
import tobyspring.splearn.splearn.application.member.required.MemberRepository;
import tobyspring.splearn.splearn.domain.member.*;
import tobyspring.splearn.splearn.domain.shared.Email;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class MemberModifyService implements MemberRegister {
    private final MemberFinder memberFinder;
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

    @Override
    public Member activate(Long memberId) {
        Member member = memberFinder.find(memberId);

        member.activate();
        // Spring Data를 사용하는 것
        return memberRepository.save(member);
    }

    @Override
    public Member deactivate(Long memberId) {
        Member member = memberFinder.find(memberId);

        member.deactivate();
        // Spring Data를 사용하는 것
        return memberRepository.save(member);
    }

    @Override
    public Member updateInfo(Long memberId, MemberInfoUpdateRequest request) {
        Member member = memberFinder.find(memberId);

        checkDuplicateProfile(member, request.profileAddress());
        member.updateInfo(request);
        return memberRepository.save(member);
    }

    private void checkDuplicateProfile(Member member, String profileAddress) {
        if (profileAddress.isEmpty()) return;

        Profile currentProfile = member.getDetail().getProfile();
        if (currentProfile != null && currentProfile.address().equals(profileAddress)) {
            return;
        }

        if (memberRepository.findByProfile(new Profile(profileAddress)).isPresent()) {
            throw new DuplicateProfileException("이미 존재하는 프로필 주소입니다: " + profileAddress);
        }

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
