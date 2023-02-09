package lk.ijse.sakya.controller.teachedashboard;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXProgressBar;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

public class  ViewIncomeFormController implements DashBoard {
    public TableView table;
    public TableColumn colClass;
    public TableColumn colSubTotal;
    public StackedBarChart chart;
    public JFXCheckBox checkBox;
    public Label lblTotal;
    public JFXProgressBar progress;
    private User loggedUser;
    private PaymentService paymentService;
    PrintBillService printBillService;

    public void initialize() {
        progress.setVisible(false);
        paymentService = new PaymentServiceImpl();
        printBillService = new PrintBillServiceImpl();
    }

    public void btnPrintOnAction(ActionEvent actionEvent) {
        printBill();
    }

    public void setTable() {
        colClass.setCellValueFactory(new PropertyValueFactory<IncomeTM, String>("className"));
        colSubTotal.setCellValueFactory(new PropertyValueFactory<IncomeTM, Double>("income"));
        double total=0;
        try {
            ArrayList<Income> list = paymentService.getIncomeByMonth(LocalDate.now().getMonthValue(),
                    loggedUser.getId());

            ArrayList<IncomeTM> list1 = new ArrayList<>();
            for (Income ob : list) {
                total=total+ob.getAmount();
                String fs = String.format("%8d\t\t%15s\t\tGRADE :%5d", ob.getYear(), ob.getSubName(), ob.getGrade());
                list1.add(new IncomeTM(fs, ob.getAmount()));
            }
            table.setItems(FXCollections.observableArrayList(list1));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        lblTotal.setText(String.valueOf(total));
    }


    public void setChart() {
        try {
            HashMap hm = paymentService.getMonthlyIncomeForTeacher(LocalDate.now().getYear(),loggedUser.getId());
            XYChart.Series series = new XYChart.Series();
            series.setName(String.valueOf(LocalDate.now().getYear()));
            for (int i = 1; i <= 12; i++) {
                if (hm.get(i) == null) {
                    series.getData().add(new XYChart.Data<>(Month.of(i).toString(), 0));
                } else {
                    series.getData().add(new XYChart.Data<>(Month.of(i).toString(), Double.parseDouble(String.valueOf
                            (hm.get(i)))));
                }
            }
            chart.getData().add(series);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void printBill(){

        String billPath =FileSystems.getDefault().getPath("src/lk/ijse/sakya/report/IncomeReportTeacher.jrxml").
                toAbsolutePath().toString();
        String sql = "SELECT sp.c_id , c.year as course_year,s.name as subject_name,s.grade,sum(sp.amount*0.8) as " +
                "income,extract(YEAR FROM sp.date) as year1  from student_payment sp inner join course c on " +
                "sp.c_id = c.id inner join subject s on c.sub_id = s.id inner join user u on c.teacherId = u.id " +
                "where sp.month = "+LocalDate.now().getMonthValue()+" AND u.id = '"+loggedUser.getId()+"' group by " +
                "sp.c_id,year1 having year1 = "+LocalDate.now().getYear();
        String savePath = FileSystems.getDefault().getPath("IncomeReportsTeacher\\"+LocalDate.now().getYear()+LocalDate.now().getMonth().toString()+
                LocalDate.now().getDayOfMonth()+ LocalTime.now().getHour()+LocalTime.now().getMinute()+LocalTime.now()
                .getSecond()+".pdf").toAbsolutePath().toString();
        HashMap<String, Object> para=new HashMap<>();
        para.put("total",lblTotal.getText());
        para.put("teacherName",loggedUser.getName());
        printBillService.printBill(loggedUser,progress,checkBox.isSelected(),billPath,sql,savePath,para);

    }
    @Override
    public void setLoggedUser(User user) {
        loggedUser = new User(user.getId(), user.getName(), user.getType(), user.getGmail(), user.getContact(), user.getPassword(), user.getDob(), user.getAddress());
        setTable();
        setChart();
    }
}
