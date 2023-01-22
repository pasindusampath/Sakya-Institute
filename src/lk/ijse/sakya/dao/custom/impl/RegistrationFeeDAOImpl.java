package lk.ijse.sakya.dao.custom.impl;

import lk.ijse.sakya.dao.custom.RegistrationFeeDAO;
import lk.ijse.sakya.entity.custom.RegistrationFee;
import lk.ijse.sakya.util.CrudUtil;

import java.sql.SQLException;

public class RegistrationFeeDAOImpl implements RegistrationFeeDAO {
    @Override
    public boolean add(RegistrationFee ob) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("insert into registration_fee values(?,?,?)",ob.getId()
                ,ob.getFee(),ob.getDate());
    }

    @Override
    public boolean update(RegistrationFee obj) throws SQLException, ClassNotFoundException {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public RegistrationFee search(String s) throws SQLException, ClassNotFoundException {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        throw new RuntimeException("Not Implemented");
    }
}
