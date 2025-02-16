package com.StarkIndustries.JwtAuthenticationMark2.Service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public boolean sendEmail(int otp,String email){

        var emailBody="Thank you for signing up! To complete your email verification, please use the One-Time Password (OTP) below:\n" +
                "\n" +
                "Your OTP: "+String.valueOf(otp)+"\n" +
                "\n" +
                "This OTP is valid for the next 10 minutes. Please do not share it with anyone.\n" +
                "\n" +
                "If you did not request this verification, please ignore this email.\n" +
                "\n" +
                "Best regards";

        var status = false;
        var properties = System.getProperties();
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","465");
        properties.put("mail.smtp.ssl.enable",true);
        properties.put("mail.smtp.auth",true);

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("kelaskaraditya1@gmail.com","ehok baxr eikl gxkc");
            }
        });

        try{
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom("kelaskaraditya1@gmail.com");
            mimeMessage.addRecipient(Message.RecipientType.TO,new InternetAddress(email));
            mimeMessage.setSubject("Verify Your Email for Secure Access");
            mimeMessage.setText(emailBody);

            status=true;

            Transport.send(mimeMessage);
        }catch (Exception e){
            e.printStackTrace();
        }

        return status;
    }
}