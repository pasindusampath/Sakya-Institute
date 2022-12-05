package lk.ijse.sakya.controller.admindashboard;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.sakya.db.DBConnection;
import lk.ijse.sakya.dto.Income;
import lk.ijse.sakya.dto.IncomeTM;
import lk.ijse.sakya.dto.User;
import lk.ijse.sakya.interfaces.DashBoard;
import lk.ijse.sakya.model.PaymentController;
import lk.ijse.sakya.thread.PrintBillTask;
import lk.ijse.sakya.thread.SendMail;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewIncomeFormController implements DashBoard {
    public TableView tblIncome;
    public TableColumn colClass;
    public TableColumn colIncome;
    public StackedBarChart chart;
    public CheckBox checkBox;
    public Label lblTotal;
    public JFXButton btnPrint;
    public JFXProgressBar progress;
    private User loggedUser;


    public void initialize(){
        progress.setVisible(false);
        setIncomeTable();
        setChart();

    }

    public void setIncomeTable(){
        double total = 0;
        colClass.setCellValueFactory(new PropertyValueFactory<IncomeTM,String>("className"));
        colIncome.setCellValueFactory(new PropertyValueFactory<IncomeTM,Double>("income"));
        try {
            ArrayList<Income> list = PaymentController.getIncomeByMonth(Integer.parseInt(String.valueOf(LocalDate.now().
                    getMonthValue())));
            ObservableList<IncomeTM> list1 = FXCollections.observableArrayList();
            for(Income ob : list){
                total=total+ob.getAmount();
                list1.add(new IncomeTM(ob.getYear()+"-"+ob.getSubName()+"-"+ob.getGrade()+"-"+
                        ob.getTeacherName(),ob.getAmount()));
            }
            tblIncome.setItems(list1);
            lblTotal.setText(String.format("%.2f",total));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setChart(){
        try {
            HashMap hm = PaymentController.getMonthlyIncomeForInstitute(LocalDate.now().getYear());
            XYChart.Series series = new XYChart.Series();
            series.setName(String.valueOf(LocalDate.now().getYear()));
            for(int i = 1 ; i <=12 ; i++) {
                if (hm.get(i) == null) {
                    series.getData().add(new XYChart.Data<>(Month.of(i).toString(), 0));
                } else {
                    series.getData().add(new XYChart.Data<>(Month.of(i).toString(), Double.parseDouble(String.valueOf(hm.get(i)))));
                }
            }
            chart.getData().add(series);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnPrintOnAction(ActionEvent actionEvent) {
        printBill();
    }

    public void printBill(){
        //btnPrint.setDisable(true);
        HashMap<String, Object> para=new HashMap<>();
        para.put("total","25000.00");
        String billPath = "G:\\IJSE\\GDSE 63\\DBP\\FinalProject\\Sakya Institute\\src\\lk\\ijse\\sakya\\report\\IncomeReport.jrxml";
        String sql = "SELECT sp.c_id , c.year as course_year,s.name as subject_name,s.grade,u.name as teacher_name," +
                "sum(sp.amount*0.2) as income,extract(YEAR FROM sp.date) as year1  from student_payment sp inner" +
                " join course c on sp.c_id = c.id inner join subject s on c.sub_id = s.id inner join user u on " +
                "c.teacherId = u.id where sp.month = '"+LocalDate.now().getMonthValue()+"'  group by sp.c_id,year1 having year1 ='"+LocalDate.now().getYear()+"'";
        String savePath = "G:\\IncomeReports\\"+LocalDate.now().getYear()+LocalDate.now().getMonth().toString()+LocalDate.now().getDayOfMonth()+ LocalTime.now().getHour()+LocalTime.now().getMinute()+LocalTime.now().getSecond()+".pdf";
        PrintBillTask task = new PrintBillTask(billPath,sql,para,savePath);
        progress.progressProperty().bind(task.progressProperty());
        task.valueProperty().addListener((a,oldValue,newValue)->{
            progress.progressProperty().unbind();
            progress.setVisible(false);
            if(newValue!=null){
                if(checkBox.isSelected()){
                    sendMail(newValue);
                }
            }
        });

        task.messageProperty().addListener((a,old,nw)->{
            new Alert(Alert.AlertType.ERROR,"Send Mail Failed").show();
        });
        Thread t1 = new Thread(task);
        if(!progress.isVisible()){
            t1.start();
            progress.setVisible(true);
        }
    }

    public void sendMail(File file){
        SendMail ob = new SendMail(loggedUser.getGmail(),"Check Your Requested Report ","Income Report",file);
        Thread t1 = new Thread(ob);
        t1.start();
    }

    @Override
    public void setLoggedUser(User user) {
        loggedUser=user;
        //System.out.println(user.getName());
    }
}
