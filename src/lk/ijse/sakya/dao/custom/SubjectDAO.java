package lk.ijse.sakya.dao.custom;

import javafx.collections.ObservableList;
import lk.ijse.sakya.dao.CrudDAO;
import lk.ijse.sakya.entity.custom.Subject;

import java.sql.SQLException;

public interface SubjectDAO extends CrudDAO<Subject,String> {
    String getNewSubjectId() throws SQLException, ClassNotFoundException;
    String getLastSubjectId() throws SQLException, ClassNotFoundException;
    ObservableList<Subject> getSubjects(int grade) throws SQLException, ClassNotFoundException;


}
