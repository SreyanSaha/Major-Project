package com.help.repository;

import com.help.dto.AdminProfile;
import com.help.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    @Query("SELECT a FROM Admin a JOIN a.authData au WHERE au.username = :username")
    Optional<Admin> findByUsername(@Param("username") String username);

    @Query("SELECT new com.help.dto.AdminProfile(" +
            "a.adminId, a.adminRole, a.adminStatus, a.signupDateTime, a.timeOutEndTime, " +
            "a.adminFirstName, a.adminLastName, a.adminEmailId, a.adminPhoneNumber, a.profileImagePath, " +
            "a.latitude, a.longitude, a.street, a.city, a.state, a.country, a.zipCode, " +
            "a.adminDepartment, a.adminEmployeeId, a.adminCompanyName) " +
            "FROM Admin a WHERE a.adminId!= :adminId")
    List<AdminProfile> fetchAllAdmins(@Param("adminId") int adminId);

    @Query("SELECT new com.help.dto.AdminProfile(" +
            "a.adminId, a.adminRole, a.adminStatus, a.signupDateTime, a.timeOutEndTime, " +
            "a.adminFirstName, a.adminLastName, a.adminEmailId, a.adminPhoneNumber, a.profileImagePath, " +
            "a.latitude, a.longitude, a.street, a.city, a.state, a.country, a.zipCode, " +
            "a.adminDepartment, a.adminEmployeeId, a.adminCompanyName) " +
            "FROM Admin a WHERE a.adminId= :adminId")
    AdminProfile fetchAdminById(@Param("adminId") int adminId);


    @Query("SELECT new com.help.dto.AdminProfile(" +
            "a.adminId, a.adminRole, a.adminStatus, a.signupDateTime, a.timeOutEndTime, " +
            "a.adminFirstName, a.adminLastName, a.adminEmailId, a.adminPhoneNumber, a.profileImagePath, " +
            "a.latitude, a.longitude, a.street, a.city, a.state, a.country, a.zipCode, " +
            "a.adminDepartment, a.adminEmployeeId, a.adminCompanyName) " +
            "FROM Admin a " +
            "WHERE LOWER(a.adminFirstName) LIKE LOWER(CONCAT('%', :searchString, '%')) " +
            "   OR LOWER(a.adminLastName) LIKE LOWER(CONCAT('%', :searchString, '%')) " +
            "   OR LOWER(a.adminEmailId) LIKE LOWER(CONCAT('%', :searchString, '%')) " +
            "   OR LOWER(a.adminCompanyName) LIKE LOWER(CONCAT('%', :searchString, '%')) " +
            "   OR LOWER(a.adminDepartment) LIKE LOWER(CONCAT('%', :searchString, '%'))")
    List<AdminProfile> searchAdmins(@Param("searchString") String searchString);

}
