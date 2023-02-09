package lk.ijse.sakya.service.custom.impl;

import lk.ijse.sakya.entity.custom.RegistrationFee;

import java.sql.SQLException;

public interface RegistrationFeeService {
    boolean addData(RegistrationFee ob) throws SQLException, ClassNotFoundException;
}
