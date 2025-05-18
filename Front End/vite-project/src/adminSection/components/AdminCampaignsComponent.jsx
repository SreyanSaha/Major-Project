import React, { useState } from 'react';
import { useNavigate } from "react-router-dom";

const ManageCampaigns = () => {
  const campaigns = [
    { id: 1, title: "Events #1", poster: "Campaign Poster 1", status: "Pending", paymentMethod: "UPI", transactionId: "ABC123XYZ", emergency: "No" },
    { id: 2, title: "Events #2", poster: "Campaign Poster 2", status: "Pending", paymentMethod: "UPI", transactionId: "ABC123XYZ", emergency: "No" },
    { id: 3, title: "Events #3", poster: "Campaign Poster 3", status: "Pending", paymentMethod: "UPI", transactionId: "ABC123XYZ", emergency: "No" },
    { id: 4, title: "Events #4", poster: "Campaign Poster 4", status: "Pending", paymentMethod: "UPI", transactionId: "ABC123XYZ", emergency: "No" },
  ];

  const styles = {
    container: {
      padding: "30px",
      backgroundColor: "#ffff",
      fontFamily: "Arial, sans-serif",
      width: "100%",
    },
    heading: {
      fontSize: "28px",
      marginBottom: "20px",
      textAlign: "center",
    },
    searchInput: {
      padding: "10px",
      width: "85%",
      borderRadius: "5px",
      border: "1px solid #ccc",
      marginRight: "10px",
    },
    searchButton: {
      padding: "10px 15px",
      backgroundColor: "#1b3c74",
      color: "#fff",
      border: "none",
      borderRadius: "5px",
      cursor: "pointer",
    },
    cardsContainer: {
      marginTop: "30px",
      display: "flex",
      flexWrap: "wrap",
      gap: "20px",
    },
    card: {
      width: "250px",
      padding: "20px",
      backgroundColor: "#fff",
      borderRadius: "10px",
      boxShadow: "0 4px 8px rgba(0,0,0,0.1)",
    },
    poster: {
      width: "100%",
      height: "150px",
      backgroundColor: "#e0e0e0",
      borderRadius: "8px",
      marginBottom: "10px",
    },
    select: {
      width: "100%",
      padding: "8px",
      marginBottom: "10px",
      borderRadius: "5px",
      border: "1px solid #ccc",
    }
  };
  return (
    <div style={styles.container}>
      <h2 style={styles.heading}>Manage Campaigns</h2>
      <input
        type="text"
        placeholder="Enter Campaigns name"
        style={styles.searchInput}
      />
      <button style={styles.searchButton}>Search Campaigns</button>

      <div style={styles.cardsContainer}>
        {campaigns.map((campaign) => (
          <div key={campaign.id} style={styles.card}>
            <h3>{campaign.title}</h3>
            <div>
              <img
                src="#"
                alt={campaign.poster}
                style={styles.poster}
              />
              <p><strong>Manage Campaigns:</strong></p>
              <select style={styles.select}>
                <option>Select</option>
                <option>Approve</option>
                <option>Reject</option>
                <option>Delete</option>
              </select>
              <p><strong>Approval Status:</strong> {campaign.status}</p>
              <p><strong>Payment Method:</strong> {campaign.paymentMethod}</p>
              <p><strong>Transaction ID:</strong> {campaign.transactionId}</p>
              <p style={{ color: "red" }}><strong>Emergency:</strong> {campaign.emergency}</p>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default ManageCampaigns;
