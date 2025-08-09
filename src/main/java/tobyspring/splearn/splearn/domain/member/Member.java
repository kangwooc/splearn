package tobyspring.splearn.splearn.domain.member;

import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

import static org.springframework.util.Assert.state;

@Getter
@ToString
public class Member {
    private Email email;
    private String nickname;
    private String passwordHash;
    private MemberStatus status;

    private Member() {}

    // @Builder // 빌더의 경우에는 여러가지 이슈가 발생할수 있음
    private Member(Email email, String nickname, String passwordHash) {
        this.email = Objects.requireNonNull(email);
        this.nickname = Objects.requireNonNull(nickname);
        this.passwordHash = Objects.requireNonNull(passwordHash);
        this.status = MemberStatus.PENDING;
    }

    // 정적 팩토리 메소드
    public static Member create(MemberCreateRequest request, PasswordEncoder encoder) {
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
