package lk.ijse.sakya.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TempCon {
    private Connection con;
    private static TempCon tempCon;

    private TempCon() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con= DriverManager.getConnection("jdbc:mysql://localhost:3306/sakya","root","1234");
    }

    public static TempCon getInstance() throws SQLException, ClassNotFoundException {
        if(tempCon==null){
            tempCon = new TempCon();
        }
        return tempCon;
    }

    public Connection getCon(){
        return con;
    }
}
