package com.secj3303.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.secj3303.model.UserProgram;

@Repository
public class UserProgramDaoHibernate implements UserProgramDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(UserProgram userProgram) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        
        try {
            tx = session.beginTransaction();
            session.save(userProgram);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public List<UserProgram> findByUserId(int userId) {
        Session session = sessionFactory.openSession();
        try {
            String hql = "FROM UserProgram up WHERE up.user.id = :uid";
            
            List<UserProgram> list = session.createQuery(hql, UserProgram.class)
                    .setParameter("uid", userId)
                    .list();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }

    // --- NEW METHODS ---

    @Override
    public void deleteById(int id) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            
            // 1. Get the object first
            UserProgram up = session.get(UserProgram.class, id);
            
            // 2. Delete it if it exists
            if (up != null) {
                session.delete(up);
            }
            
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public boolean isEnrolled(int userId, int programId) {
        Session session = sessionFactory.openSession();
        try {
            // Count how many records match this user AND this program
            String hql = "SELECT count(up) FROM UserProgram up WHERE up.user.id = :uid AND up.program.id = :pid";
            
            Long count = session.createQuery(hql, Long.class)
                    .setParameter("uid", userId)
                    .setParameter("pid", programId)
                    .uniqueResult();
            
            return count > 0; // Returns true if 1 or more records exist
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }
}