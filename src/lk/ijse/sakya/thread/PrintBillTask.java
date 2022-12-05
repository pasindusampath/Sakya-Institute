package lk.ijse.sakya.thread;

import javafx.concurrent.Task;
import lk.ijse.sakya.db.DBConnection;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;

public class   PrintBillTask extends Task<File> {
    String billPath;
    String sql;
    HashMap<String, Object> hm;
    String savePath;

    public PrintBillTask(String billPath,String sql, HashMap<String, Object> hm, String savePath) {
        this.billPath=billPath;
        this.sql = sql;
        this.hm = hm;
        this.savePath = savePath;
    }

    @Override
    protected File call() throws Exception {
        try {
            updateProgress(25,100);
            JasperDesign jasdi = JRXmlLoader.load(billPath);
            updateProgress(50,100);
            String sql = this.sql;
            JRDesignQuery newQuery = new JRDesignQuery();
            newQuery.setText(sql);
            jasdi.setQuery(newQuery);
            JasperReport js = JasperCompileManager.compileReport(jasdi);
            updateProgress(75,100);
            JasperPrint jp = JasperFillManager.fillReport(js, hm, DBConnection.getInstance().getConnection());
            // JasperExportManager.exportReportToHtmlFile(jp ,ore);
            //= "G:\\IncomeReports\\" + LocalDate.now().getYear() + LocalDate.now().getMonth().toString() + LocalDate.now().getDayOfMonth() + LocalTime.now().getHour() + LocalTime.now().getMinute() + LocalTime.now().getSecond() + ".pdf";
            JasperExportManager.exportReportToPdfFile(jp, savePath);
            JasperViewer viewer = new JasperViewer(jp, false);
            viewer.show();
            updateProgress(100,100);
            return new File(savePath);
        } catch (Exception e) {
            updateMessage("Error");
            //e.printStackTrace();
            //System.out.println("Error");
            //JOptionPane.showMessageDialog(rootPane, e);
        }
        return null;
    }
}
