package lk.ijse.sakya.dao.custom;

import javafx.collections.ObservableList;
import lk.ijse.sakya.dao.CrudDAO;
import lk.ijse.sakya.entity.custom.User;


import java.sql.SQLException;

public interface UserDAO extends CrudDAO<User,String> {
    //User searchUser(String id) throws SQLException, ClassNotFoundException;
    User searchUserByGmail(String gmail) throws SQLException, ClassNotFoundException;
    String getNewUserId() throws SQLException, ClassNotFoundException;
    String getLastUserId() throws SQLException, ClassNotFoundException;
    ObservableList<User> getAllUsers() throws SQLException, ClassNotFoundException;
    ObservableList<User> getTeachers() throws SQLException, ClassNotFoundException;
    boolean resetPassword(User user) throws SQLException, ClassNotFoundException;
    ObservableList<User> searchUser(String searchBy,String text) throws SQLException, ClassNotFoundException;


}
