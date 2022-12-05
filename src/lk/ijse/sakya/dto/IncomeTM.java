package lk.ijse.sakya.dto;

public class IncomeTM {
    private String className;
    private double income;

    public IncomeTM(String className, double income) {
        this.setClassName(className);
        this.setIncome(income);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }
}
