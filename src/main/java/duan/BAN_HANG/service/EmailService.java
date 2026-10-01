package duan.BAN_HANG.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	private final JavaMailSender mailSender;

	public EmailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void sendOtp(String toEmail, String otp) {

		SimpleMailMessage message = new SimpleMailMessage();

		message.setTo(toEmail);
		message.setSubject("Mã xác nhận đăng ký tài khoản");
		message.setText("Mã xác nhận của bạn là: " + otp + "\nMã có hiệu lực trong 5 phút.");

		mailSender.send(message);
	}
}
