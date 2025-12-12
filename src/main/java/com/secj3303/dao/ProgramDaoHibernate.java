package com.secj3303.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.secj3303.model.Program;

@Repository
public class ProgramDaoHibernate implements ProgramDao {

    @Autowired
    private SessionFactory sessionFactory;

    // For the time being/beginning: we explicitly open/close session
    private Session openSession() {
        return sessionFactory.openSession();
    }

    @Override
    public List<Program> findAll() {
        Session session = sessionFactory.openSession();
        List<Program> list = session
                .createQuery("FROM Program", Program.class) // HQL
                .list();
        session.close();
        return list;
    }

    @Override
    public Program findById(Integer id) {
        Session session = sessionFactory.openSession();
        // JDBC equivalent: SELECT * FROM program WHERE id = ?
        Program program = session.get(Program.class, id);
        
        session.close();
        return program;
    }

    @Override
    public void save(Program program) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        
        try {
            tx = session.beginTransaction();
            
            // JDBC equivalent: INSERT INTO program ... (or UPDATE if it exists)
            session.saveOrUpdate(program); 
            
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(Integer id) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            
            // To delete in Hibernate, we usually need the object first
            Program program = session.get(Program.class, id);
            
            if (program != null) {
                // JDBC equivalent: DELETE FROM program WHERE id = ?
                session.delete(program);
            }
            
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}