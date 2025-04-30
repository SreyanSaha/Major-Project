import React, { useState } from "react";
import UploadPost from "./components/UploadPostComponent";
import UploadCampaign from "./components/UploadCampaignComponent";
import UploadEmergencyPost from "./components/UploadEmergencyComponent";

export default function UserDashboard(props) {
  const [activeTab, setActiveTab] = useState(props.activeTab);
  const [showAddPost, setShowAddPost] = useState(false);
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  const [isLoggedin, setLogin] = useState(false);

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
    header: {
      backgroundColor: "#11398f",
      color: "#ffffff",
      padding: "1.2rem 2rem",
      fontSize: "1.5rem",
      fontWeight: "bold",
      textAlign: "center",
      position: "relative",
      marginTop: "-8px",
    },
    hamburger: {
      position: "absolute",
      left: "1rem",
      top: "50%",
      transform: "translateY(-50%)",
      fontSize: "1.8rem",
      cursor: "pointer",
    },
    sidebar: {
      position: "fixed",
      top: "0",
      left: isSidebarOpen ? "0" : "-290px",
      width: "250px",
      height: "100%",
      backgroundColor: "#11398f",
      color: "#ffffff",
      padding: "2rem 1rem",
      transition: "left 0.3s ease",
      display: "flex",
      flexDirection: "column",
      gap: "1.5rem",
      zIndex: 1000,
    },
    sidebarItem: {
      fontSize: "1.2rem",
      cursor: "pointer",
      padding: "0.5rem",
      borderRadius: "8px",
      transition: "background 0.2s",
    },
    sidebarItemHover: {
      backgroundColor: "#1a4bc3",
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
      gridTemplateColumns: "repeat(auto-fit, minmax(250px, 1fr))",
      gap: "1.5rem",
    },
    card: {
      height: "250px",
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

  const renderCards = (label) =>
    Array.from({ length: 3 })
      .map((_, i) => (
        <div key={i} style={styles.card}>
          <div style={styles.imagePlaceholder}></div>
          <div style={styles.title}>
            {label} Title {i + 1}
          </div>
          <div style={styles.desc}>Description of the {label.toLowerCase()} item.</div>
        </div>
      ));

  const handleSearch = () => {
    
  };

  return (
    <div style={styles.container}>
<div style={styles.sidebar}>
  <div
    style={{
      ...styles.sidebarItem,
      fontWeight: "bold",
      fontSize: "1.4rem",
      color: "#ffffff",
      textAlign: "right",
      cursor: "pointer",
      marginBottom: "2rem",
    }}
    onClick={() => setIsSidebarOpen(false)}
  >
    ✖
  </div>

  <div
    style={styles.sidebarItem}
    onMouseOver={(e) => (e.currentTarget.style.backgroundColor = "#1a4bc3")}
    onMouseOut={(e) => (e.currentTarget.style.backgroundColor = "transparent")}
    onClick={() => setIsSidebarOpen(false)}
  >
    Profile
  </div>
  <div
    style={styles.sidebarItem}
    onMouseOver={(e) => (e.currentTarget.style.backgroundColor = "#1a4bc3")}
    onMouseOut={(e) => (e.currentTarget.style.backgroundColor = "transparent")}
    onClick={() => setIsSidebarOpen(false)}
  >
    Settings
  </div>
  <div
    style={styles.sidebarItem}
    onMouseOver={(e) => (e.currentTarget.style.backgroundColor = "#1a4bc3")}
    onMouseOut={(e) => (e.currentTarget.style.backgroundColor = "transparent")}
    onClick={() => setIsSidebarOpen(false)}
  >
    Help
  </div>
  <div
    style={styles.sidebarItem}
    onMouseOver={(e) => (e.currentTarget.style.backgroundColor = "#1a4bc3")}
    onMouseOut={(e) => (e.currentTarget.style.backgroundColor = "transparent")}
    onClick={() => setIsSidebarOpen(false)}
  >
    {isLoggedin===true?"Logout":"Login"}
  </div>
</div>


      <div style={styles.header}>
        <div style={styles.hamburger} onClick={() => setIsSidebarOpen(!isSidebarOpen)}>
          ☰
        </div>
        User Dashboard
      </div>
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

        {showAddPost===false?(
          <div style={styles.searchBoxWrapper}>
          <input
            type="text"
            placeholder={activeTab === "posts"
              ? "Search for posts..."
              : activeTab === "campaigns"
              ? "Search for campaigns..."
              : "Search for Emergency Posts..."}
            style={styles.searchBox}
          />
          <button style={styles.searchButton} onClick={handleSearch}>
            Search
          </button>
        </div>):""}

        <div style={styles.sectionTitle}>
          {activeTab === "posts"
            ? "📝 Posts"
            : activeTab === "campaigns"
            ? "📢 Campaigns"
            : "🚨 Emergency"}
        </div>

        {showAddPost===false?(<div style={styles.cardGrid}>
          {renderCards(
            activeTab === "posts"
              ? "Post"
              : activeTab === "campaigns"
              ? "Campaign"
              : "Emergency"
          )}
        </div>):
        activeTab === "posts"?<UploadPost/>:
        activeTab === "campaigns"?<UploadCampaign/>:<UploadEmergencyPost/>
        }
      </div>
      {isLoggedin===false?(
        <div style={styles.fab} onClick={() => {setShowAddPost(true);}}>
        +
      </div>
      ):""}
    </div>
  );
}
