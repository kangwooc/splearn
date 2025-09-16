package tobyspring.splearn.splearn.application.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import tobyspring.splearn.splearn.application.member.provided.MemberFinder;
import tobyspring.splearn.splearn.application.member.required.MemberRepository;
import tobyspring.splearn.splearn.domain.member.Member;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class MemberQueryService implements MemberFinder {
    private final MemberRepository memberRepository;

    @Override
    public Member find(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("회원을 찾을수 없습니다: " + memberId));
    }
}
