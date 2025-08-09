package tobyspring.splearn.splearn.domain.member;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmailTest {
    @Test
    void equality() {
        var email1 = new Email("toby@splearn.com");
        var email2 = new Email("toby@splearn.com");

        assertThat(email1).isEqualTo(email2);
    }
}