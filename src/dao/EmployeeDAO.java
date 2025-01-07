package dao;

import model.Employee;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EmployeeDAO {
    public void addEmployee(Employee employee) {
        String query = "INSERT INTO employees (ime, prezime, pozicija, plata, radiSati) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = MySQLConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, employee.getName());
            stmt.setString(2, employee.getSurname());
            stmt.setString(3, employee.getPosition());
            stmt.setDouble(4, employee.getSalary());
            stmt.setInt(5, employee.getHoursWorked());
            stmt.executeUpdate();
            System.out.println("✅ Employee added successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error adding employee: " + e.getMessage());
        }
    }
}
