import React, { useState , useEffect} from "react";
import UserDashboardLayout from "./components/UserDashboardLayout";
import UserUploadedPostsComponent from "./components/UserPosts";
import UserCampaignPostsComponent from "./components/UserCampaign";
import UserEmergencyPostsComponent from "./components/UserEmergencyPosts";
import UserProfile from "./components/UserProfile";
import UserProfileCard from "./components/UsersProfileRenderingComponent";
import AISubscriptionCard from "./components/AiSubscription";
import { useNavigate } from "react-router-dom";

export default function UserDashboard(props) {
  const navigate = useNavigate();
  const [authenticated, setAuthenticated] = useState(false);
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  const [searchingUser, setSearchingUser] = useState(false);
  const [layout, setLayout] = useState(<UserDashboardLayout/>);
  const [searchTerm, setSearchTerm] = useState("");

  useEffect(() => {
        try {
          const usr = localStorage.getItem("user");
          if(usr===null)setAuthenticated(false);
          else setAuthenticated(true);
        } catch (err) {
          console.error("Error parsing user from localStorage:", err);
        }
      }, []);

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
    left: isSidebarOpen ? "0" : "-300px",
    width: "270px",
    height: "100vh",
    backgroundColor: "#11398f",
    color: "#ffffff",
    padding: "2rem 1rem",
    transition: "left 0.3s ease",
    display: "flex",
    flexDirection: "column",
    zIndex: 1000,
    boxShadow: "2px 0 10px rgba(0, 0, 0, 0.3)",
    overflowY: "auto",
  },

  sidebarHeader: {
    marginTop:"-50px",
    fontWeight: "bold",
    fontSize: "1.4rem",
    color: "#ffffff",
    marginBottom: "10px",
    textAlign: "left",
    borderBottom: "1px solid rgba(255,255,255,0.3)",
    paddingBottom: "1rem",
  },

  sidebarItem: {
    fontSize: "1.1rem",
    cursor: "pointer",
    padding: "0.7rem 1rem",
    borderRadius: "8px",
    transition: "background 0.2s, transform 0.2s",
  },

  sidebarItemHover: {
    backgroundColor: "#1a4bc3",
  },

  sidebarMessage: {
    fontSize: "1rem",
    marginBottom: "1rem",
    padding: "0 0.5rem",
    color: "#c7d4ff",
  },

  searchInput: {
    padding: "0.5rem",
    borderRadius: "6px",
    border: "none",
    outline: "none",
    marginBottom: "0.5rem",
    fontSize: "1rem",
  },

  searchButton: {
    padding: "0.5rem",
    backgroundColor: "#ffffff",
    color: "#11398f",
    border: "none",
    borderRadius: "6px",
    fontWeight: "bold",
    cursor: "pointer",
    marginBottom: "1.5rem",
  },
  };

  const handleLogout = () =>{
    if(authenticated){localStorage.removeItem("user");setAuthenticated(false);}
    else navigate("/user/login");
  }

  const updatelayout=(component)=>{
    switch(component.toLowerCase()){
      case "posts".toLowerCase():setLayout(<UserUploadedPostsComponent/>);setSearchingUser(false);
      break;
      case "dashboard".toLowerCase():setLayout(<UserDashboardLayout/>);setSearchingUser(false);
      break;
      case "campaigns".toLowerCase():setLayout(<UserCampaignPostsComponent/>);setSearchingUser(false);
      break;
      case "e-posts".toLocaleLowerCase():setLayout(<UserEmergencyPostsComponent/>);setSearchingUser(false);
      break;
      case "profile":setLayout(<UserProfile/>);setSearchingUser(false);
      break;
      case "searchUser".toLocaleLowerCase():setSearchingUser(true);
      setLayout(<UserProfileCard onBack={()=>{setLayout(<UserDashboardLayout/>);setSearchingUser(false);}} searchString={searchTerm}/>);
      break;
      case "subscription":setLayout(<AISubscriptionCard/>);
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

  <div style={styles.sidebarHeader}>👋 Welcome!</div>
  <div style={styles.sidebarMessage}>
    Manage your posts, campaigns, profile, and more from here.
  </div>

  {!searchingUser?(<>
    <input
  type="text"
  placeholder="Search users..."
  value={searchTerm}
  onChange={(e) => setSearchTerm(e.target.value)}
  onKeyDown={(e) => {
    if (e.key === "Enter") {()=>{setIsSidebarOpen(false);updatelayout("searchuser");}}
  }}
  style={styles.searchInput}
/>

<button onClick={()=>{setIsSidebarOpen(false); updatelayout("searchuser");}} style={styles.searchButton}>
  Search
</button>
</>
  ):""}

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
    onClick={() => {setIsSidebarOpen(false); updatelayout("profile");}}
  >
    Profile
  </div>
  <div
    style={styles.sidebarItem}
    onMouseOver={(e) => (e.currentTarget.style.backgroundColor = "#1a4bc3")}
    onMouseOut={(e) => (e.currentTarget.style.backgroundColor = "transparent")}
    onClick={() => {setIsSidebarOpen(false); updatelayout("subscription");}}
  >
    Subscription
  </div>
  
  <div
    style={styles.sidebarItem}
    onMouseOver={(e) => (e.currentTarget.style.backgroundColor = "#1a4bc3")}
    onMouseOut={(e) => (e.currentTarget.style.backgroundColor = "transparent")}
    onClick={() => {setIsSidebarOpen(false); handleLogout();}}
  >
    {authenticated===true?"Logout":"Login"}
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
