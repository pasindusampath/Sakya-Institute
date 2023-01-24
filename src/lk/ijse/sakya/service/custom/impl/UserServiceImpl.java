package lk.ijse.sakya.service.custom.impl;

import javafx.collections.ObservableList;
import lk.ijse.sakya.dao.custom.UserDAO;
import lk.ijse.sakya.dao.custom.impl.UserDAOImpl;
import lk.ijse.sakya.entity.custom.User;
import lk.ijse.sakya.service.custom.UserService;

import java.sql.SQLException;

public class UserServiceImpl implements UserService {
    UserDAO userDAO;
    public UserServiceImpl(){
        userDAO = new UserDAOImpl();

    }
    @Override
    public ObservableList<User> getTeachers() throws SQLException, ClassNotFoundException {
        return userDAO.getTeachers();
    }
}
