package e.gongfurui.digitallearnerlogbook.Helpers;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class EmailUtil {
  private final String MAIL_FROM_EMAIL = "gongfurui0452424857@gmail.com";//发件人邮箱地址（必填自己申请的）
  private final String MAIL_FROM_PWD = "gfr199510";//授权码（必填自己申请的）：成功开启IMAP/SMTP服务，在第三方客户端登录时，腾讯提供的授权码。注意不是邮箱密码
  private final String MAIL_FROM_NAME = "Developer Gong";//发件人用户名(随意改写)
  private final String MAIL_TO_NAME = "shoujianren";//收件人用户名(随意改写)
  private boolean isDebug=true;//是否打印发送邮件的调试提示信息
  private final String MAIL_SMTP_HOST = "smtp.gmail.com";//发送邮件服务器：smtp.qq.com
  private final String MAIL_SMTP_PORT = "587";//使用465或587端口
  private final String MAIL_SMTP_AUTH = "true";//设置使用验证
  private final String MAIL_SMTP_STARTTLS_ENABLE = "true";//使用 STARTTLS安全连接
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
   * @return*/

  public void sendEmail(String emailTo,String subject,String content){
    Properties props = new Properties();
    props.put("mail.smtp.host", MAIL_SMTP_HOST);
    props.put("mail.smtp.port", MAIL_SMTP_PORT);//使用465或587端口
    props.put("mail.smtp.auth", MAIL_SMTP_AUTH);//设置使用验证
    props.put("mail.smtp.starttls.enable",MAIL_SMTP_STARTTLS_ENABLE);//使用 STARTTLS安全连接
    try {
      PopupAuthenticator auth = new PopupAuthenticator();
      Session session = Session.getInstance(props, auth);
      session.setDebug(isDebug);//打印Debug信息
      MimeMessage message = new MimeMessage(session);
      Address addressFrom = new InternetAddress(MAIL_FROM_EMAIL, MAIL_FROM_NAME);//第一个参数为发件人邮箱地址；第二个参数为发件人用户名(随意改写)
      Address addressTo = new InternetAddress(emailTo, MAIL_TO_NAME);//第一个参数为接收方电子邮箱地址；第二个参数为接收方用户名
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
        System.out.println("发送成功");
      }
    } catch (Exception e) {
      System.out.println(e.toString());
      System.out.println("发送失败");
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