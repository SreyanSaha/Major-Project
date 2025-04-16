package com.help.repository;

import com.help.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    @Query("SELECT a FROM Admin a JOIN a.authData au WHERE au.username = :username")
    Admin findByUsername(String username);
}
