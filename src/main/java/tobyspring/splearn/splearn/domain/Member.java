package tobyspring.splearn.splearn.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

import java.util.Objects;

import static org.springframework.util.Assert.state;

@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NaturalIdCache // 영속성 컨텍스트에서 체크가 된다.
public class Member extends AbstractEntity {
    @NaturalId
    private Email email;
    private String nickname;
    private String passwordHash;

    private MemberStatus status;

    // 정적 팩토리 메소드
    public static Member register(MemberRegisterRequest request, PasswordEncoder encoder) {
        Member member = new Member();

        member.email = new Email(request.email());
        member.nickname = Objects.requireNonNull(request.nickname());
        member.passwordHash = Objects.requireNonNull(encoder.encode(request.passwordHash()));
        member.status = MemberStatus.PENDING;

        return member;
    }

    public void activate() {
        state(status == MemberStatus.PENDING, "PENDING 상태가 아닙니다.");

        this.status = MemberStatus.ACTIVE;
    }


    public void deactivate() {
        state(status == MemberStatus.ACTIVE, "ACTIVE 상태가 아닙니다");

        this.status = MemberStatus.DEACTIVATED;
    }

    public boolean verifyPassword(String password, PasswordEncoder encoder) {
        return encoder.matches(password, this.passwordHash);
    }

    public void changeNickname(String nickname) {
        this.nickname = Objects.requireNonNull(nickname);
    }

    public void changePassword(String password) {
        this.passwordHash = Objects.requireNonNull(password);
    }

    public boolean isActive() {
        return status == MemberStatus.ACTIVE;
    }
}
