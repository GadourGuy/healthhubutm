package com.secj3303.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.secj3303.model.Person;

@Repository
public class PersonDaoHibernate implements PersonDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Person> findAll() {
        Session session = sessionFactory.openSession();
        List<Person> list = session.createQuery("from Person", Person.class).list();
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
    public int insert(Person p) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        int id = 0;
        try {
            tx = session.beginTransaction();
            id = (int) session.save(p);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return id;
    }

    @Override
    public void update(Person p) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(p);
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
            Person p = session.get(Person.class, id);
            if (p != null) {
                session.delete(p);
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