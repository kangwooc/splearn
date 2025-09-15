package tobyspring.splearn.splearn.adapter.integration;

import org.springframework.context.annotation.Fallback;
import org.springframework.stereotype.Component;
import tobyspring.splearn.splearn.application.required.EmailSender;
import tobyspring.splearn.splearn.domain.member.Email;

@Component
@Fallback // spring 6.2 도입 다른 빈을 찾다 못 찾으면 이 빈을 사용하는 것
public class DummyEmailSender implements EmailSender {
    @Override
    public void send(Email email, String subject, String body) {
        System.out.println("Sending email..." + email);
    }
}
