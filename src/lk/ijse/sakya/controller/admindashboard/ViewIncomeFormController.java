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

import lk.ijse.sakya.dto.Income;
import lk.ijse.sakya.dto.IncomeTM;

import lk.ijse.sakya.entity.custom.User;
import lk.ijse.sakya.service.interfaces.DashBoard;

import lk.ijse.sakya.service.custom.PaymentService;
import lk.ijse.sakya.service.custom.PrintBillService;
import lk.ijse.sakya.service.custom.impl.PaymentServiceImpl;
import lk.ijse.sakya.service.custom.impl.PrintBillServiceImpl;

import java.nio.file.FileSystems;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
//Done
public class ViewIncomeFormController implements DashBoard {
    private PaymentService paymentService;
    public TableView tblIncome;
    public TableColumn colClass;
    public TableColumn colIncome;
    public StackedBarChart chart;
    public CheckBox checkBox;
    public Label lblTotal;
    public JFXButton btnPrint;
    public JFXProgressBar progress;
    private User loggedUser;
    private PrintBillService printBillService;


    public void initialize(){
        paymentService = new PaymentServiceImpl();
        printBillService = new PrintBillServiceImpl();
        progress.setVisible(false);
        setIncomeTable();
        setChart();

    }

    public void setIncomeTable(){
        double total = 0;
        colClass.setCellValueFactory(new PropertyValueFactory<IncomeTM,String>("className"));
        colIncome.setCellValueFactory(new PropertyValueFactory<IncomeTM,Double>("income"));
        try {
            ArrayList<Income> list = paymentService.getIncomeByMonth(Integer.parseInt(String.valueOf(LocalDate.now().
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
            HashMap hm = paymentService.getMonthlyIncomeForInstitute(LocalDate.now().getYear());
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

        HashMap<String, Object> para=new HashMap<>();
        para.put("total","25000.00");
        String billPath = FileSystems.getDefault().getPath("src/lk/ijse/sakya/report/IncomeReport.jrxml").toAbsolutePath().toString();
        String sql = "SELECT sp.c_id , c.year as course_year,s.name as subject_name,s.grade,u.name as teacher_name," +
                "sum(sp.amount*0.2) as income,extract(YEAR FROM sp.date) as year1  from student_payment sp inner" +
                " join course c on sp.c_id = c.id inner join subject s on c.sub_id = s.id inner join user u on " +
                "c.teacherId = u.id where sp.month = '"+ LocalDate.now().getMonthValue()+"'  group by sp.c_id,year1 " +
                "having year1 ='"+LocalDate.now().getYear()+"'";
        String savePath =FileSystems.getDefault().getPath("IncomeReport\\"+LocalDate.now().getYear()+LocalDate.now().
                getMonth().toString()+LocalDate.now().getDayOfMonth()+ LocalTime.now().getHour()+LocalTime.now().
                getMinute()+ LocalTime.now().getSecond()+".pdf").toAbsolutePath().toString();


        printBillService.printBill(loggedUser,progress,checkBox.isSelected(),billPath,sql,savePath,para);

    }

    @Override
    public void setLoggedUser(User user) {
        loggedUser=user;
        //System.out.println(user.getName());
    }
}
