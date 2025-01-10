package dao;

import model.Payroll;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PayrollDAO {

    // Existing methods remain intact...

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

    public void generatePayroll(int employeeId, String payDate) {
        String fetchEmployeeQuery = "SELECT Basic_Salary FROM employee WHERE ID = ?";
        String insertPayrollQuery = "INSERT INTO Payroll (employee_ID, Salary, Bonus, Deductions, Tax, Pay_Date) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = MySQLConnection.connect();
             PreparedStatement fetchStmt = conn.prepareStatement(fetchEmployeeQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertPayrollQuery)) {

            // Fetch employee details
            fetchStmt.setInt(1, employeeId);
            ResultSet rs = fetchStmt.executeQuery();

            if (rs.next()) {
                double basicSalary = rs.getDouble("Basic_Salary");

                // Calculate dynamic bonus, deductions, and tax
                double bonus = calculateBonus(basicSalary);
                double deductions = calculateDeductions(basicSalary);
                double tax = calculateTax(basicSalary);

                // Insert payroll record
                insertStmt.setInt(1, employeeId);
                insertStmt.setDouble(2, basicSalary);
                insertStmt.setDouble(3, bonus);
                insertStmt.setDouble(4, deductions);
                insertStmt.setDouble(5, tax);
                insertStmt.setString(6, payDate);
                insertStmt.executeUpdate();

                System.out.println("✅ Payroll generated successfully for Employee ID: " + employeeId);
            } else {
                System.out.println("❌ Employee not found for ID: " + employeeId);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error generating payroll: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Dynamic bonus calculation
    private double calculateBonus(double salary) {
        if (salary < 3000) {
            return salary * 0.05; // 5% bonus for lower salaries
        } else if (salary <= 7000) {
            return salary * 0.03; // 3% bonus for mid-range salaries
        } else {
            return salary * 0.01; // 1% bonus for higher salaries
        }
    }

    // Dynamic deductions calculation
    private double calculateDeductions(double salary) {
        return 200;
    }

    // Dynamic tax calculation
    private double calculateTax(double salary) {
        if (salary < 4000) {
            return salary * 0.10; // 10% tax for lower salaries
        } else if (salary <= 8000) {
            return salary * 0.15; // 15% tax for mid-range salaries
        } else {
            return salary * 0.20; // 20% tax for higher salaries
        }
    }


    public double calculateNetSalary(int payrollId) {
        String query = "SELECT Salary, Bonus, Deductions, Tax FROM Payroll WHERE Payroll_ID = ?";
        double netSalary = 0.0;

        try (Connection conn = MySQLConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, payrollId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                double salary = rs.getDouble("Salary");
                double bonus = rs.getDouble("Bonus");
                double deductions = rs.getDouble("Deductions");
                double tax = rs.getDouble("Tax");

                netSalary = salary + bonus - deductions - tax;
            }

        } catch (SQLException e) {
            System.out.println("❌ Error calculating net salary: " + e.getMessage());
            e.printStackTrace();
        }

        return netSalary;
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

                // Fetch and print employee name
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

    public List<Payroll> getPaymentsInRange(String startDate, String endDate) {
        List<Payroll> payrolls = new ArrayList<>();
        String query = "SELECT * FROM Payroll WHERE Pay_Date BETWEEN ? AND ?";

        try (Connection conn = MySQLConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, startDate);
            stmt.setString(2, endDate);
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
                payrolls.add(payroll);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error fetching payments in range: " + e.getMessage());
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




    /**
     * Fetches the name and surname of an employee based on their ID.
     *
     * @param employeeId The ID of the employee.
     * @return A string containing the name and surname of the employee, or null if not found.
     */
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

    // Fetch employee details
    public String[] getEmployeeDetails(int employeeId) {
        String query = """
        SELECT 
            e.ID AS Employee_ID,
            CONCAT(e.First_Name, ' ', e.Last_Name) AS Full_Name,
            e.Email,
            e.Contact,
            e.Department,
            e.Job_Title,
            e.Basic_Salary
        FROM 
            employee e
        WHERE 
            e.ID = ?;
        """;

        try (Connection conn = MySQLConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new String[]{
                        rs.getString("Employee_ID"),
                        rs.getString("Full_Name"),
                        rs.getString("Email"),
                        rs.getString("Contact"),
                        rs.getString("Department"),
                        rs.getString("Job_Title"),
                        String.format("%.2f", rs.getDouble("Basic_Salary"))
                };
            }

        } catch (SQLException e) {
            System.out.println("❌ Error fetching employee details: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    // Fetch payroll entries
    public List<String[]> getEmployeeReportByIds(int[] employeeIds) {
        List<String[]> report = new ArrayList<>();

        // Prepare SQL query with placeholders for multiple IDs
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

        // Add placeholders for the number of IDs
        for (int i = 0; i < employeeIds.length; i++) {
            queryBuilder.append("?");
            if (i < employeeIds.length - 1) queryBuilder.append(", ");
        }
        queryBuilder.append(") ORDER BY e.ID, p.Pay_Date;");

        try (Connection conn = MySQLConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(queryBuilder.toString())) {

            // Set the IDs as parameters
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
    public List<String[]> getPayrollDetailsForUser(String username) {
        List<String[]> payrollDetails = new ArrayList<>();

        String query = """
        SELECT 
            p.Payroll_ID, 
            p.Salary, 
            p.Bonus, 
            p.Deductions, 
            p.Tax, 
            (p.Salary + p.Bonus - p.Deductions - p.Tax) AS Net_Salary,
            p.Pay_Date
        FROM 
            Payroll p
        JOIN 
            employee e ON p.employee_ID = e.ID
        WHERE 
            e.Username = ?
        ORDER BY 
            p.Pay_Date DESC;
    """;

        try (Connection conn = MySQLConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                payrollDetails.add(new String[]{
                        rs.getString("Payroll_ID"),
                        String.format("%.2f", rs.getDouble("Salary")),
                        String.format("%.2f", rs.getDouble("Bonus")),
                        String.format("%.2f", rs.getDouble("Deductions")),
                        String.format("%.2f", rs.getDouble("Tax")),
                        String.format("%.2f", rs.getDouble("Net_Salary")),
                        rs.getString("Pay_Date")
                });
            }

        } catch (SQLException e) {
            System.out.println("❌ Error fetching payroll details for user: " + e.getMessage());
            e.printStackTrace();
        }

        return payrollDetails;
    }

    public List<String[]> getPayrollForLoggedInUser(String username) {
        List<String[]> payrollDetails = new ArrayList<>();

        // SQL query with explicit table references to avoid ambiguity
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

            // Set the username parameter
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

            // Postavi parametar za username
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
}

