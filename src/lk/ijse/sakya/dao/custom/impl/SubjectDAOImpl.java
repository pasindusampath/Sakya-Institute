package lk.ijse.sakya.dao.custom.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.sakya.dao.custom.SubjectDAO;
import lk.ijse.sakya.entity.custom.Subject;
import lk.ijse.sakya.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SubjectDAOImpl implements SubjectDAO {
    @Override
    public boolean add(Subject sub) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("insert into subject values(?,?,?)",sub.getId(),sub.getName(),sub.getGrade());
    }

    @Override
    public boolean update(Subject obj) throws SQLException, ClassNotFoundException {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public Subject search(String s) throws SQLException, ClassNotFoundException {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public String getNewSubjectId() throws SQLException, ClassNotFoundException {
        String lastOrderId=getLastSubjectId();
        if(lastOrderId==null){
            return "S-001";
        }else{
            String[] split=lastOrderId.split("[S][-]");
            int lastDigits=Integer.parseInt(split[1]);
            lastDigits++;
            String newOrderId=String.format("S-%03d", lastDigits);
            return newOrderId;
        }
    }

    @Override
    public String getLastSubjectId() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT id from subject order by id DESC limit 1");
        if(rs.next()){
            return rs.getString(1);
        }
        return null;
    }

    @Override
    public ObservableList<Subject> getSubjects(int grade) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT * from subject where grade = ?",grade);
        ObservableList<Subject> sList = FXCollections.observableArrayList();
        while(rs.next()){
            Subject temp = new Subject(rs.getString(1),rs.getString(2),rs.getInt(3));
            sList.add(temp);
        }
        return sList;
    }
}
