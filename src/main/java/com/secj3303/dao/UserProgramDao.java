package com.secj3303.dao;

import java.util.List;

import com.secj3303.model.UserProgram;

public interface UserProgramDao {
    
    // Saves a new enrollment record
    void save(UserProgram userProgram);
    
    // Finds all programs a specific user has enrolled in
    List<UserProgram> findByUserId(int userId);
    
    // Removes an enrollment by its ID (used for Unenroll)
    void deleteById(int id);
    
    // Checks if a user is already enrolled in a specific program (to prevent duplicates)
    boolean isEnrolled(int userId, int programId);
}