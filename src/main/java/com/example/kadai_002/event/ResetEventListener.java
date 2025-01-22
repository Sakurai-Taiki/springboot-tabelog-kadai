package com.example.kadai_002.event;

import java.util.UUID;

import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.example.kadai_002.entity.Users;
import com.example.kadai_002.service.VerificationTokenService;

@Component
public class ResetEventListener {
	private final VerificationTokenService verificationTokenService;
	private final JavaMailSender javaMailSender;
	
	public ResetEventListener(VerificationTokenService verificationTokenService, JavaMailSender mailSender) {
		this.verificationTokenService = verificationTokenService;
		this.javaMailSender = mailSender;
	}
	
	@EventListener
	private void onResetEvent(ResetEvent resetEvent) {
		Users users = resetEvent.getUsers();
		String token = UUID.randomUUID().toString();
		verificationTokenService.update(users, token);
		
		String recipientAddress = users.getMailAddress();
		String subject = "メール認証";
		String confirmationUrl = resetEvent.getRequestUrl() + "/verify?token=" + token;
		String message = "以下のリンクをクリックしてパスワードをリセットしてください。";
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(recipientAddress);
		mailMessage.setSubject(subject);
		mailMessage.setText(message + "\n" + confirmationUrl);
		javaMailSender.send(mailMessage);
	}


}