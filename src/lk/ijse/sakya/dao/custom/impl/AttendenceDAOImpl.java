package lk.ijse.sakya.dao.custom.impl;



import lk.ijse.sakya.dao.custom.AttendenceDAO;
import lk.ijse.sakya.dao.util.CrudUtil;
import lk.ijse.sakya.db.DBConnection;



import lk.ijse.sakya.entity.custom.Attendence;




import java.sql.SQLException;

import java.util.ArrayList;

public class AttendenceDAOImpl implements AttendenceDAO {

    @Override
    public boolean add(Attendence ob) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("insert into attendence values(?,?,?)", ob.getlId(), ob.getsId(),
                ob.getStatus());
    }

    @Override
    public boolean update(Attendence ob) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("update attendence set status = ? where lec_id = ? AND st_id = ?",
                ob.getStatus(), ob.getlId(), ob.getsId());
    }

    @Override
    public Attendence search(String s) throws SQLException, ClassNotFoundException {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public boolean add(ArrayList<Attendence> ob) throws SQLException, ClassNotFoundException {
        for (Attendence obj : ob) {
            if (!add(obj)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean updateAttendence(ArrayList<Attendence> list) throws SQLException, ClassNotFoundException {
        DBConnection.getInstance().getConnection().setAutoCommit(false);
        try{
            for (Attendence ob : list) {
                if (!update(ob)) {
                    DBConnection.getInstance().getConnection().rollback();
                    return false;
                }
            }
            DBConnection.getInstance().getConnection().commit();
            return true;
        }catch (SQLException e){
            DBConnection.getInstance().getConnection().rollback();
            throw e;
        }catch (ClassNotFoundException e){
            DBConnection.getInstance().getConnection().rollback();
            throw e;
        }finally {
            DBConnection.getInstance().getConnection().setAutoCommit(true);
        }
    }


}
