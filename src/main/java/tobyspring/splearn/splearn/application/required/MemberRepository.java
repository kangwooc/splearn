package tobyspring.splearn.splearn.application.required;

import org.springframework.data.repository.Repository;
import tobyspring.splearn.splearn.domain.member.Member;

/**
 * 회원 정보를 저장하거나 조회한다.
 */
public interface MemberRepository extends Repository<Member, Long> {
    Member save(Member member);

}
