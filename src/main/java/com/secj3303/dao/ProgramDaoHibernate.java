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

    @Override
    public List<Program> findAll() {
        Session session = sessionFactory.openSession();
        List<Program> list = session.createQuery("from Program", Program.class).list();
        session.close();
        return list;
    }

    @Override
    public Program findById(int id) {
        Session session = sessionFactory.openSession();
        Program program = session.get(Program.class, id);
        session.close();
        return program;
    }

    @Override
    public Program findProgramByName(String name) {
        Session session = sessionFactory.openSession();
        Program program = session.createQuery("FROM Program WHERE name = :name", Program.class)
                .setParameter("name", name)
                .uniqueResult();
        session.close();
        return program;
    }

    @Override
    public void save(Program program) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
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
    public void delete(int id) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Program program = session.get(Program.class, id);
            
            if (program != null) {
                session.createQuery("DELETE FROM UserProgram up WHERE up.program.id = :pid")
                       .setParameter("pid", id)
                       .executeUpdate();
                
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