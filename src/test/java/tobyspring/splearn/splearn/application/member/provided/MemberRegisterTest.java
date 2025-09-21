package tobyspring.splearn.splearn.application.member.provided;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import tobyspring.splearn.splearn.SplearnTestConfiguration;
import tobyspring.splearn.splearn.domain.member.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
record MemberRegisterTest(MemberRegister memberRegister, EntityManager entityManager) {

    @Test
    void registerMember() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

        System.out.println(member);

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void duplicateEmailTest() {
        memberRegister.register(MemberFixture.createMemberRegisterRequest());
        assertThatThrownBy(() -> memberRegister.register(MemberFixture.createMemberRegisterRequest()))
                .isInstanceOf(DuplicateEmailException.class);
    }

    @Test
    void memberRegisterRequestFail() {
        checkValidation(new MemberRegisterRequest("toby@splearn.app", "Toby", "secret"));
        checkValidation(new MemberRegisterRequest("toby@splearn.app", "Charlie_______________", "longsecret"));
        checkValidation(new MemberRegisterRequest("tobysplearn.app", "Charlie", "longsecret"));
    }

    private void checkValidation(MemberRegisterRequest invalid) {
        Assertions.assertThatThrownBy(() -> memberRegister.register(invalid))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void activate() {
        Member member = register();

        member = memberRegister.activate(member.getId());

        entityManager.flush();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
        assertThat(member.getDetail().getActivatedAt()).isNotNull();
    }

    @Test
    void deactivate() {
        Member member = register();

        member = memberRegister.activate(member.getId());

        entityManager.flush();
        entityManager.clear();

        member = memberRegister.deactivate(member.getId());

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
        assertThat(member.getDetail().getDeactivatedAt()).isNotNull();
    }

    @Test
    void updateInfo() {
        Member member = register();

        member = memberRegister.activate(member.getId());
        entityManager.flush();
        entityManager.clear();

        member = memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("Peter", "toby100", "자기소개"));

        assertThat(member.getDetail().getProfile().address()).isEqualTo("toby100");
    }

    private Member register() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        entityManager.flush();
        entityManager.clear();
        return member;
    }

    private Member register(String email) {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest(email));
        entityManager.flush();
        entityManager.clear();
        return member;
    }

    @Test
    void updateInfoFail() {
        Member member = register();

        memberRegister.activate(member.getId());
        memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("Peter", "toby100", "자기소개"));
        entityManager.flush();
        entityManager.clear();

        Member member2 = register("toby2@splearn.app");
        memberRegister.activate(member2.getId());
        entityManager.flush();
        entityManager.clear();

        // member2는 기존의 member와 같은 주소를 사용할수 없다.
        Assertions.assertThatThrownBy(
                () -> memberRegister.updateInfo(member2.getId(), new MemberInfoUpdateRequest("James", "toby100", "introduction"))
        ).isInstanceOf(DuplicateProfileException.class);

        // 다른 프로필로는 사용이 가능함
        memberRegister.updateInfo(member2.getId(), new MemberInfoUpdateRequest("James", "toby101", "introduction"));


        // 다른 프로필 주소로는 변경이 가능
        memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("Peter", "toby100", "자기소개"));
        memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("Peter", "", "자기소개"));

        // 하지만 중복되는 주소는 사용이 불가능함
        Assertions.assertThatThrownBy(
                () -> memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("Peter", "toby101", "introduction"))
        ).isInstanceOf(DuplicateProfileException.class);
    }
}
