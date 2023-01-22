package lk.ijse.sakya.dao.custom;

import javafx.collections.ObservableList;
import lk.ijse.sakya.dao.CrudDAO;
import lk.ijse.sakya.entity.custom.Module;

import java.sql.SQLException;

public interface ModuleDAO extends CrudDAO<Module,String> {
    String getNewModuleId() throws SQLException, ClassNotFoundException;
    String getLastModuleId() throws SQLException, ClassNotFoundException;
    boolean delete(Module module) throws SQLException, ClassNotFoundException;
    ObservableList<Module> getModuleOfSubect(String subjectId) throws SQLException, ClassNotFoundException;


}
