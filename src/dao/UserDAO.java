package dao;

import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // Autentifikacija
    public boolean authenticate(String username, String password, String userType) {
        String query = "SELECT * FROM users WHERE ime = ? AND password = ? AND role = ?";
        try (Connection conn = MySQLConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, userType);
            ResultSet rs = stmt.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            System.out.println("❌ Authentication error: " + e.getMessage());
            return false;
        }
    }

    public List<Object[]> searchUsersByNameOrRole(String query) {
        String sql = "SELECT id, ime, role FROM users WHERE ime LIKE ? OR role LIKE ?";
        List<Object[]> results = new ArrayList<>();

        try (Connection conn = MySQLConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + query + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(new Object[]{rs.getInt("id"), rs.getString("ime"), rs.getString("role")});
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Error searching users by name or role: " + e.getMessage());
        }

        return results;
    }


    public boolean changePassword(String username, String currentPassword, String newPassword) {
        String verifyQuery = "SELECT * FROM users WHERE ime = ? AND password = ?";
        String updateQuery = "UPDATE users SET password = ? WHERE ime = ?";

        try (Connection conn = MySQLConnection.connect();
             PreparedStatement verifyStmt = conn.prepareStatement(verifyQuery)) {

            verifyStmt.setString(1, username);
            verifyStmt.setString(2, currentPassword);

            ResultSet rs = verifyStmt.executeQuery();

            if (rs.next()) {

                try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                    updateStmt.setString(1, newPassword);
                    updateStmt.setString(2, username);

                    int rowsUpdated = updateStmt.executeUpdate();
                    return rowsUpdated > 0;
                }
            } else {
                System.out.println("❌ Incorrect current password.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("❌ Error changing password: " + e.getMessage());
            return false;
        }
    }

    public String getUserNameById(int userId) {
        String query = "SELECT ime FROM users WHERE ID = ?";
        try (Connection conn = MySQLConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("ime");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching user name: " + e.getMessage());
        }
        return null;
    }

    // Super Admin password change by user ID
    public boolean changePasswordById(int userId, String newPassword) {
        String updateQuery = "UPDATE users SET password = ? WHERE ID = ?";
        try (Connection conn = MySQLConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            stmt.setString(1, newPassword);
            stmt.setInt(2, userId);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error changing password: " + e.getMessage());
            return false;
        }
    }

    // Dodavanje korisnika
    public boolean addUser(String ime, String password, String role) {
        String query = "INSERT INTO users (ime, password, role) VALUES (?, ?, ?)";
        try (Connection conn = MySQLConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, ime);
            stmt.setString(2, password);
            stmt.setString(3, role);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0; // Ovdje vraća true ako je korisnik uspješno dodan
        } catch (SQLException e) {
            System.out.println("❌ Error adding user: " + e.getMessage());
            return false;
        }
    }

    // Izmjena uloge korisnika prema ID
    public boolean updateUserRole(int userId, String newRole) {
        String query = "UPDATE users SET role = ? WHERE id = ?";
        try (Connection conn = MySQLConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newRole);
            stmt.setInt(2, userId);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0; // Vraća true ako je uloga uspješno izmijenjena
        } catch (SQLException e) {
            System.out.println("❌ Error updating role: " + e.getMessage());
            return false;
        }
    }

    // Izmjena imena korisnika prema ID
    public boolean updateUserIme(int userId, String newIme) {
        String query = "UPDATE users SET ime = ? WHERE id = ?";
        try (Connection conn = MySQLConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newIme);
            stmt.setInt(2, userId);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error updating name: " + e.getMessage());
            return false;
        }
    }

    // Brisanje korisnika prema ID
    public boolean deleteUser(int userId) {
        String query = "DELETE FROM users WHERE id = ?";
        try (Connection conn = MySQLConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);

            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error deleting user: " + e.getMessage());
            return false;
        }
    }

    public List<Object[]> getAllUsersAsList() {
        String query = "SELECT id, ime, role FROM users";
        List<Object[]> results = new ArrayList<>();

        try (Connection conn = MySQLConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                results.add(new Object[]{rs.getInt("id"), rs.getString("ime"), rs.getString("role")});
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching all users: " + e.getMessage());
        }

        return results;
    }

    public User getUserById(int userId) {
        String query = "SELECT id, ime, role, password FROM users WHERE id = ?";
        try (Connection conn = MySQLConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("✅ User data found for ID: " + userId);
                    return new User(
                            rs.getInt("id"),
                            rs.getString("ime"),
                            rs.getString("role"),
                            rs.getString("password")
                    );
                } else {
                    System.out.println("⚠️ No data found for ID: " + userId);
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching user: " + e.getMessage());
        }
        return null;
    }

    public String[] getEmployeeDetailsByUsername(String username) {
        String query = """
        SELECT 
            u.ime AS Username,
            u.role AS Role,
            e.First_Name AS FirstName,
            e.Last_Name AS LastName,
            e.Email AS Email,
            e.Department AS Department,
            e.Job_Title AS JobTitle,
            e.Basic_Salary AS BasicSalary,
            e.Date_Hired AS DateHired
        FROM 
            users u
        JOIN 
            user_employee_map m ON u.id = m.User_ID
        JOIN 
            employee e ON m.Employee_ID = e.ID
        WHERE 
            u.ime = ?;
    """;

        try (Connection conn = MySQLConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new String[]{
                        rs.getString("Username"),
                        rs.getString("Role"),
                        rs.getString("FirstName") + " " + rs.getString("LastName"), // Full Name
                        rs.getString("Email"),
                        rs.getString("Department"),
                        rs.getString("JobTitle"),
                        String.format("%.2f", rs.getDouble("BasicSalary")),
                        rs.getString("DateHired")
                };
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching employee details by username: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public boolean addUserAndMapToEmployee(String ime, String password, String role) {
        String insertUserQuery = "INSERT INTO users (ime, password, role, Employee_Number) VALUES (?, ?, ?, 0)";
        String updateUserQuery = "UPDATE users SET Employee_Number = ? WHERE id = ?";
        String insertMappingQuery = "INSERT INTO user_employee_map (User_ID, Employee_ID) VALUES (?, ?)";

        try (Connection conn = MySQLConnection.connect();
             PreparedStatement userStmt = conn.prepareStatement(insertUserQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement updateStmt = conn.prepareStatement(updateUserQuery);
             PreparedStatement mappingStmt = conn.prepareStatement(insertMappingQuery)) {

            // Dodavanje korisnika u tabelu users
            userStmt.setString(1, ime);
            userStmt.setString(2, password);
            userStmt.setString(3, role);
            userStmt.executeUpdate();

            // Dohvatanje generisanog ID
            ResultSet generatedKeys = userStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int userId = generatedKeys.getInt(1);

                // Ažuriranje polja Employee_Number
                updateStmt.setInt(1, userId);
                updateStmt.setInt(2, userId);
                updateStmt.executeUpdate();

                // Povezivanje u tabeli user_employee_map
                mappingStmt.setInt(1, userId);
                mappingStmt.setInt(2, userId);
                mappingStmt.executeUpdate();

                return true;
            }
        } catch (SQLException e) {
            System.out.println("❌ Error adding user and mapping: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
}
