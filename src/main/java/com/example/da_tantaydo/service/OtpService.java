package com.example.da_tantaydo.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final JavaMailSender mailSender;
    private final Map<String, OtpData> otpStorage = new ConcurrentHashMap<>();

    public void sendOtp(String gmail) {
        String otp = generateOtp();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(5);
        otpStorage.put(gmail, new OtpData(otp, expiry));
        sendOtpEmail(gmail, otp);
    }

    public boolean verifyOtp(String gmail, String inputOtp) {
        OtpData otpData = otpStorage.get(gmail);
        if (otpData == null) return false;
        if (LocalDateTime.now().isAfter(otpData.getExpiry())) {
            otpStorage.remove(gmail);
            return false;
        }
        if (!otpData.getOtp().equals(inputOtp)) return false;
        otpStorage.remove(gmail);
        return true;
    }

    private String generateOtp() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

    private void sendOtpEmail(String toGmail, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toGmail);
            helper.setSubject("Mã OTP xác thực");
            helper.setText("""
                <div style="font-family: Arial; max-width: 480px; margin: auto;">
                    <h2>Xác thực OTP</h2>
                    <p>Mã OTP của bạn là:</p>
                    <h1 style="letter-spacing: 8px;">%s</h1>
                    <p>Mã có hiệu lực trong <strong>5 phút</strong>.</p>
                </div>
            """.formatted(otp), true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Không thể gửi email: " + e.getMessage());
        }
    }

    @Data
    @AllArgsConstructor
    private static class OtpData {
        private String otp;
        private LocalDateTime expiry;
    }
}