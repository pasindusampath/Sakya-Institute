package lk.ijse.sakya.service.custom.impl;

import com.jfoenix.controls.JFXProgressBar;
import lk.ijse.sakya.entity.custom.User;
import lk.ijse.sakya.service.custom.PrintBillService;
import lk.ijse.sakya.thread.PrintBillTask;
import lk.ijse.sakya.thread.SendMail;

import java.io.File;
import java.nio.file.FileSystems;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;

public class PrintBillServiceImpl implements PrintBillService {


    public void printBill(User loggedUser, String total, JFXProgressBar progress,boolean mail){
        String billPath = FileSystems.getDefault().getPath("src/lk/ijse/sakya/report/IncomeReportTeacher.jrxml").
                toAbsolutePath().toString();
        String sql = "SELECT sp.c_id , c.year as course_year,s.name as subject_name,s.grade,sum(sp.amount*0.8) as " +
                "income,extract(YEAR FROM sp.date) as year1  from student_payment sp inner join course c on " +
                "sp.c_id = c.id inner join subject s on c.sub_id = s.id inner join user u on c.teacherId = u.id " +
                "where sp.month = "+ LocalDate.now().getMonthValue()+" AND u.id = '"+loggedUser.getId()+"' group by " +
                "sp.c_id,year1 having year1 = "+LocalDate.now().getYear();
        String savePath = FileSystems.getDefault().getPath("IncomeReportsTeacher\\"+LocalDate.now().getYear()+LocalDate.now().getMonth().toString()+
                LocalDate.now().getDayOfMonth()+ LocalTime.now().getHour()+LocalTime.now().getMinute()+LocalTime.now()
                .getSecond()+".pdf").toAbsolutePath().toString();
        HashMap<String, Object> para=new HashMap<>();
        para.put("total",total);
        para.put("teacherName",loggedUser.getName());
        PrintBillTask task = new PrintBillTask(billPath,sql,para,savePath);
        progress.progressProperty().bind(task.progressProperty());
        task.valueProperty().addListener((a,oldValue,newValue)->{
            progress.progressProperty().unbind();
            progress.setVisible(false);
            if(newValue!=null){
                if(mail){
                    sendMail(newValue,loggedUser);
                }
            }
        });
        Thread t1 = new Thread(task);
        if(!progress.isVisible()){
            t1.start();
            progress.setVisible(true);
        }
    }

    @Override
    public void printBill(User loggedUser, JFXProgressBar progress,boolean mail,String billPath,String sql,String savePath,HashMap<String,Object> para){
        PrintBillTask task = new PrintBillTask(billPath,sql,para,savePath);
        progress.progressProperty().bind(task.progressProperty());
        task.valueProperty().addListener((a,oldValue,newValue)->{
            progress.progressProperty().unbind();
            progress.setVisible(false);
            if(newValue!=null){
                if(mail){
                    sendMail(newValue,loggedUser);
                }
            }
        });
        Thread t1 = new Thread(task);
        if(!progress.isVisible()){
            t1.start();
            progress.setVisible(true);
        }
    }


    public void sendMail(File file,User loggedUser){
        SendMail ob = new SendMail(loggedUser.getGmail(),"Check Your Requested Report ","Income Report",file);
        Thread t1 = new Thread(ob);
        t1.start();
    }
}
