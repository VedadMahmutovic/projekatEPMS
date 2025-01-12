package com.vedadmahmutovic.dao;

import com.vedadmahmutovic.model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    // Dodavanje novih zaposlenika u bazu

    public boolean addEmployee(Employee employee) {
        String insertQuery = "INSERT INTO Employee (First_Name, Last_Name, Date_of_Birth, Email, Contact, Address, Apt_House_No, Post_Code, Department, Date_Hired, Basic_Salary, Job_Title, Status, Employee_Number) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = MySQLConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {

            // Popunjavanje podataka
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
            stmt.setInt(14, 0); // Placeholder za Employee_Number, postavlja se kasnije

            // Izvršavanje unosa i dohvatanje ID
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);

                    // Ažuriranje Employee_Number na ID
                    String updateQuery = "UPDATE Employee SET Employee_Number = ? WHERE ID = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                        updateStmt.setInt(1, id);
                        updateStmt.setInt(2, id);
                        updateStmt.executeUpdate();
                    }
                    System.out.println("✅ Employee added successfully with Employee_Number: " + id);
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Error adding employee: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }


    // Get all employees from the database
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

    // Pretraga zaposlenika
    public List<Employee> searchEmployees(String searchText) {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM Employee WHERE First_Name LIKE ? OR Last_Name LIKE ? OR Email LIKE ? " +
                "OR Department LIKE ? OR Job_Title LIKE ? OR Status LIKE ?";

        try (Connection conn = MySQLConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Prepare the search query with wildcards for partial matching
            String searchPattern = "%" + searchText + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            stmt.setString(4, searchPattern);
            stmt.setString(5, searchPattern);
            stmt.setString(6, searchPattern);

            try (ResultSet rs = stmt.executeQuery()) {
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
            }

        } catch (SQLException e) {
            System.out.println("❌ Error searching employees: " + e.getMessage());
            e.printStackTrace();
        }

        return employees;
    }

    // Pretraga po ID
    public Employee getEmployeeById(int id) {
        String query = "SELECT * FROM Employee WHERE ID = ?";
        Employee employee = null;

        try (Connection conn = MySQLConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    employee = new Employee();
                    employee.setId(rs.getInt("ID"));
                    employee.setFirstName(rs.getString("First_Name"));
                    employee.setLastName(rs.getString("Last_Name"));
                    employee.setDateOfBirth(rs.getDate("Date_of_Birth").toLocalDate());
                    employee.setEmail(rs.getString("Email"));
                    employee.setContact(rs.getString("Contact"));
                    employee.setAddress(rs.getString("Address"));
                    employee.setAptHouseNo(rs.getString("Apt_House_No"));
                    employee.setPostCode(rs.getString("Post_Code"));
                    employee.setDepartment(rs.getString("Department"));
                    employee.setDateHired(rs.getDate("Date_Hired").toLocalDate());
                    employee.setBasicSalary(rs.getDouble("Basic_Salary"));
                    employee.setJobTitle(rs.getString("Job_Title"));
                    employee.setStatus(rs.getString("Status"));
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Error retrieving employee: " + e.getMessage());
            e.printStackTrace();
        }

        return employee;
    }

    // Update an existing employee in the database
    public void updateEmployee(Employee employee) {
        String query = "UPDATE Employee SET First_Name = ?, Last_Name = ?, Date_of_Birth = ?, Email = ?, Contact = ?, " +
                "Address = ?, Apt_House_No = ?, Post_Code = ?, Department = ?, Date_Hired = ?, Basic_Salary = ?, " +
                "Job_Title = ?, Status = ? WHERE ID = ?";
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
            stmt.setInt(14, employee.getId());

            stmt.executeUpdate();
            System.out.println("✅ Employee updated successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error updating employee: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Delete an employee from the database
    public void deleteEmployee(int id) {
        String query = "DELETE FROM Employee WHERE ID = ?";
        try (Connection conn = MySQLConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("✅ Employee deleted successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error deleting employee: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
