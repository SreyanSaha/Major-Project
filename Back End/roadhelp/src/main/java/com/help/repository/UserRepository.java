package com.help.repository;

import com.help.dto.ServiceResponse;
import com.help.dto.UserProfile;
import com.help.dto.UserSearchData;
import com.help.model.User;
import org.hibernate.annotations.processing.SQL;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User getUserByAuthData_AuthId(int authId);

    @Query("SELECT u FROM User u JOIN u.authData a WHERE a.username = :username")
    Optional<User> findByUsername(@Param("username") String username);

    @Query("SELECT new com.help.dto.UserProfile(u.userId, u.userFirstName, u.userLastName, u.userEmailId, u.userPhoneNumber, u.profileImagePath, u.civicTrustScore, u.userStatus, " +
            "u.timeOutEndTime, u.street, u.city, u.state, u.zipCode, u.country, u.signupDateTime) FROM User u WHERE u.userId = :userId")
    Optional<UserProfile> findUserById(@Param("userId")int userId);

    @Query("SELECT new com.help.dto.UserProfile(u.userId, u.userFirstName, u.userLastName, u.profileImagePath, u.civicTrustScore, u.userStatus, " +
            "u.timeOutEndTime, u.city, u.state, u.country, u.signupDateTime) FROM User u WHERE u.userId = :userId")
    Optional<UserProfile> findUserProfileForSearchById(@Param("userId")int userId);

    @Query("SELECT new com.help.dto.UserProfile(u.userId, u.userFirstName, u.userLastName, u.userEmailId, u.userPhoneNumber, u.profileImagePath, u.civicTrustScore, u.userStatus, " +
            "u.timeOutEndTime, u.street, u.city, u.state, u.zipCode, u.country, u.signupDateTime) FROM User u JOIN u.authData a WHERE a.username = :username")
    Optional<UserProfile> findUserProfile(@Param("username")String username);

    @Query("""
    SELECT new com.help.dto.UserSearchData(
        u.userId,
        u.userFirstName,
        u.userLastName,
        u.profileImagePath,
        u.civicTrustScore,
        COUNT(DISTINCT p.postId),
        COUNT(DISTINCT c.campaignId),
        u.userStatus,
        u.signupDateTime
    )
    FROM User u
    LEFT JOIN Post p ON p.user.userId = u.userId
    LEFT JOIN Campaign c ON c.user.userId = u.userId
    WHERE LOWER(u.userFirstName) LIKE LOWER(CONCAT('%', :searchString, '%'))
       OR LOWER(u.userLastName) LIKE LOWER(CONCAT('%', :searchString, '%'))
    GROUP BY u.userId, u.userFirstName, u.userLastName, u.profileImagePath,
             u.civicTrustScore, u.userStatus, u.signupDateTime
""")
    Page<UserSearchData> searchUser(Pageable pageable, @Param("searchString") String searchString);

    @Query("""
    SELECT new com.help.dto.UserSearchData(
        u.userId,
        u.userFirstName,
        u.userLastName,
        u.profileImagePath,
        u.civicTrustScore,
        COUNT(DISTINCT p.postId),
        COUNT(DISTINCT c.campaignId),
        u.userStatus,
        u.signupDateTime
    )
    FROM User u
    LEFT JOIN Post p ON p.user.userId = u.userId
    LEFT JOIN Campaign c ON c.user.userId = u.userId
    WHERE LOWER(u.userFirstName) LIKE LOWER(CONCAT('%', :firstName, '%'))
      AND LOWER(u.userLastName) LIKE LOWER(CONCAT('%', :lastName, '%'))
    GROUP BY u.id, u.userFirstName, u.userLastName, u.profileImagePath,
             u.civicTrustScore, u.userStatus, u.signupDateTime
""")
    Page<UserSearchData> searchUser(Pageable pageable, @Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query("SELECT new com.help.dto.UserProfile(u.userId, u.userFirstName, u.userLastName, u.userEmailId, u.userPhoneNumber, u.profileImagePath, u.civicTrustScore, u.userStatus, " +
            "u.timeOutEndTime, u.street, u.city, u.state, u.zipCode, u.country, u.signupDateTime, u.userReports) FROM User u ORDER BY u.userReports DESC")
    List<UserProfile> findUserProfiles();


    @Query("""
    SELECT new com.help.dto.UserProfile(
        u.userId,
        u.userFirstName,
        u.userLastName,
        u.userEmailId,
        u.userPhoneNumber,
        u.profileImagePath,
        u.civicTrustScore,
        u.userStatus,
        u.timeOutEndTime,
        u.street,
        u.city,
        u.state,
        u.zipCode,
        u.country,
        u.signupDateTime,
        u.userReports
    )
    FROM User u
    WHERE LOWER(u.userFirstName) LIKE LOWER(CONCAT('%', :searchString, '%'))
       OR LOWER(u.userLastName) LIKE LOWER(CONCAT('%', :searchString, '%'))
       OR LOWER(u.userEmailId) LIKE LOWER(CONCAT('%', :searchString, '%'))
       OR STR(u.userPhoneNumber) LIKE CONCAT('%', :searchString, '%')
       OR LOWER(u.street) LIKE LOWER(CONCAT('%', :searchString, '%'))
       OR LOWER(u.city) LIKE LOWER(CONCAT('%', :searchString, '%'))
       OR LOWER(u.state) LIKE LOWER(CONCAT('%', :searchString, '%'))
       OR LOWER(u.zipCode) LIKE LOWER(CONCAT('%', :searchString, '%'))
       OR LOWER(u.country) LIKE LOWER(CONCAT('%', :searchString, '%'))
""")
    List<UserProfile> searchUserProfiles(@Param("searchString") String searchString);

}
