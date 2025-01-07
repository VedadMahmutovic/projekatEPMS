package dao;

import model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    public void addEmployee(Employee employee) {
        String query = "INSERT INTO Employee (First_Name, Last_Name, Date_of_Birth, Email, Contact, Address, Apt_House_No, Post_Code, Department, Date_Hired, Basic_Salary, Job_Title, Status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = MySQLConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, employee.getFirstName());
            stmt.setString(2, employee.getLastName());
            stmt.setDate(3, java.sql.Date.valueOf(employee.getDateOfBirth()));
            stmt.setString(4, employee.getEmail());
            stmt.setString(5, employee.getContact());
            stmt.setString(6, employee.getAddress());
            stmt.setString(7, employee.getAptHouseNo());
            stmt.setString(8, employee.getPostCode());
            stmt.setString(9, employee.getDepartment());
            stmt.setDate(10, java.sql.Date.valueOf(employee.getDateHired()));
            stmt.setDouble(11, employee.getBasicSalary());
            stmt.setString(12, employee.getJobTitle());
            stmt.setString(13, employee.getStatus());

            stmt.executeUpdate();
            System.out.println("✅ Employee added successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error adding employee: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM Employee";

        try (Connection conn = MySQLConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Employee emp = new Employee();
                emp.setId(rs.getInt("ID"));
                emp.setFirstName(rs.getString("First_Name"));
                emp.setLastName(rs.getString("Last_Name"));
                emp.setDateOfBirth(rs.getDate("Date_of_Birth").toLocalDate());
                emp.setEmail(rs.getString("Email"));
                emp.setContact(rs.getString("Contact"));
                emp.setAddress(rs.getString("Address"));
                emp.setAptHouseNo(rs.getString("Apt_House_No"));
                emp.setPostCode(rs.getString("Post_Code"));
                emp.setDepartment(rs.getString("Department"));
                emp.setDateHired(rs.getDate("Date_Hired").toLocalDate());
                emp.setBasicSalary(rs.getDouble("Basic_Salary"));
                emp.setJobTitle(rs.getString("Job_Title"));
                emp.setStatus(rs.getString("Status"));

                employees.add(emp);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error retrieving employees: " + e.getMessage());
            e.printStackTrace();
        }

        return employees;
    }
}
