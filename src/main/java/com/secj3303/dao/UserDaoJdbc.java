package com.secj3303.dao;

import com.secj3303.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDaoJdbc implements UserDao {

    private final DataSource dataSource;
    
    @Autowired
    public UserDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    public User findByEmailAndPassword(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        User user = new User();
	    
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                int id = rs.getInt("id");
                user.setId(rs.wasNull() ? null : id);
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error in findByEmailAndPassword()", e);
        }
        return user;
    }
    
    // Registration: Insert new user
    public int insert(User user) {
        String sql = "INSERT INTO users (first_name, last_name, email, password, role) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getRole() != null ? user.getRole() : "MEMBER");

            int rows = ps.executeUpdate();
            System.out.println("insert(): inserted rows = " + rows);
            return rows;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error in insert()", e);
        }
    }
    
    // Check if email exists (for registration validation)
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
	    
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error in emailExists()", e);
        }
        return false;
    }
    
    // Find user by ID
    public User findById(int id) {		
        String sql = "SELECT * FROM users WHERE id = ?";
        User user = new User();
	    
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                user.setId(rs.getInt("id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error in findById()", e);
        }
        return user;
    }
    
    // Get all users
    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT id, first_name, last_name, email, role FROM users";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User user = new User();
                int id = rs.getInt("id");
                System.out.println("id" + id);
                user.setId(rs.wasNull() ? null : id);
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));

                list.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error in findAll()", e);
        }

        return list;
    }

    // Update user
    public void update(User user) {
        String sql = "UPDATE users SET first_name=?, last_name=?, email=?, password=?, role=? WHERE id=?";
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getRole());
            ps.setInt(6, user.getId());
            ps.executeUpdate();

        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error in update()", e);
        }
    }

    // Delete user
    public void delete(int id) {
        String sql = "DELETE FROM users WHERE id=?";
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            System.out.println("delete(): Deleted rows: " + rows);
            System.out.println("id: " + id);
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error in delete()", e);
        }
    }

} 