package com.secj3303.dao;

import com.secj3303.model.User;
import java.util.List;

public interface UserDao {
    User findByEmailAndPassword(String email, String password);
    boolean emailExists(String email);
    List<User> findAll();
    User findById(int id);
    int insert(User user);
    void update(User user);
    void delete(int id);
}