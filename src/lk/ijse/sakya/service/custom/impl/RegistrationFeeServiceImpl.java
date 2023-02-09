package lk.ijse.sakya.service.custom.impl;

import lk.ijse.sakya.dao.custom.RegistrationFeeDAO;
import lk.ijse.sakya.dao.custom.impl.RegistrationFeeDAOImpl;
import lk.ijse.sakya.entity.custom.RegistrationFee;

import java.sql.SQLException;

public class RegistrationFeeServiceImpl implements RegistrationFeeService{
    RegistrationFeeDAO registrationFeeDAO;

    public RegistrationFeeServiceImpl() {
        registrationFeeDAO = new RegistrationFeeDAOImpl();
    }

    @Override
    public boolean addData(RegistrationFee ob) throws SQLException, ClassNotFoundException {
        return registrationFeeDAO.add(ob);
    }
}
