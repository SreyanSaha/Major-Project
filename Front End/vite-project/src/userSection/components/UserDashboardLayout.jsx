import React, { useState } from "react";
import UploadPost from "./UploadPostComponent";
import UploadCampaign from "./UploadCampaignComponent";
import UploadEmergencyPost from "./UploadEmergencyComponent";

export default function UserDashboardLayout({setLayout}) {
  const [activeTab, setActiveTab] = useState("posts");
  const [showAddPost, setShowAddPost] = useState(false);
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  const [isLoggedin, setLogin] = useState(false);

  const handleSearch = () => {
    console.log("Search initiated");
  };

  const renderCards = (type) => {
    return Array.from({ length: 6 }).map((_, index) => (
      <div key={index} style={styles.card}>
        <div style={styles.imagePlaceholder}></div>
        <div style={styles.title}>{type} Title {index + 1}</div>
        <div style={styles.desc}>Short description for {type.toLowerCase()} {index + 1}</div>
      </div>
    ));
  };

  const styles = {
    container: {
      display: "flex",
      flexDirection: "column",
      minHeight: "100vh",
      backgroundColor: "#f5f9ff",
      color: "#11398f",
      fontFamily: "Arial, sans-serif",
      position: "relative",
    },
    main: {
      flex: 1,
      padding: "1.5rem",
      display: "flex",
      flexDirection: "column",
      gap: "1rem",
    },
    tabColumn: {
      display: "flex",
      flexDirection: "column",
      gap: "1rem",
    },
    navBlock: {
      padding: "1rem",
      backgroundColor: "#ffffff",
      border: "1px solid #dce4f1",
      borderRadius: "10px",
      textAlign: "center",
      fontWeight: "600",
      fontSize: "1.1rem",
      cursor: "pointer",
      color: "#ff0000",
      transition: "background-color 0.3s, transform 0.2s",
    },
    navRow: {
      display: "flex",
      gap: "1rem",
    },
    navItem: {
      flex: 1,
      padding: "1rem",
      backgroundColor: "#ffffff",
      border: "1px solid #dce4f1",
      borderRadius: "10px",
      textAlign: "center",
      fontWeight: "600",
      fontSize: "1rem",
      cursor: "pointer",
      color: "#11398f",
      transition: "background-color 0.3s, transform 0.2s",
    },
    navItemActive: {
      backgroundColor: "#11398f",
      color: "#ffffff",
      transform: "scale(1.02)",
    },
    searchBoxWrapper: {
      marginTop: "1rem",
      display: "flex",
      justifyContent: "center",
      alignItems: "center",
      gap: "0.5rem",
    },
    searchBox: {
      width: "100%",
      maxWidth: "500px",
      padding: "0.75rem 1rem",
      borderRadius: "8px",
      border: "1px solid #ccc",
      fontSize: "1rem",
      backgroundColor: "#ffffff",
      color: "#11398f",
      outline: "none",
    },
    searchButton: {
      padding: "0.75rem 1.5rem",
      borderRadius: "8px",
      backgroundColor: "#11398f",
      color: "#ffffff",
      fontSize: "1rem",
      border: "none",
      cursor: "pointer",
    },
    sectionTitle: {
      marginTop: "1rem",
      fontSize: "1.5rem",
      fontWeight: "600",
      textAlign: "center",
      color: "#11398f",
    },
    cardGrid: {
      marginTop: "1rem",
      display: "grid",
      gridTemplateColumns: "repeat(auto-fit, minmax(350px, 1fr))",
      gap: "1.5rem",
    },
    card: {
      height: "250px",
      maxWidth:"400px",
      backgroundColor: "#ffffff",
      padding: "1rem",
      borderRadius: "10px",
      border: "1px solid #dce4f1",
      display: "flex",
      flexDirection: "column",
      gap: "0.75rem",
      boxShadow: "0 2px 5px rgba(0,0,0,0.05)",
      transition: "transform 0.2s",
    },
    imagePlaceholder: {
      backgroundColor: "#cce0ff",
      height: "190px",
      borderRadius: "8px",
    },
    title: {
      fontSize: "1rem",
      fontWeight: "600",
      color: "#11398f",
    },
    desc: {
      fontSize: "0.9rem",
      color: "#555",
    },
    fab: {
      position: "fixed",
      bottom: "30px",
      right: "30px",
      width: "60px",
      height: "60px",
      borderRadius: "50%",
      backgroundColor: "#11398f",
      color: "#ffffff",
      fontSize: "2rem",
      display: "flex",
      alignItems: "center",
      justifyContent: "center",
      cursor: "pointer",
      boxShadow: "0 4px 10px rgba(0,0,0,0.3)",
      zIndex: 1000,
    },
  };

  return (
    <div style={styles.container}>
      <div style={styles.main}>
        <div style={styles.tabColumn}>
          <div
            style={{
              ...styles.navBlock,
              backgroundColor: activeTab === "emergency" ? "#ffe5e5" : "#ffffff",
            }}
            onClick={() => {
              setActiveTab("emergency");
              setShowAddPost(false);
            }}
          >
            🚨 Emergency
          </div>

          <div style={styles.navRow}>
            <div
              style={{
                ...styles.navItem,
                ...(activeTab === "posts" ? styles.navItemActive : {}),
              }}
              onClick={() => {
                setActiveTab("posts");
                setShowAddPost(false);
              }}
            >
              📝 Posts
            </div>
            <div
              style={{
                ...styles.navItem,
                ...(activeTab === "campaigns" ? styles.navItemActive : {}),
              }}
              onClick={() => {
                setActiveTab("campaigns");
                setShowAddPost(false);
              }}
            >
              📢 Campaigns
            </div>
          </div>
        </div>

        {!showAddPost && (
          <div style={styles.searchBoxWrapper}>
            <input
              type="text"
              placeholder={
                activeTab === "posts"
                  ? "Search for posts..."
                  : activeTab === "campaigns"
                  ? "Search for campaigns..."
                  : "Search for Emergency Posts..."
              }
              style={styles.searchBox}
            />
            <button style={styles.searchButton} onClick={handleSearch}>
              Search
            </button>
          </div>
        )}

        <div style={styles.sectionTitle}>
          {activeTab === "posts"
            ? "📝 Posts"
            : activeTab === "campaigns"
            ? "📢 Campaigns"
            : "🚨 Emergency"}
        </div>

        {!showAddPost ? (
          <div style={styles.cardGrid}>
            {renderCards(
              activeTab === "posts"
                ? "Post"
                : activeTab === "campaigns"
                ? "Campaign"
                : "Emergency"
            )}
          </div>
        ) : activeTab === "posts" ? (
          <UploadPost />
        ) : activeTab === "campaigns" ? (
          <UploadCampaign />
        ) : (
          <UploadEmergencyPost />
        )}
      </div>

      {!isLoggedin && (
        <div style={styles.fab} onClick={() => setShowAddPost(true)}>
          +
        </div>
      )}
    </div>
  );
}
