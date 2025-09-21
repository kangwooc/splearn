package tobyspring.splearn.splearn.domain.member;

// 도메인 레이어에서 사용되는데 어댑터에 의존하는 코드가 됨
//@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String message) {
        super(message);
    }
}
