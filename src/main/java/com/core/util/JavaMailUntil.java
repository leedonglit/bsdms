package com.core.util;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Pattern;

import com.core.tools.PropertiesTool;
import com.sun.mail.util.MailSSLSocketFactory;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import cn.hutool.core.util.RandomUtil;

public class JavaMailUntil {
	private static String emailRegular = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";

	public static boolean validEmail(String emailAddress) {
		return Pattern.matches(emailRegular, emailAddress);
	}

	private static String fromMailSMTPHost = "smtp.exmail.qq.com";
	private static String smtpPort = "465";
	private static Properties setProperties() {
		Properties properties = new Properties();
		properties.setProperty("mail.transport.protocol", "smtp");
		properties.setProperty("mail.smtp.host", fromMailSMTPHost);
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.port", smtpPort);

		//开启安全协议
		MailSSLSocketFactory sf = null;
		try {
			sf = new MailSSLSocketFactory();
			sf.setTrustAllHosts(true);
		} catch (GeneralSecurityException e1) {
			e1.printStackTrace();
		}
		properties.put("mail.smtp.ssl.socketFactory", sf);
		properties.setProperty("mail.smtp.socketFactory.fallback", "false");
		properties.setProperty("mail.smtp.socketFactory.port", smtpPort);
		properties.setProperty("mail.smtp.ssl.enable", "true");
		properties.setProperty("mail.smtp.starttls.enable", "true");
		properties.setProperty("mail.debug", "false");
		return properties;
	}

	private static Message setMail(Session session, String toEmailAdress, String emailTitle, String emailContent) throws Exception {
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(PropertiesTool.getString("send.email.account"), "IDGAR", "UTF-8"));
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmailAdress, toEmailAdress, "UTF-8"));
		message.setSubject(emailTitle);
		message.setContent(emailContent, "text/html;charset=utf-8");
		message.setSentDate(new Date());
		message.saveChanges();
		return message;
	}

	private static Session setSession() {
		Properties properties = setProperties();
		Session session = Session.getDefaultInstance(properties,new SimpleAuthenticator(PropertiesTool.getString("send.email.account"), PropertiesTool.getString("send.email.pwd")));
		session.setDebug(true);
		return session;
	}


	public static boolean sendEmail(String toEmailAdress, String emailContent, String emailTitle) {
		Session session = setSession();
		try {
			Message message = setMail(session, toEmailAdress, emailTitle, emailContent);
			Transport transport = session.getTransport();
			transport.connect(PropertiesTool.getString("send.email.account"), PropertiesTool.getString("send.email.pwd"));
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		String code = RandomUtil.randomNumbers(4)+"";
		sendEmail("1083606942@qq.com","IDGAR Verification Code: "+code,"IDGAR email verification code");
	}
}
