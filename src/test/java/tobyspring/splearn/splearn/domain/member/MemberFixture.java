package tobyspring.splearn.splearn.domain.member;

public class MemberFixture {
    public static MemberRegisterRequest createMemberRegisterRequest(String email) {
        return new MemberRegisterRequest(email, "Charlie", "verysecret");
    }

    public static MemberRegisterRequest createMemberRegisterRequest() {
        return createMemberRegisterRequest("toby@splearn.app");
    }

    public static PasswordEncoder createPasswordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(String rawPassword) {
                return rawPassword.toUpperCase();
            }

            @Override
            public boolean matches(String rawPassword, String passwordHash) {
                return encode(rawPassword).equals(passwordHash);
            }
        };
    }
}
