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
}
