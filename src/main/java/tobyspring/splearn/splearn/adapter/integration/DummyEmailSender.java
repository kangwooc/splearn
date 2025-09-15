package tobyspring.splearn.splearn.adapter.integration;

import org.springframework.stereotype.Component;
import tobyspring.splearn.splearn.application.required.EmailSender;
import tobyspring.splearn.splearn.domain.member.Email;

@Component
public class DummyEmailSender implements EmailSender {
    @Override
    public void send(Email email, String subject, String body) {
        System.out.println("Sending email..." + email);
    }
}
