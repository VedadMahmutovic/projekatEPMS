package com.vedadmahmutovic.dao;

import com.vedadmahmutovic.model.Payroll;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PayrollDAO {

    public boolean generatePayroll(String employeeIds, String payDate, Double bonus, Double deduction) {
        String[] idArray = employeeIds.split(",");
        boolean allSuccess = true;

        String insertQuery = """
        INSERT INTO Payroll (Employee_ID, Pay_Date, Salary, Bonus, Deductions, Tax)
        SELECT e.ID, ?, e.Basic_Salary, ?, ?, e.Basic_Salary * 0.15
        FROM Employee e WHERE e.ID = ?
    """;

        try (Connection conn = MySQLConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(insertQuery)) {

            for (String id : idArray) {
                try {
                    int employeeId = Integer.parseInt(id.trim());
                    stmt.setString(1, payDate);
                    stmt.setDouble(2, bonus != null ? bonus : 0.0);
                    stmt.setDouble(3, deduction != null ? deduction : 0.0);
                    stmt.setInt(4, employeeId);

                    int rowsInserted = stmt.executeUpdate();
                    if (rowsInserted <= 0) {
                        System.out.println("❌ Failed to generate payroll for Employee ID: " + employeeId);
                        allSuccess = false;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("❌ Invalid Employee ID format: " + id);
                    allSuccess = false;
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Error generating payroll: " + e.getMessage());
            allSuccess = false;
        }

        return allSuccess;
    }

    public List<Payroll> getPaymentHistory(int employeeId) {
        List<Payroll> payrolls = new ArrayList<>();
        String query = """
            SELECT p.Payroll_ID, p.employee_ID, p.Salary, p.Bonus, p.Deductions, p.Tax, p.Pay_Date,
                   e.First_name, e.Last_name
            FROM Payroll p
            JOIN employee e ON p.employee_ID = e.ID
            WHERE p.employee_ID = ?
            """;

        try (Connection conn = MySQLConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Payroll payroll = new Payroll();
                payroll.setId(rs.getInt("Payroll_ID"));
                payroll.setEmployeeId(rs.getInt("employee_ID"));
                payroll.setSalary(rs.getDouble("Salary"));
                payroll.setBonus(rs.getDouble("Bonus"));
                payroll.setDeductions(rs.getDouble("Deductions"));
                payroll.setTax(rs.getDouble("Tax"));
                payroll.setPayDate(rs.getString("Pay_Date"));

                // Fetch and print employee name
                String name = rs.getString("First_name");
                String surname = rs.getString("Last_name");
                System.out.println("Employee: " + name + " " + surname);

                payrolls.add(payroll);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error fetching payment history: " + e.getMessage());
            e.printStackTrace();
        }

        return payrolls;
    }


    public List<Payroll> getPaymentWithEmployeeId(String startDate, String endDate, Integer employeeId) {
        List<Payroll> payrolls = new ArrayList<>();
        String query;

        if (employeeId != null) {
            query = """
                    SELECT p.Payroll_ID, p.employee_ID, p.Salary, p.Bonus, p.Deductions, p.Tax, p.Pay_Date,
                           e.First_name, e.Last_name
                    FROM Payroll p
                    JOIN employee e ON p.employee_ID = e.ID
                    WHERE p.Pay_Date BETWEEN ? AND ? AND p.employee_ID = ?
                    """;
        } else {
            query = """
                    SELECT p.Payroll_ID, p.employee_ID, p.Salary, p.Bonus, p.Deductions, p.Tax, p.Pay_Date,
                           e.First_name, e.Last_name
                    FROM Payroll p
                    JOIN employee e ON p.employee_ID = e.ID
                    WHERE p.Pay_Date BETWEEN ? AND ?
                    """;
        }

        try (Connection conn = MySQLConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, startDate);
            stmt.setString(2, endDate);

            if (employeeId != null) {
                stmt.setInt(3, employeeId);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Payroll payroll = new Payroll();
                payroll.setId(rs.getInt("Payroll_ID"));
                payroll.setEmployeeId(rs.getInt("employee_ID"));
                payroll.setSalary(rs.getDouble("Salary"));
                payroll.setBonus(rs.getDouble("Bonus"));
                payroll.setDeductions(rs.getDouble("Deductions"));
                payroll.setTax(rs.getDouble("Tax"));
                payroll.setPayDate(rs.getString("Pay_Date"));

                String name = rs.getString("First_name");
                String surname = rs.getString("Last_name");
                System.out.println("Employee: " + name + " " + surname);

                payrolls.add(payroll);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error fetching payments: " + e.getMessage());
            e.printStackTrace();
        }

        return payrolls;
    }

    public List<String[]> generateMonthlyReport(String year, String month) {
        List<String[]> report = new ArrayList<>();
        String query = """
        SELECT 
            e.ID AS Employee_ID,
            CONCAT(e.First_Name, ' ', e.Last_Name) AS Employee_Name,
            SUM(p.Salary + p.Bonus - p.Deductions - p.Tax) AS Total_Salary,
            DATE_FORMAT(p.Pay_Date, '%Y-%m') AS Month
        FROM 
            Payroll p
        JOIN 
            employee e ON p.employee_ID = e.ID
        WHERE 
            DATE_FORMAT(p.Pay_Date, '%Y-%m') = ?
        GROUP BY 
            e.ID, Employee_Name, DATE_FORMAT(p.Pay_Date, '%Y-%m')
        ORDER BY 
            Total_Salary DESC;
        """;

        try (Connection conn = MySQLConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            String dateFilter = year + "-" + month;
            System.out.println("Executing query with parameter: " + dateFilter); // Ispis parametra
            stmt.setString(1, dateFilter);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String[] row = new String[]{
                        rs.getString("Employee_ID"),
                        rs.getString("Employee_Name"),
                        String.format("%.2f", rs.getDouble("Total_Salary")),
                        rs.getString("Month")
                };
                report.add(row);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error generating monthly report: " + e.getMessage());
            e.printStackTrace();
        }

        return report;
    }

    public String getEmployeeName(int employeeId) {
        String query = "SELECT First_name, Last_name FROM employee WHERE ID = ?";
        try (Connection conn = MySQLConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("First_name");
                String surname = rs.getString("Last_name");
                return name + " " + surname;
            } else {
                System.out.println("❌ Employee not found for ID: " + employeeId);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching employee name: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<String[]> getEmployeeReportByIds(int[] employeeIds) {
        List<String[]> report = new ArrayList<>();

        StringBuilder queryBuilder = new StringBuilder("""
        SELECT 
            e.ID AS Employee_ID,
            CONCAT(e.First_Name, ' ', e.Last_Name) AS Employee_Name,
            e.Email,
            e.Contact,
            e.Department,
            e.Job_Title,
            e.Basic_Salary,
            p.Payroll_ID,
            p.Salary,
            p.Bonus,
            p.Deductions,
            p.Tax,
            p.Pay_Date,
            (p.Salary + p.Bonus - p.Deductions - p.Tax) AS Net_Salary
        FROM 
            employee e
        LEFT JOIN 
            Payroll p ON e.ID = p.employee_ID
        WHERE 
            e.ID IN (
        """);

        // Ovdje dodajem privremenu vrijednost za ID
        for (int i = 0; i < employeeIds.length; i++) {
            queryBuilder.append("?");
            if (i < employeeIds.length - 1) queryBuilder.append(", ");
        }
        queryBuilder.append(") ORDER BY e.ID, p.Pay_Date;");

        try (Connection conn = MySQLConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(queryBuilder.toString())) {


            for (int i = 0; i < employeeIds.length; i++) {
                stmt.setInt(i + 1, employeeIds[i]);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                report.add(new String[]{
                        rs.getString("Employee_ID"),
                        rs.getString("Employee_Name"),
                        rs.getString("Email"),
                        rs.getString("Contact"),
                        rs.getString("Department"),
                        rs.getString("Job_Title"),
                        String.format("%.2f", rs.getDouble("Basic_Salary")),
                        rs.getString("Payroll_ID"),
                        String.format("%.2f", rs.getDouble("Salary")),
                        String.format("%.2f", rs.getDouble("Bonus")),
                        String.format("%.2f", rs.getDouble("Deductions")),
                        String.format("%.2f", rs.getDouble("Tax")),
                        rs.getString("Pay_Date"),
                        String.format("%.2f", rs.getDouble("Net_Salary"))
                });
            }

        } catch (SQLException e) {
            System.out.println("❌ Error fetching employee report by IDs: " + e.getMessage());
            e.printStackTrace();
        }

        return report;
    }

    public List<String[]> getFullEmployeeReport() {
        List<String[]> report = new ArrayList<>();

        String query = """
        SELECT 
            e.ID AS Employee_ID,
            CONCAT(e.First_Name, ' ', e.Last_Name) AS Employee_Name,
            e.Email,
            e.Contact,
            e.Department,
            e.Job_Title,
            e.Basic_Salary,
            p.Payroll_ID,
            p.Salary,
            p.Bonus,
            p.Deductions,
            p.Tax,
            p.Pay_Date,
            (p.Salary + p.Bonus - p.Deductions - p.Tax) AS Net_Salary
        FROM 
            employee e
        LEFT JOIN 
            Payroll p ON e.ID = p.employee_ID
        ORDER BY e.ID, p.Pay_Date;
        """;

        try (Connection conn = MySQLConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                report.add(new String[]{
                        rs.getString("Employee_ID"),
                        rs.getString("Employee_Name"),
                        rs.getString("Email"),
                        rs.getString("Contact"),
                        rs.getString("Department"),
                        rs.getString("Job_Title"),
                        String.format("%.2f", rs.getDouble("Basic_Salary")),
                        rs.getString("Payroll_ID"),
                        String.format("%.2f", rs.getDouble("Salary")),
                        String.format("%.2f", rs.getDouble("Bonus")),
                        String.format("%.2f", rs.getDouble("Deductions")),
                        String.format("%.2f", rs.getDouble("Tax")),
                        rs.getString("Pay_Date"),
                        String.format("%.2f", rs.getDouble("Net_Salary"))
                });
            }

        } catch (SQLException e) {
            System.out.println("❌ Error fetching full employee report: " + e.getMessage());
            e.printStackTrace();
        }

        return report;
    }

    public boolean adjustSalary(int employeeId, double newSalary) {
        String query = "UPDATE employee SET Basic_Salary = ? WHERE ID = ?";
        try (Connection conn = MySQLConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (conn == null) {
                System.out.println("❌ Failed to establish a database connection.");
                return false;
            }

            // Log parameters
            System.out.println("Executing: " + query);
            System.out.println("Parameters: Employee ID = " + employeeId + ", New Salary = " + newSalary);

            stmt.setDouble(1, newSalary);
            stmt.setInt(2, employeeId);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Salary updated successfully for Employee ID: " + employeeId);
                return true;
            } else {
                System.out.println("❌ No rows updated. Employee ID might not exist: " + employeeId);
                return false;
            }

        } catch (SQLException e) {
            System.out.println("❌ Error adjusting salary: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public List<String[]> getPayrollForLoggedInUser(String username) {
        List<String[]> payrollDetails = new ArrayList<>();

        String query = """
        SELECT 
            p.Payroll_ID,
            p.Salary,
            p.Deductions,
            p.Tax,
            p.Bonus,
            p.Pay_Date
        FROM 
            users u
        JOIN 
            user_employee_map m ON u.id = m.User_ID
        JOIN 
            payroll p ON m.Employee_ID = p.employee_ID
        WHERE 
            u.ime = ?;
    """;

        try (Connection conn = MySQLConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Postavljanje parametra za username
            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    payrollDetails.add(new String[]{
                            rs.getString("Payroll_ID"),
                            String.format("%.2f", rs.getDouble("Salary")),
                            String.format("%.2f", rs.getDouble("Deductions")),
                            String.format("%.2f", rs.getDouble("Tax")),
                            String.format("%.2f", rs.getDouble("Bonus")),
                            rs.getString("Pay_Date")
                    });
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ SQL Error: " + e.getMessage());
            e.printStackTrace();
        }

        return payrollDetails;
    }

    public List<String[]> getMonthlyReportForLoggedInUser(String username) {
        List<String[]> monthlyReport = new ArrayList<>();

        String query = """
        SELECT 
            DATE_FORMAT(p.Pay_Date, '%Y-%m') AS Month,
            SUM(p.Salary) AS Total_Salary,
            SUM(p.Bonus) AS Total_Bonus,
            SUM(p.Deductions) AS Total_Deductions,
            SUM(p.Tax) AS Total_Tax,
            SUM(p.Salary + p.Bonus - p.Deductions - p.Tax) AS Net_Income
        FROM 
            payroll p
        JOIN 
            user_employee_map m ON p.employee_ID = m.Employee_ID
        JOIN 
            users u ON m.User_ID = u.id
        WHERE 
            u.ime = ?
        GROUP BY 
            DATE_FORMAT(p.Pay_Date, '%Y-%m')
        ORDER BY 
            Month;
    """;

        try (Connection conn = MySQLConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Postavljanje parametra za username
            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    monthlyReport.add(new String[]{
                            rs.getString("Month"),
                            String.format("%.2f", rs.getDouble("Total_Salary")),
                            String.format("%.2f", rs.getDouble("Total_Bonus")),
                            String.format("%.2f", rs.getDouble("Total_Deductions")),
                            String.format("%.2f", rs.getDouble("Total_Tax")),
                            String.format("%.2f", rs.getDouble("Net_Income"))
                    });
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error fetching monthly report: " + e.getMessage());
            e.printStackTrace();
        }

        return monthlyReport;
    }

    public boolean addBonusAndDeductions(int employeeId, double bonus, double deduction) {
        String query = "UPDATE Payroll SET Bonus = Bonus + ?, Deductions = Deductions + ? WHERE Employee_ID = ?";
        try (Connection conn = MySQLConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDouble(1, bonus);
            stmt.setDouble(2, deduction);
            stmt.setInt(3, employeeId);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0; // Return true if the update is successful
        } catch (SQLException e) {
            System.out.println("❌ Error updating bonus and deductions: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

}

