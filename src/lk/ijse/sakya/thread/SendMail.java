package lk.ijse.sakya.thread;

import com.email.durgesh.Email;
import javafx.concurrent.Task;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;


public class SendMail extends Task<Boolean> {
    String mail;
    String text;
    String subject;
    File file;

    public SendMail(String mail, String text, String subject, File file) {
        this.mail = mail;
        this.text = text;
        this.subject = subject;
        this.file=file;
    }

    public SendMail(String mail, String subject , String text) {
        this.mail = mail;
        this.text = text;
        this.subject=subject;

    }


    @Override
    protected Boolean call() throws Exception {
            try {
                Email mail = new Email("applicationtesting2002@gmail.com", "dspmcvzxuegliaah");
                mail.setFrom("applicationtesting2002@gmail.com", "Sakya Smart Class System");
                mail.setSubject(subject);
                mail.setContent(text, "text/html");
                if(file!=null) {
                    MimeBodyPart mbp1 = new MimeBodyPart();
                    mbp1.setContent(text, "text/html");
                    //mbp1.setText(text);
                    MimeBodyPart mbp2 = new MimeBodyPart();
                    mbp2.attachFile(file);
                    Multipart mp = new MimeMultipart();
                    mp.addBodyPart(mbp1);
                    mp.addBodyPart(mbp2);
                    mail.addAttatchment(mp);
                }

                //mail.addAttatchment();
                mail.addRecipient(this.mail);
                updateProgress(50,100);
                mail.send();
                updateProgress(99,100);
                Thread.sleep(250);
                return true;
            } catch (Exception ex) {
                updateMessage("Connection Error - Sending User Details Error");
                //ex.printStackTrace();
            }
        return false;
    }

}
