package tobyspring.splearn.splearn;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import tobyspring.splearn.splearn.application.required.EmailSender;
import tobyspring.splearn.splearn.domain.member.MemberFixture;
import tobyspring.splearn.splearn.domain.PasswordEncoder;

@TestConfiguration
public class SplearnTestConfiguration {
    @Bean
    EmailSender emailSender() {
        return (email, subject, body) -> System.out.println("sending email: " + email);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return MemberFixture.createPasswordEncoder();
    }
}