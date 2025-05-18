import React, { useState } from 'react';
import { useNavigate } from "react-router-dom";

const EmergencyPanel = () => {

  const emergencies = [
    {
      type: "Emergency",
      title: "Emergency #1",
      name: "Rahul Sen",
      phone: "+91 9876543210",
      location: "Kolkata, West Bengal",
      description: "Massive pothole near the main road causing traffic congestion.",
      color: "#f44336",
    },
    {
      type: "SOS",
      title: "SOS #2",
      name: "Anjali Sharma",
      phone: "+91 9123456780",
      location: "Delhi, India",
      description: "Collapsed drain leading to flooding in residential area.",
      color: "#2196f3",
    },
    {
      type: "Emergency",
      title: "Emergency #3",
      name: "Vikram Patel",
      phone: "+91 9988776655",
      location: "Ahmedabad, Gujarat",
      description: "Streetlight poles fallen during storm—risk of electrical hazard.",
      color: "#f44336",
    },
    {
      type: "SOS",
      title: "SOS #4",
      name: "Priya Das",
      phone: "+91 9012345678",
      location: "Bhubaneswar, Odisha",
      description: "Medical help requested for stranded elderly person.",
      color: "#2196f3",
    },
  ];

  const containerStyle = {
    padding: "20px",
    fontFamily: "Arial, sans-serif",
    backgroundColor: "#f4f6f8",
    width: "100%",
  };

  const titleStyle = {
    fontSize: "28px",
    fontWeight: "bold",
    marginBottom: "20px",
    textAlign: "center",
  };

  const cardContainerStyle = {
    display: "flex",
    gap: "20px",
    flexWrap: "wrap",
    justifyContent: "center",
  };

  const cardStyle = (borderColor) => ({
    width: "300px",
    backgroundColor: "white",
    border: `2px solid ${borderColor}`,
    borderRadius: "10px",
    padding: "15px",
    boxShadow: "0 2px 10px rgba(0,0,0,0.1)",
  });

  const badgeStyle = (bgColor) => ({
    display: "inline-block",
    backgroundColor: bgColor,
    color: "#fff",
    padding: "3px 8px",
    borderRadius: "5px",
    fontSize: "12px",
    float: "right",
  });
  
  return (
    <div style={containerStyle}>
      <div style={titleStyle}>Emergency Panel</div>
      <div style={cardContainerStyle}>
        {emergencies.map((e, index) => (
          <div key={index} style={cardStyle(e.color)}>
            <div style={{ marginBottom: "10px", fontWeight: "bold", fontSize: "16px" }}>
              {e.title}
              <span style={badgeStyle(e.color)}>{e.type}</span>
            </div>
            <img
              src={`https://via.placeholder.com/300x150?text=${e.title.replace(" ", "+")}`}
              alt={e.title}
              style={{ width: "100%", height: "150px", objectFit: "cover", borderRadius: "5px" }}
            />
            <p><strong>Name:</strong> {e.name}</p>
            <p><strong>Phone No:</strong> {e.phone}</p>
            <p>
              <strong>Location:</strong>{" "}
              <a href={`https://www.google.com/maps/search/${e.location}`} target="_blank" rel="noopener noreferrer">
                {e.location}
              </a>
            </p>
            <p><strong>Description:</strong> {e.description}</p>
            <p>
              <strong>Action:</strong>{" "}
              <select>
                <option>Select</option>
                <option>Assign Volunteer</option>
                <option>Mark as Resolved</option>
              </select>
            </p>
          </div>
        ))}
      </div>
    </div>
  );
};

export default EmergencyPanel;
