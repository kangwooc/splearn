package tobyspring.splearn.splearn.domain.member;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProfileTest {
    @Test
    void profile() {
        new Profile("tobylee");
        new Profile("toby100");
        new Profile("12345");
        new Profile("");
    }

    @Test
    void profileFail() {
        assertThatThrownBy(() -> new Profile("tobyleerandomlongtext"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Profile("AA"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Profile("프로필"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void url() {
        var profile = new Profile("tobylee");

        assertThat(profile.url()).isEqualTo("@tobylee");
    }
}