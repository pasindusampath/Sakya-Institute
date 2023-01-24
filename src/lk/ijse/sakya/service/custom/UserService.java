package lk.ijse.sakya.service.custom;

import javafx.collections.ObservableList;
import lk.ijse.sakya.entity.custom.User;


import java.sql.SQLException;

public interface UserService {
    ObservableList<User> getTeachers() throws SQLException, ClassNotFoundException;

}
