package dao;

import model.Payroll;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PayrollDAO {
    public void addPayroll(Payroll payroll) {
        String query = "INSERT INTO Payroll (Employee_ID, Salary, Bonus, Deductions, Tax, Pay_Date) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = MySQLConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, payroll.getEmployeeId());
            stmt.setDouble(2, payroll.getSalary());
            stmt.setDouble(3, payroll.getBonus());
            stmt.setDouble(4, payroll.getDeductions());
            stmt.setDouble(5, payroll.getTax());
            stmt.setString(6, payroll.getPayDate());

            stmt.executeUpdate();
            System.out.println("✅ Payroll record added successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error adding payroll record: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Example of updating the payroll for an employee (if salary, bonus, or other values need to be changed)
    public void updatePayroll(Payroll payroll) {
        String query = "UPDATE Payroll SET Salary = ?, Bonus = ?, Deductions = ?, Tax = ?, Pay_Date = ? WHERE Employee_ID = ?";
        try (Connection conn = MySQLConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDouble(1, payroll.getSalary());
            stmt.setDouble(2, payroll.getBonus());
            stmt.setDouble(3, payroll.getDeductions());
            stmt.setDouble(4, payroll.getTax());
            stmt.setString(5, payroll.getPayDate());
            stmt.setInt(6, payroll.getEmployeeId());

            stmt.executeUpdate();
            System.out.println("✅ Payroll record updated successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error updating payroll record: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
