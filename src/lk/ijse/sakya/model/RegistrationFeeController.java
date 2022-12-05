package lk.ijse.sakya.model;

import lk.ijse.sakya.dto.RegistrationFee;
import lk.ijse.sakya.util.CrudUtil;

import java.sql.SQLException;

public class RegistrationFeeController {
    public static boolean addData(RegistrationFee ob) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("insert into registration_fee values(?,?,?)",ob.getId()
        ,ob.getFee(),ob.getDate());
    }
}
