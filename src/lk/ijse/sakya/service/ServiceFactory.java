package lk.ijse.sakya.service;

import lk.ijse.sakya.service.custom.LectureService;
import lk.ijse.sakya.service.custom.impl.*;

public class ServiceFactory {
    private static ServiceFactory serviceFactory;
    private ServiceFactory(){
    }

    public static ServiceFactory getInstance(){
        if(serviceFactory==null)serviceFactory=new ServiceFactory();
        return serviceFactory;
    }

    public <T extends SuperService>T getService(ServiceType serviceType){
        switch (serviceType){
            case EXAM:return (T)new ExamServiceImpl();
            case USER:return (T)new UserServiceImpl();
            case COURSE:return (T)new CourseServiceImpl();
            case MODULE:return (T)new ModuleServiceImpl();
            case LECTURE:return (T)new LectureServiceImpl();
            case PAYMENT:return (T)new PaymentServiceImpl();
            case STUDENT:return (T)new StudentServiceImpl();
            case SUBJECT:return (T)new SubjectServiceImpl();
            case PRINTBILL:return (T) new PrintBillServiceImpl();
            case ATTENDENCE:return (T)new AttendenceServiceImpl();
            case EXAMSTUDENT:return (T)new ExamStudentServiceImpl();
            case STUDENTCOURSE:return (T)new StudentCourseServiceImpl();

        }
        return null;
    }

}
