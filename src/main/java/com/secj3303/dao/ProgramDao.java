package com.secj3303.dao;

import java.util.List;

import com.secj3303.model.Program;

public interface ProgramDao {
    
    List<Program> findAll();
    
    Program findById(int id);
    
    
    Program findProgramByName(String name);
    
    void save(Program program);
    
    
    void delete(int id);
}