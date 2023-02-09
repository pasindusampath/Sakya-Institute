package lk.ijse.sakya.service.custom;

import com.jfoenix.controls.JFXProgressBar;
import javafx.collections.ObservableList;
import lk.ijse.sakya.entity.custom.User;
import lk.ijse.sakya.service.SuperService;


import java.sql.SQLException;

public interface UserService extends SuperService {
    String getNewUserId() throws SQLException, ClassNotFoundException;
    boolean addUser(User user) throws SQLException, ClassNotFoundException;
    ObservableList<User> getTeachers() throws SQLException, ClassNotFoundException;
    ObservableList<User> getAllUsers() throws SQLException, ClassNotFoundException;
    boolean updateUser(User user) throws SQLException, ClassNotFoundException;
    boolean deleteUser(User user) throws SQLException, ClassNotFoundException;
    ObservableList<User> searchUser(String searchBy, String text) throws SQLException, ClassNotFoundException;
    void sendUserDetailsMail(JFXProgressBar prograss, User user, String subject) throws Exception;
    User searchUserByGmail(String gmail) throws SQLException, ClassNotFoundException;
    boolean resetPassword(User user) throws SQLException, ClassNotFoundException;



}
