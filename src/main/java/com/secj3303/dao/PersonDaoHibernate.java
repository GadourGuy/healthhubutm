package com.secj3303.dao;

import com.secj3303.model.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class PersonDaoHibernate implements PersonDao {

    @Autowired
    private SessionFactory sessionFactory;

    // For the time being/beginning: we explicitly open/close session
    private Session openSession() {
        return sessionFactory.openSession();
    }

    @Override
    public List<Person> findAll() {
        Session session = sessionFactory.openSession();
        List<Person> list = session
                .createQuery("FROM Person", Person.class)
                .list();
        session.close();
        return list;
    }

    @Override
    public Person findById(int id) {
        Session session = sessionFactory.openSession();
        Person person = session.get(Person.class, id);
        session.close();
        return person;
    }

    @Override
    public int insert(Person person) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        
        try {
            tx = session.beginTransaction();
            session.save(person);
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
    public void update(Person person) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        
        try {
            tx = session.beginTransaction();
            
            // Ensure BMI is calculated before updating
            person.calculateBmi();

            session.update(person);
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
            Person person = session.get(Person.class, id);
            if (person != null) {
                session.delete(person);
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
}