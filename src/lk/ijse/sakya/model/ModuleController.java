package lk.ijse.sakya.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.sakya.dto.Module;
import lk.ijse.sakya.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ModuleController {
    public static String getNewModuleId() throws SQLException, ClassNotFoundException {
        String lastModuleId=getLastModuleId();
        if(lastModuleId==null){
            return "M-001";
        }else{
            String[] split=lastModuleId.split("[M][-]");
            int lastDigits=Integer.parseInt(split[1]);
            lastDigits++;
            String newOrderId=String.format("M-%03d", lastDigits);
            return newOrderId;
        }
    }

    private static String getLastModuleId() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT id from module order by id DESC limit 1");
        if(rs.next()){
            return rs.getString(1);
        }
        return null;
    }

    public static ObservableList<Module> getModuleOfSubect(String subjectId) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT * FROM module WHERE sub_id = ? ",subjectId);
        ObservableList<Module> mList = FXCollections.observableArrayList();
        while(rs.next()){
            mList.add(new Module(rs.getString(1),rs.getString(2), rs.getString(3)));
        }
        return mList;
    }
    public static boolean addModule(Module temp) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("insert into module values(?,?,?)", temp.getId(), temp.getName(), temp.getSubId());
    }

    public static boolean updateModule(Module module) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("update module set name=? where id=?",module.getName(),module.getId());
    }

    public static boolean deleteModule(Module module) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("delete from module where id = ?",module.getId());
    }
}
