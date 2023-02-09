package lk.ijse.sakya.service.custom;

import javafx.collections.ObservableList;
import lk.ijse.sakya.entity.custom.Module;


import java.sql.SQLException;

public interface ModuleService {
    ObservableList<Module> getModuleOfSubect(String subjectId) throws SQLException, ClassNotFoundException;
    String getNewModuleId() throws SQLException, ClassNotFoundException;
    boolean addModule(Module temp) throws SQLException, ClassNotFoundException;
    boolean updateModule(Module module) throws SQLException, ClassNotFoundException;
    boolean deleteModule(Module module) throws SQLException, ClassNotFoundException;

}
