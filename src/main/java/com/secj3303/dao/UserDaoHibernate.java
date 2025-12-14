package com.secj3303.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.secj3303.model.User;

@Repository
public class UserDaoHibernate implements UserDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session openSession() {
        return sessionFactory.openSession();
    }

    @Override
    public User findByEmailAndPassword(String email, String password) {
        Session session = sessionFactory.openSession();
        List<User> list = session
                .createQuery("FROM User WHERE email = :e AND password = :p", User.class)
                .setParameter("e", email)
                .setParameter("p", password)
                .list();
        session.close();
        
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public int insert(User user) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        
        try {
            tx = session.beginTransaction();
            
            if (user.getRole() == null) {
                user.setRole("MEMBER");
            }
            
            session.save(user);
            tx.commit();
            return 1;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            throw new RuntimeException("Error in insert()", e);
        } finally {
            session.close();
        }
    }

    @Override
    public boolean emailExists(String email) {
        Session session = sessionFactory.openSession();
        List<User> list = session
                .createQuery("FROM User WHERE email = :e", User.class)
                .setParameter("e", email)
                .list();
        session.close();
        
        return !list.isEmpty();
    }

    @Override
    public User findById(int id) {
        Session session = sessionFactory.openSession();
        User user = session.get(User.class, id);
        session.close();
        return user;
    }

    @Override
    public List<User> findAll() {
        Session session = sessionFactory.openSession();
        List<User> list = session
                .createQuery("FROM User", User.class)
                .list();
        session.close();
        return list;
    }

    @Override
    public void update(User user) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        
        try {
            tx = session.beginTransaction();
            session.update(user);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            throw new RuntimeException("Error in update()", e);
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(int id) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            throw new RuntimeException("Error in delete()", e);
        } finally {
            session.close();
        }
    }

    public List<User> findByRole(String role) {
        Session session = sessionFactory.openSession();
        List<User> list = session
                .createQuery("FROM User WHERE role = :r", User.class)
                .setParameter("r", role)
                .list();
        session.close();
        return list;
    }
}