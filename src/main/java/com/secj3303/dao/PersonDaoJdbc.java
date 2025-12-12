package com.secj3303.dao;

import com.secj3303.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;



@Repository
public class PersonDaoJdbc implements PersonDao {

    private final DataSource dataSource;
    
    @Autowired
    public PersonDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
     public List<Person> findAll() {
        List<Person> list = new ArrayList<>();

        String sql = "SELECT id, name, yob,weight, height, bmi, category FROM person";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Person p = new Person();
                int id = rs.getInt("id");
                System.out.println("id" + id);
                p.setId(rs.wasNull() ? null : id);
                p.setName(rs.getString("name"));
                int yob = rs.getInt("yob");
                p.setYob(rs.wasNull() ? null : yob);
                double weight = rs.getDouble("weight");
                p.setWeight(rs.wasNull() ? null : weight);
                double height = rs.getDouble("height");
                p.setHeight(rs.wasNull() ? null : height);
                double bmi = rs.getDouble("bmi");
                p.setBMI(rs.wasNull() ? null : bmi);
                String category = rs.getString("category");
                p.setCategory(rs.wasNull() ? null : category);

                list.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error in getAll()", e);
        }

        return list;
    }

   @Override
    public int insert(Person person) {
        String sql = "INSERT INTO person (name, yob, age, weight, height, bmi, category) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, person.getName());
            ps.setInt(2, person.getYob());
            ps.setInt(3, person.getAge());
            ps.setDouble(4, person.getWeight());
            ps.setDouble(5, person.getHeight());
            ps.setDouble(6, person.getBmi());
            ps.setString(7, person.getCategory());

            int rows = ps.executeUpdate();
            System.out.println("add(): inserted rows = " + rows);
            return rows;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error in add()", e);
        }
    }


    @Override
    public Person findById(int id) {		
        //findById
        String sql = "SELECT * FROM person WHERE id = ?";
        Person p = new Person();
	    
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                p.setId(rs.getInt("id"));
                p.setHeight(rs.getDouble("height"));
                p.setYob(rs.getInt("yob"));
                p.setWeight(rs.getDouble("weight"));
                p.setName(rs.getString("name"));
                p.setBMI(rs.getDouble("bmi"));
                p.setCategory(rs.getString("category"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error in add()", e);

        }
        return p;
    }

    

    @Override
    public void update(Person p) {
        String sql = "UPDATE person SET name=?, yob=?, age=?, weight=?, height=?, bmi=?, category=? WHERE id=?";
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            p.setBMI(p.getBmi());
            p.categorize();
            
            ps.setString(1, p.getName());
            ps.setInt(2, p.getYob());
            ps.setInt(3, p.getAge());
            ps.setDouble(4, p.getWeight());
            ps.setDouble(5, p.getHeight());
            ps.setDouble(6, p.getBmi());
            ps.setString(7, p.getCategory());
            ps.setInt(8, p.getId());
            ps.executeUpdate();

        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error in update()", e);

        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM person WHERE id=?";
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

} //end class

