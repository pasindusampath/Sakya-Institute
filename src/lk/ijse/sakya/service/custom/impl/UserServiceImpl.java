package lk.ijse.sakya.service.custom.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.jfoenix.controls.JFXProgressBar;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import lk.ijse.sakya.dao.custom.UserDAO;
import lk.ijse.sakya.dao.custom.impl.UserDAOImpl;
import lk.ijse.sakya.entity.custom.User;
import lk.ijse.sakya.service.custom.UserService;
import lk.ijse.sakya.thread.SendMail;

import javax.imageio.ImageIO;
import java.io.File;
import java.nio.file.FileSystems;
import java.sql.SQLException;

public class UserServiceImpl implements UserService {
    UserDAO userDAO;
    public UserServiceImpl(){
        userDAO = new UserDAOImpl();

    }

    @Override
    public String getNewUserId() throws SQLException, ClassNotFoundException {
       return userDAO.getNewUserId();
    }

    @Override
    public boolean addUser(User user) throws SQLException, ClassNotFoundException {
        return userDAO.add(user);
    }

    @Override
    public ObservableList<User> getTeachers() throws SQLException, ClassNotFoundException {
        return userDAO.getTeachers();
    }

    @Override
    public ObservableList<User> getAllUsers() throws SQLException, ClassNotFoundException {
        return userDAO.getAllUsers();
    }

    @Override
    public boolean updateUser(User user) throws SQLException, ClassNotFoundException {
        return userDAO.update(user);
    }

    @Override
    public boolean deleteUser(User user) throws SQLException, ClassNotFoundException {
        return userDAO.delete(user.getId());
    }

    @Override
    public ObservableList<User> searchUser(String searchBy, String text) throws SQLException, ClassNotFoundException {
        return userDAO.searchUser(searchBy,text);
    }

    @Override
    public void sendUserDetailsMail(JFXProgressBar prograss, User user, String subject) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("Use This Username and Password to Login to Sakya Smart Classroom Application");
        sb.append("");
        sb.append("                    Username : "+user.getGmail()+"");
        sb.append("                    Password : "+user.getPassword()+"");
        sb.append("                                                                              ");
        sb.append("                                                                              ");
        sb.append("                                                                              ");
        File file = generateQRCodeImage(user.getId());
        SendMail os = new SendMail(user.getGmail(),sb.toString(),subject,file);
        Thread ob = new Thread(os);
        ob.setDaemon(true);
        os.messageProperty().addListener((h, oldMessage, newMessage) -> {
            new Alert(Alert.AlertType.WARNING, newMessage).show();
            user.setPassword("12345678");
            try {
                userDAO.update(user);
                new Alert(Alert.AlertType.WARNING, "Login The System Using " +
                        "Username : "+user.getGmail()+"" +
                        "Password : "+user.getPassword()).show();
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR,"System Error - User - Database \n" +
                        "Contact Manufactures").show();
            } catch (ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR,"System Error - User - Driver \n" +
                        "Contact Manufactures").show();

            }
        });
        prograss.progressProperty().bind(os.progressProperty());
        prograss.visibleProperty().bind(os.runningProperty());
        ob.start();
    }

    @Override
    public User searchUserByGmail(String gmail) throws SQLException, ClassNotFoundException {
        return userDAO.searchUserByGmail(gmail);
    }

    @Override
    public boolean resetPassword(User user) throws SQLException, ClassNotFoundException {
        return userDAO.delete(user.getId());
    }

    public File generateQRCodeImage(String barcodeText) throws Exception {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix =
                barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 200, 200);
        File outputfile = new File(FileSystems.getDefault().getPath("src\\" +"userQr\\"+barcodeText+".jpg").toAbsolutePath().toString());

        ImageIO.write(MatrixToImageWriter.toBufferedImage(bitMatrix), "jpg", outputfile);
        //sendMail(outputfile);
        return outputfile;
    }


}
