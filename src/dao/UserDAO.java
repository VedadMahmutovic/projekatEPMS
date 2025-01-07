package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public boolean authenticate(String username, String password, String userType) {
        String query = "SELECT * FROM users WHERE ime = ? AND password = ? AND role = ?";
        try (Connection conn = MySQLConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, userType);
            ResultSet rs = stmt.executeQuery();

            return rs.next(); // Ako postoji rezultat, autentifikacija je uspješna
        } catch (SQLException e) {
            System.out.println("❌ Authentication error: " + e.getMessage());
            return false;
        }
    }
}
