package lk.ijse.sakya.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.sakya.dto.User;
import lk.ijse.sakya.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserController {
    public static User searchUser(String id) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT * from user where id = ?", id);
        if (rs.next()) {
            return new User(rs.getString(1), rs.getString(2), rs.getString(3)
                    , rs.getString(4), rs.getString(5), rs.getString(6)
                    , rs.getString(7), rs.getString(8));
        }
        return null;
    }
    public static User searchUserByGmail(String gmail) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT * from user where gmail = ?", gmail);
        if (rs.next()) {
            return new User(rs.getString(1), rs.getString(2), rs.getString(3)
                    , rs.getString(4), rs.getString(5), rs.getString(6)
                    , rs.getString(7), rs.getString(8));
        }
        return null;
    }

    public static boolean addUser(User user) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("insert into user values(?,?,?,?,?,?,?,?)", user.getId(), user.getName()
                , user.getType(), user.getGmail(), user.getContact(), user.getPassword(), user.getDob(), user.getAddress());
    }

    public static String getNewUserId() throws SQLException, ClassNotFoundException {
        String lastUserId = getLastUserId();
        if (lastUserId == null) {
            return "U-001";
        } else {
            String[] split = lastUserId.split("[U][-]");
            int lastDigits = Integer.parseInt(split[1]);
            lastDigits++;
            String newUserId = String.format("U-%03d", lastDigits);
            return newUserId;
        }
    }

    private static String getLastUserId() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT id from user order by id DESC limit 1");
        if (rs.next()) {
            return rs.getString(1);
        }
        return null;
    }

    public static ObservableList<User> getAllUsers() throws SQLException, ClassNotFoundException {
        ObservableList<User> ob = FXCollections.observableArrayList();
        ResultSet rs = CrudUtil.execute("SELECT * FROM USER");
        while (rs.next()) {
            ob.add(new User(rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                    rs.getString(6),
                    rs.getString(7),
                    rs.getString(8)));
        }
        return ob;
    }

    public static ObservableList<User> getTeachers() throws SQLException, ClassNotFoundException {
        ObservableList<User> ob = FXCollections.observableArrayList();
        ResultSet rs = CrudUtil.execute("SELECT * from user where type = 'teacher'");
        while (rs.next()) {
            ob.add(new User(rs.getString(1), rs.getString(2), rs.getString(3)
                    , rs.getString(4), rs.getString(5), rs.getString(6)
                    , rs.getString(7), rs.getString(8)));
        }
        return ob;
    }

    public static boolean updateUser(User user) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("update user set name = ? ,type = ? ,gmail = ? ,contact = ? ,dob = ? ," +
                        "address = ? where id = ?", user.getName(), user.getType(), user.getGmail(),
                user.getContact(), user.getDob(), user.getAddress(), user.getId());
    }

    public static boolean deleteUser(User user) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("delete from user where id = ?", user.getId());
    }

    public static boolean resetPassword(User user) throws SQLException, ClassNotFoundException {
       return CrudUtil.execute("update user set password = ? where id = ?",user.getPassword(),user.getId());
    }

    public static ObservableList<User> searchUser(String searchBy,String text) throws SQLException, ClassNotFoundException {
        String sql= "SELECT * from user where "+searchBy+" like '%"+text+"%'";
        ResultSet rs = CrudUtil.execute(sql);
        ObservableList<User> list = FXCollections.observableArrayList();
        while(rs.next()){
            list.add(new User(rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                    rs.getString(6),
                    rs.getString(7),
                    rs.getString(8)));
        }
        return list;
    }
}
