package tobyspring.splearn.splearn.domain.member;

public interface PasswordEncoder {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String passwordHash);
}
