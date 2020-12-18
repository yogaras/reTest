package com.estimulo.common.servlet.controller;


import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.estimulo.common.servlet.ModelAndView;

public class SendEmailController extends AbstractController {
   protected final Log logger = LogFactory.getLog(this.getClass());

   private Multipart multipart;

   public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
      
      if (logger.isDebugEnabled()) {
            logger.debug(" SendEmailController : handleRequestInternal 시작 ");
        }
      
      String fileName = "Estimate.jrxml";
      String savePath = "C:\\hoprj\\project\\estimulo\\WebContent\\resources\\iReportForm";
      
      String host = "smtp.naver.com";
      final String user = "damhyang12";
      final String password = "fbghqja1590";

      String to = "damhyang12@naver.com";

      // Get the session object
      Properties props = new Properties();
      props.put("mail.smtp.host", host);
      props.put("mail.smtp.auth", "true");

      Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
         protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(user, password);
         }
      });

      // Compose the message
      try {
         MimeMessage message = new MimeMessage(session);
         message.setFrom(new InternetAddress(user));
         message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

         // Subject
         
         switch (1) {
		case 1:
			
			break;

		default:
			break;
		}
         message.setSubject("요청하신 견적서 입니다.");
         multipart = new MimeMultipart();
               
         // Text
         MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setText("요청하신 견적서 입니다. ");
            multipart.addBodyPart(mbp1);

         // send the message
         if(fileName != null){
               DataSource source = new FileDataSource(savePath+"\\"+fileName);
               BodyPart messageBodyPart = new MimeBodyPart();
               messageBodyPart.setDataHandler(new DataHandler(source));
               messageBodyPart.setFileName(fileName);
               multipart.addBodyPart(messageBodyPart);
           }
         message.setContent(multipart);
            Transport.send(message);
         System.out.println("메일 발송 성공!");

      } catch (MessagingException e) {
         e.printStackTrace();
      }
      if (logger.isDebugEnabled()) {
            logger.debug(" ReportController : handleRequestInternal 종료 ");
        }
        return null;
   }
}
