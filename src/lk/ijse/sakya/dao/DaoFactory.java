package lk.ijse.sakya.dao;

import lk.ijse.sakya.dao.custom.ExamStudentDAO;
import lk.ijse.sakya.dao.custom.impl.*;

public class DaoFactory {
    private static DaoFactory daoFactory;

    private DaoFactory(){

    }

    public static DaoFactory getInstance(){
        if(daoFactory==null)daoFactory=new DaoFactory();
        return daoFactory;
    }

    public <T extends SuperDAO>T getDao(DaoType type){
        switch (type){
            case Exam:return (T)new ExamDAOImpl();
            case User:return (T)new UserDAOImpl();
            case Query:return (T)new QueryDAOImpl();
            case Course:return (T)new CourseDAOImpl();
            case Module:return (T)new ModuleDAOImpl();
            case Lecture:return (T)new LectureDAOImpl();
            case Payment:return (T)new PaymentDAOImpl();
            case Student:return (T)new StudentDAOImpl();
            case Subject:return (T)new SubjectDAOImpl();
            case Attendance:return (T)new AttendenceDAOImpl();
            case ExamStudent:return (T)new ExamStudentDAOImpl();
            case RegistrationFee:return (T)new RegistrationFeeDAOImpl();
        }
        return null;
    }
}
