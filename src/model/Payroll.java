package model;

public class Payroll {
    private int id;
    private int employeeId;
    private String datumPlate;
    private double kolicina;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
    public String getPaymentDate() { return datumPlate; }
    public void setPaymentDate(String paymentDate) { this.datumPlate = paymentDate; }
    public double getAmount() { return kolicina; }
    public void setAmount(double amount) { this.kolicina = amount; }
}
