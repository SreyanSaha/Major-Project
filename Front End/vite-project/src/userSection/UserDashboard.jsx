import React, { useState } from "react";
import UserDashboardLayout from "./components/UserDashboardLayout";
import UserUploadedPostsComponent from "./components/UserPosts";
import UserCampaignPostsComponent from "./components/UserCampaign";
import UserEmergencyPostsComponent from "./components/UserEmergencyPosts";

export default function UserDashboard(props) {
  const [activeTab, setActiveTab] = useState(props.activeTab);
  const [showAddPost, setShowAddPost] = useState(false);
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  const [isLoggedin, setLogin] = useState(false);
  const [layout, setLayout] = useState(<UserDashboardLayout/>);

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
  };

  const handleSearch = () => {
    
  };

  const updatelayout=(component)=>{
    switch(component){
      case "posts".toLowerCase():setLayout(<UserUploadedPostsComponent/>);
      break;
      case "dashboard".toLowerCase():setLayout(<UserDashboardLayout/>);
      break;
      case "campaigns".toLowerCase():setLayout(<UserCampaignPostsComponent/>);
      break;
      case "e-posts".toLocaleLowerCase():setLayout(<UserEmergencyPostsComponent/>);
      break;
    }
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
    onClick={() => {setIsSidebarOpen(false); updatelayout("dashboard");}}
  >
    Dashboard
  </div>
  <div
    style={styles.sidebarItem}
    onMouseOver={(e) => (e.currentTarget.style.backgroundColor = "#1a4bc3")}
    onMouseOut={(e) => (e.currentTarget.style.backgroundColor = "transparent")}
    onClick={() => {setIsSidebarOpen(false); updatelayout("posts");}}
  >
    Your posts
  </div>
  <div
    style={styles.sidebarItem}
    onMouseOver={(e) => (e.currentTarget.style.backgroundColor = "#1a4bc3")}
    onMouseOut={(e) => (e.currentTarget.style.backgroundColor = "transparent")}
    onClick={() => {setIsSidebarOpen(false); updatelayout("campaigns");}}
  >
    Your campaigns
  </div>
  <div
    style={styles.sidebarItem}
    onMouseOver={(e) => (e.currentTarget.style.backgroundColor = "#1a4bc3")}
    onMouseOut={(e) => (e.currentTarget.style.backgroundColor = "transparent")}
    onClick={() => {setIsSidebarOpen(false); updatelayout("e-posts");}}
  >
    Your emergency posts
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
      {layout}
    </div>
  );
}
