package lk.ijse.sakya.dao.custom.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.sakya.dao.custom.ModuleDAO;
import lk.ijse.sakya.entity.custom.Module;
import lk.ijse.sakya.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ModuleDAOImpl implements ModuleDAO {
    @Override
    public boolean add(Module temp) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("insert into module values(?,?,?)", temp.getId(), temp.getName(), temp.getSubId());
    }

    @Override
    public boolean update(Module module) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("update module set name=? where id=?",module.getName(),module.getId());
    }

    @Override
    public Module search(String s) throws SQLException, ClassNotFoundException {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("delete from module where id = ?",s);
    }

    @Override
    public boolean delete(Module module) throws SQLException, ClassNotFoundException {
        return delete(module.getId());
    }

    @Override
    public String getNewModuleId() throws SQLException, ClassNotFoundException {
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

    @Override
    public String getLastModuleId() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT id from module order by id DESC limit 1");
        if(rs.next()){
            return rs.getString(1);
        }
        return null;
    }

    @Override
    public ObservableList<Module> getModuleOfSubect(String subjectId) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT * FROM module WHERE sub_id = ? ",subjectId);
        ObservableList<Module> mList = FXCollections.observableArrayList();
        while(rs.next()){
            mList.add(new Module(rs.getString(1),rs.getString(2), rs.getString(3)));
        }
        return mList;
    }
}
