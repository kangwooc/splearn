package tobyspring.splearn.splearn.domain.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tobyspring.splearn.splearn.domain.Member;
import tobyspring.splearn.splearn.domain.MemberStatus;
import tobyspring.splearn.splearn.domain.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static tobyspring.splearn.splearn.domain.member.MemberFixture.createMemberRegisterRequest;
import static tobyspring.splearn.splearn.domain.member.MemberFixture.createPasswordEncoder;

class MemberTest {
    Member member;
    PasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        this.encoder = createPasswordEncoder();

        member = Member.register(createMemberRegisterRequest(), this.encoder);
    }

    @Test
    void registerMember() {
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

//    @Test
//    void constructorNonNullCheck() {
//        assertThatThrownBy(() -> Member.create(new MemberCreateRequest(null, "Toby", "secret"), this.encoder)
//                .isInstanceOf(NullPointerException.class);
//    }

    @Test
    void activate() {
        member.activate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }

    @Test
    void activateFail() {

        member.activate();
        
        assertThatThrownBy(() -> member.activate())
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void deactivate() {
        member.activate();
        member.deactivate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
    }

    @Test
    void deactivateFail() {
        assertThatThrownBy(() -> member.deactivate())
                .isInstanceOf(IllegalStateException.class);

        member.activate();
        member.deactivate();

        assertThatThrownBy(() -> member.deactivate())
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void verifyPassword() {
        assertThat(member.verifyPassword("verysecret", this.encoder)).isTrue();
        assertThat(member.verifyPassword("hello", this.encoder)).isFalse();
    }

    @Test
    void changeNickname() {
        assertThat(this.member.getNickname()).isEqualTo("Charlie");

        member.changeNickname("charlie2");
        assertThat(this.member.getNickname()).isEqualTo("charlie2");
    }

    @Test
    void changePassword() {
        member.changePassword("verySecret2");
        assertThat(this.member.getPasswordHash()).isEqualTo("verySecret2");
    }

    @Test
    void shouldBeActive() {
        assertThat(member.isActive()).isFalse();

        member.activate();

        assertThat(member.isActive()).isTrue();

        member.deactivate();

        assertThat(member.isActive()).isFalse();
    }

    @Test
    void invalidEmail() {
        assertThatThrownBy(() -> Member.register(createMemberRegisterRequest("invalid email"), this.encoder))
                .isInstanceOf(IllegalArgumentException.class);
    }
}