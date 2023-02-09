package lk.ijse.sakya.dao.custom.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.sakya.dao.custom.ExamDAO;
import lk.ijse.sakya.dao.util.CrudUtil;
import lk.ijse.sakya.db.DBConnection;
import lk.ijse.sakya.entity.custom.Exam;
import lk.ijse.sakya.service.custom.ExamStudentService;
import lk.ijse.sakya.service.custom.impl.ExamStudentServiceImpl;


import java.sql.ResultSet;
import java.sql.SQLException;

public class ExamDAOImpl implements ExamDAO {
    ExamStudentService examStudentService;

    public void initialize(){
        examStudentService = new ExamStudentServiceImpl();

    }

    @Override
    public boolean add(Exam exam) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("insert into exam values (?,?,?,?)", exam.getExamId(), exam.getClassId(),
                exam.getModuleId(), exam.getDate());
    }

    @Override
    public boolean update(Exam obj) throws SQLException, ClassNotFoundException {
       throw new RuntimeException("Not Implemented Yet");
    }

    @Override
    public Exam search(String s) throws SQLException, ClassNotFoundException {
        throw new RuntimeException("Not Implemented Yet");
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        throw new RuntimeException("Not Implemented Yet");
    }

    @Override
    public String getNewExamId() throws SQLException, ClassNotFoundException {
        String lastExamId=getLastExamId();
        if(lastExamId==null){
            return "E-0000000000001";
        }else{
            String[] split=lastExamId.split("[E][-]");
            int lastDigits=Integer.parseInt(split[1]);
            lastDigits++;
            String newExamId=String.format("E-%013d", lastDigits);
            return newExamId;
        }
    }

    @Override
    public String getLastExamId() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT id from exam order by id DESC limit 1");
        if(rs.next()){
            return rs.getString(1);
        }
        return null;
    }

    @Override
    public ObservableList<Exam> getAllExamsByClassIdAndModuleId(String cId, String mId) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT * from exam where class_id = ? AND m_id = ? ", cId, mId);
        ObservableList<Exam> list = FXCollections.observableArrayList();
        while(rs.next()){
            list.add(new Exam(rs.getString(1),rs.getString(2), rs.getString(3),
                    rs.getString(4),null));
        }
        return list;
    }
}
