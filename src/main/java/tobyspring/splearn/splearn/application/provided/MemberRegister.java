package tobyspring.splearn.splearn.application.provided;

import jakarta.validation.Valid;
import tobyspring.splearn.splearn.domain.member.Member;
import tobyspring.splearn.splearn.domain.member.MemberRegisterRequest;

/**
 * 회원의 등록과 같은 기능을 제공한다.
 */
public interface MemberRegister {
    Member register(@Valid MemberRegisterRequest request);
}
