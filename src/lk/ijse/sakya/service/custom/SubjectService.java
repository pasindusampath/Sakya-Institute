package lk.ijse.sakya.service.custom;

import javafx.collections.ObservableList;
import lk.ijse.sakya.entity.custom.Subject;

import java.sql.SQLException;

public interface SubjectService {
    ObservableList<Subject> getSubjects(int grade) throws SQLException, ClassNotFoundException;
    boolean addSubject(Subject sub) throws SQLException, ClassNotFoundException;
    String getNewSubjectId() throws SQLException, ClassNotFoundException;
    boolean updateSubject(Subject subject) throws SQLException, ClassNotFoundException;
    boolean deleteSubject(String id) throws SQLException, ClassNotFoundException;
}
