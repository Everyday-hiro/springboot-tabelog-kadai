package com.example.taberogu.event;

import java.util.UUID;

import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.example.taberogu.entity.User;
import com.example.taberogu.service.VerificationTokenService;

@Component
public class PasswordResetEventListener {
	private final VerificationTokenService verificationTokenService;
	private final JavaMailSender mailSender;

	public PasswordResetEventListener(VerificationTokenService verificationTokenService, JavaMailSender mailSender) {
		this.verificationTokenService = verificationTokenService;
		this.mailSender = mailSender;
	}

	@EventListener
	public void onPasswordResetEvent(PasswordResetEvent passwordResetEvent) {
		User user = passwordResetEvent.getUser();
		String token = UUID.randomUUID().toString();

		// トークンの生成と保存
		verificationTokenService.create(user, token);

		// パスワード再設定リンクの生成
		String resetUrl = passwordResetEvent.getRequestUrl() + "/reset-password?token=" + token;

		// メール送信内容の設定
		String recipientAddress = user.getEmail();
		String subject = "パスワード再設定のリクエスト";
		String message = "以下のリンクをクリックしてパスワードを再設定してください: ";

		// メールの作成と送信
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(recipientAddress);
		mailMessage.setSubject(subject);
		mailMessage.setText(message + "\n" + resetUrl);

		mailSender.send(mailMessage);
	}
}
