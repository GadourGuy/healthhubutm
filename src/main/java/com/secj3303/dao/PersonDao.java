package com.secj3303.dao;
import java.util.List;
import com.secj3303.model.Person;


public interface PersonDao {
  List<Person> findAll();		
  Person findById(int id);		
  int insert(Person p);		
  void update(Person p);		
  void delete(int id);		
}