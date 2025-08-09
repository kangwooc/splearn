package tobyspring.splearn.splearn.domain.member;

public record MemberCreateRequest(String email, String nickname, String passwordHash) {
}
