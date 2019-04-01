package e.gongfurui.digitallearnerlogbook.Helpers;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class EmailUtil {
    private final String MAIL_FROM_EMAIL = "gongfurui0452424857@gmail.com";//Sending out mail address
    private final String MAIL_FROM_PWD = "gfr199510";//Sending out mail password
    private final String MAIL_FROM_NAME = "Developer Gong";//sender name(Random)
    private final String MAIL_TO_NAME = "shoujianren";//receiver name
    private boolean isDebug=true;//decide whether to print out the debug information
    private final String MAIL_SMTP_HOST = "smtp.gmail.com";//the server to send the mail
    private final String MAIL_SMTP_PORT = "587";//use 587 port
    private final String MAIL_SMTP_AUTH = "true";//set authorization
    private final String MAIL_SMTP_STARTTLS_ENABLE = "true";//use STARTTLS connection
    private static EmailUtil instance;
    public static EmailUtil getInstance() {
      if (instance == null) {
        synchronized (EmailUtil.class) {
          if (instance == null) {
            instance = new EmailUtil();
          }
        }
      }
      return instance;
    }

    /**
     * Send the email
     * @param emailTo the email address of receiving
     * @param subject email title
     * @param content email content
     * @return void
     * */

    public void sendEmail(String emailTo,String subject,String content) {
      Properties props = new Properties();
      props.put("mail.smtp.host", MAIL_SMTP_HOST);
      props.put("mail.smtp.port", MAIL_SMTP_PORT);//use port 587
      props.put("mail.smtp.auth", MAIL_SMTP_AUTH);
      props.put("mail.smtp.starttls.enable",MAIL_SMTP_STARTTLS_ENABLE);//use STARTTLS connection
      try {
        PopupAuthenticator auth = new PopupAuthenticator();
        Session session = Session.getInstance(props, auth);
        session.setDebug(isDebug);//show the bug message
        MimeMessage message = new MimeMessage(session);
        Address addressFrom = new InternetAddress(MAIL_FROM_EMAIL, MAIL_FROM_NAME);
        Address addressTo = new InternetAddress(emailTo, MAIL_TO_NAME);
        message.setSubject(subject);
        message.setText(content);
        message.setFrom(addressFrom);
        message.addRecipient(Message.RecipientType.TO, addressTo);
        message.saveChanges();
        Transport transport = session.getTransport("smtp");
        transport.connect(MAIL_SMTP_HOST, MAIL_FROM_EMAIL,MAIL_FROM_PWD);
        transport.send(message);
        transport.close();
        if(isDebug){
          System.out.println("Send out successfully");
        }
      } catch (Exception e) {
        System.out.println(e.toString());
        System.out.println("Failed to send out");
      }
    }

    class PopupAuthenticator extends Authenticator {
      public PopupAuthenticator() {
      }

      public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(MAIL_FROM_EMAIL, MAIL_FROM_PWD);
      }
    }

}