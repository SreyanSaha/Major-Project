package com.help.repository;

import com.help.dto.CampaignPostData;
import com.help.dto.FullCampaignData;
import com.help.dto.UserCampaign;
import com.help.model.Campaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Integer> {

    @Query("SELECT new com.help.dto.UserCampaign(c.campaignId, c.campaignTitle, c.campaignDescription, c.status, c.imagePath1) FROM Campaign c JOIN c.user.authData a WHERE a.username = :username")
    List<UserCampaign> findAllCampaignsOfUser(@Param("username") String username);

    @Query("SELECT new com.help.dto.CampaignPostData(c.campaignId, u.civicTrustScore, c.campaignTitle, c.campaignDescription, CONCAT(u.userFirstName, ' ', u.userLastName), u.profileImagePath, " +
            "c.status, c.campaignOrganizerContact, c.imagePath1, c.upVoteCount, c.downVoteCount, c.campaignReports, c.campaignCreationTime, c.campaignType) FROM Campaign c JOIN c.user u " +
            "WHERE c.status != -1 ORDER BY c.campaignCreationTime DESC")
    List<CampaignPostData> findLimitedCampaigns(Pageable pageable);

    @Query("SELECT new com.help.dto.CampaignPostData(c.campaignId, u.civicTrustScore, c.campaignTitle, c.campaignDescription, CONCAT(u.userFirstName, ' ', u.userLastName), u.profileImagePath, " +
            "c.status, c.campaignOrganizerContact, c.imagePath1, c.upVoteCount, c.downVoteCount, c.campaignReports, c.campaignCreationTime, c.campaignType) " +
            "FROM Campaign c JOIN c.user u WHERE c.status != -1 ORDER BY c.campaignCreationTime DESC")
    Page<CampaignPostData> findAllCampaigns(Pageable pageable);

    @Query("SELECT new com.help.dto.FullCampaignData(c.campaignId, c.campaignTitle, c.campaignDescription, CONCAT(u.userFirstName, ' ', u.userLastName), c.campaignOrganizerEmail, " +
            "c.status, c.campaignOrganizerContact, c.imagePath1, c.imagePath2, c.imagePath3, c.imagePath4, c.imagePath5, c.upiImage, c.street, c.upVoteCount, c.downVoteCount, c.campaignReports, " +
            "c.city, c.state, c.country, c.postalCode, c.campaignCreationTime, c.campaignType, u.profileImagePath, u.civicTrustScore, u.userId) " +
            "FROM Campaign c JOIN c.user u WHERE c.status != -1 AND c.campaignId = :campaignId")
    Optional<FullCampaignData> findCampaignById(@Param("campaignId")int campaignId);

    @Query("SELECT new com.help.dto.FullCampaignData(c.campaignId, c.campaignTitle, c.campaignDescription, CONCAT(u.userFirstName, ' ', u.userLastName), c.campaignOrganizerEmail, " +
            "c.status, c.campaignOrganizerContact, c.imagePath1, c.imagePath2, c.imagePath3, c.imagePath4, c.imagePath5, c.upiImage, c.street, c.upVoteCount, c.downVoteCount, c.campaignReports, " +
            "c.city, c.state, c.country, c.postalCode, c.campaignCreationTime, c.campaignType, u.profileImagePath, u.civicTrustScore, u.userId) " +
            "FROM Campaign c JOIN c.user u WHERE c.campaignId = :campaignId")
    Optional<FullCampaignData> findCampaignByIdToEdit(@Param("campaignId")int campaignId);

    @Query("SELECT new com.help.dto.CampaignPostData(c.campaignId, u.civicTrustScore, c.campaignTitle, c.campaignDescription, " +
            "CONCAT(u.userFirstName, ' ', u.userLastName), u.profileImagePath, c.status, c.campaignOrganizerContact, " +
            "c.imagePath1, c.upVoteCount, c.downVoteCount, c.campaignReports, c.campaignCreationTime, c.campaignType) " +
            "FROM Campaign c JOIN c.user u " +
            "WHERE c.status != -1 AND (" +
            "LOWER(c.campaignTitle) LIKE LOWER(CONCAT('%', :searchString, '%')) OR " +
            "LOWER(c.campaignDescription) LIKE LOWER(CONCAT('%', :searchString, '%')) OR " +
            "LOWER(c.street) LIKE LOWER(CONCAT('%', :searchString, '%')) OR " +
            "LOWER(c.city) LIKE LOWER(CONCAT('%', :searchString, '%')) OR " +
            "LOWER(c.state) LIKE LOWER(CONCAT('%', :searchString, '%')) OR " +
            "LOWER(c.postalCode) LIKE LOWER(CONCAT('%', :searchString, '%')))")
    Page<CampaignPostData> findAllBySearchString(@Param("searchString") String searchString, Pageable pageable);

    @Query("SELECT new com.help.dto.FullCampaignData(c.campaignId, c.campaignTitle, c.campaignDescription, CONCAT(u.userFirstName, ' ', u.userLastName), c.campaignOrganizerEmail, " +
            "c.status, c.campaignOrganizerContact, c.imagePath1, c.imagePath2, c.imagePath3, c.imagePath4, c.imagePath5, c.upiImage, c.street, c.upVoteCount, c.downVoteCount, c.campaignReports, " +
            "c.city, c.state, c.country, c.postalCode, c.campaignCreationTime, c.campaignType, u.profileImagePath, u.civicTrustScore, u.userId) " +
            "FROM Campaign c JOIN c.user u ORDER BY c.campaignCreationTime DESC")
    List<FullCampaignData> findAllCampaigns();

    @Query("SELECT new com.help.dto.FullCampaignData(c.campaignId, c.campaignTitle, c.campaignDescription, " +
            "CONCAT(u.userFirstName, ' ', u.userLastName), c.campaignOrganizerEmail, c.status, c.campaignOrganizerContact, " +
            "c.imagePath1, c.imagePath2, c.imagePath3, c.imagePath4, c.imagePath5, c.upiImage, c.street, " +
            "c.upVoteCount, c.downVoteCount, c.campaignReports, c.city, c.state, c.country, c.postalCode, " +
            "c.campaignCreationTime, c.campaignType, u.profileImagePath, u.civicTrustScore, u.userId) " +
            "FROM Campaign c JOIN c.user u " +
            "WHERE (LOWER(c.campaignTitle) LIKE LOWER(CONCAT('%', :searchString, '%')) OR " +
            "LOWER(c.campaignDescription) LIKE LOWER(CONCAT('%', :searchString, '%')) OR " +
            "LOWER(c.street) LIKE LOWER(CONCAT('%', :searchString, '%')) OR " +
            "LOWER(c.city) LIKE LOWER(CONCAT('%', :searchString, '%')) OR " +
            "LOWER(c.state) LIKE LOWER(CONCAT('%', :searchString, '%')) OR " +
            "LOWER(c.postalCode) LIKE LOWER(CONCAT('%', :searchString, '%')))")
    List<FullCampaignData> searchCampaignsByKeyword(@Param("searchString") String searchString);

}
