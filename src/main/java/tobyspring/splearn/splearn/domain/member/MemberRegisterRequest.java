package tobyspring.splearn.splearn.domain.member;

public record MemberRegisterRequest(String email, String nickname, String passwordHash) {
}
