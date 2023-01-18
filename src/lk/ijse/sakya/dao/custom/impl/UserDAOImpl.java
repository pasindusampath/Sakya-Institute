package lk.ijse.sakya.dao.custom.impl;

import javafx.collections.ObservableList;
import lk.ijse.sakya.dao.custom.UserDAO;
import lk.ijse.sakya.entity.custom.User;
import lk.ijse.sakya.util.CrudUtil;


import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOImpl implements UserDAO {
    @Override
    public boolean add(User user) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("insert into user values(?,?,?,?,?,?,?,?)", user.getId(), user.getName()
                , user.getType(), user.getGmail(), user.getContact(), user.getPassword(), user.getDob(), user.getAddress());
    }

    @Override
    public boolean update(User obj) {
        return false;
    }

    @Override
    public User search(String id) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT * from user where id = ?", id);
        if (rs.next()) {
            return new User(rs.getString(1), rs.getString(2), rs.getString(3)
                    , rs.getString(4), rs.getString(5), rs.getString(6)
                    , rs.getString(7), rs.getString(8));
        }
        return null;
    }

    @Override
    public boolean delete(String s) {
        return false;
    }

    @Override
    public User searchUserByGmail(String gmail) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT * from user where gmail = ?", gmail);
        if (rs.next()) {
            return new User(rs.getString(1), rs.getString(2), rs.getString(3)
                    , rs.getString(4), rs.getString(5), rs.getString(6)
                    , rs.getString(7), rs.getString(8));
        }
        return null;
    }

    @Override
    public String getNewUserId() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public String getLastUserId() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public ObservableList<User> getAllUsers() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public ObservableList<User> getTeachers() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean resetPassword(User user) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public ObservableList<User> searchUser(String searchBy, String text) throws SQLException, ClassNotFoundException {
        return null;
    }
}
