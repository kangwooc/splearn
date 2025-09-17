package tobyspring.splearn.splearn.domain.member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;
import tobyspring.splearn.splearn.domain.AbstractEntity;
import tobyspring.splearn.splearn.domain.shared.Email;

import java.util.Objects;

import static org.springframework.util.Assert.state;

@Entity
@Getter
@ToString(callSuper = true, exclude = "detail")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends AbstractEntity {
    @NaturalId
    private Email email;
    private String nickname;
    private String passwordHash;

    private MemberStatus status;

    private MemberDetail detail;

    // 정적 팩토리 메소드
    public static Member register(MemberRegisterRequest request, PasswordEncoder encoder) {
        Member member = new Member();

        member.email = new Email(request.email());
        member.nickname = Objects.requireNonNull(request.nickname());
        member.passwordHash = Objects.requireNonNull(encoder.encode(request.passwordHash()));
        member.status = MemberStatus.PENDING;

        member.detail = MemberDetail.create();

        return member;
    }

    public void activate() {
        state(status == MemberStatus.PENDING, "PENDING 상태가 아닙니다.");

        this.status = MemberStatus.ACTIVE;
        this.detail.activate();
    }

    public void deactivate() {
        state(status == MemberStatus.ACTIVE, "ACTIVE 상태가 아닙니다");

        this.status = MemberStatus.DEACTIVATED;
        this.detail.deactivate();
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

    public void updateInfo(MemberInfoUpdateRequest request) {
        this.nickname = Objects.requireNonNull(request.nickname());

        this.detail.updateInfo(request);
    }

    public boolean isActive() {
        return status == MemberStatus.ACTIVE;
    }
}
