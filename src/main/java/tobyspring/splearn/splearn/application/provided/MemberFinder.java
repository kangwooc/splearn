package tobyspring.splearn.splearn.application.provided;

import tobyspring.splearn.splearn.domain.member.Member;

/**
 * 회원은 조회한다.
 */
public interface MemberFinder {
    Member find(Long memberId);
}
