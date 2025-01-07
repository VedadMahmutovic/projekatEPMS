package dao;

import model.Payroll;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PayrollDAO {
    public void addPayroll(Payroll payroll) {
        String query = "INSERT INTO payroll (employeeId, datumPlate, kolicina) VALUES (?, ?, ?)";
        try (Connection conn = MySQLConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, payroll.getEmployeeId());
            stmt.setString(2, payroll.getPaymentDate());
            stmt.setDouble(3, payroll.getAmount());
            stmt.executeUpdate();
            System.out.println("✅ Payroll record added successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error adding payroll record: " + e.getMessage());
        }
    }
}
