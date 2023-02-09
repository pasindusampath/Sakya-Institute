package lk.ijse.sakya.service.custom.impl;

import javafx.collections.ObservableList;
import lk.ijse.sakya.dao.custom.SubjectDAO;
import lk.ijse.sakya.entity.custom.Subject;
import lk.ijse.sakya.service.custom.SubjectService;

import java.sql.SQLException;

public class SubjectServiceImpl implements SubjectService {
    SubjectDAO ob;

    @Override
    public ObservableList<Subject> getSubjects(int grade) throws SQLException, ClassNotFoundException {
        return ob.getSubjects(grade);
    }

    @Override
    public boolean addSubject(Subject sub) throws SQLException, ClassNotFoundException {
        return ob.add(sub);
    }

    @Override
    public String getNewSubjectId() throws SQLException, ClassNotFoundException {
        return ob.getNewSubjectId();
    }

    @Override
    public boolean updateSubject(Subject subject) throws SQLException, ClassNotFoundException {
        return ob.update(subject);
    }

    @Override
    public boolean deleteSubject(String id) throws SQLException, ClassNotFoundException {
        return ob.delete(id);
    }

}
