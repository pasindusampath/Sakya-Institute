package lk.ijse.sakya.service.custom.impl;

import javafx.collections.ObservableList;
import lk.ijse.sakya.dao.custom.ModuleDAO;
import lk.ijse.sakya.dao.custom.impl.ModuleDAOImpl;
import lk.ijse.sakya.entity.custom.Module;
import lk.ijse.sakya.service.custom.ModuleService;

import java.sql.SQLException;

public class ModuleServiceImpl implements ModuleService {
    ModuleDAO moduleDAO = new ModuleDAOImpl();
    @Override
    public ObservableList<Module> getModuleOfSubect(String subjectId) throws SQLException, ClassNotFoundException {
        return moduleDAO.getModuleOfSubect(subjectId);
    }

    @Override
    public String getNewModuleId() throws SQLException, ClassNotFoundException {
        return moduleDAO.getNewModuleId();
    }

    @Override
    public boolean addModule(Module temp) throws SQLException, ClassNotFoundException {
        return moduleDAO.add(temp);
    }

    @Override
    public boolean updateModule(Module module) throws SQLException, ClassNotFoundException {
        return moduleDAO.update(module);
    }

    @Override
    public boolean deleteModule(Module module) throws SQLException, ClassNotFoundException {
        return moduleDAO.delete(module);
    }
}
