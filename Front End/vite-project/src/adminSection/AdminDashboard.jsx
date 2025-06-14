import React, { useState, useEffect } from 'react';
import { useNavigate } from "react-router-dom";
import ManageCampaigns from './components/AdminCampaignsComponent';
import AdminDefaultDetails from './components/AdminDefaultDetails';
import AdminProfileCard from './components/AdminProfile';
import UserDirectory from './components/AdminUserComponent';
import AdminProfileList from './components/AdminManagementComponent';
import axios from "axios";

export default function AdminDashboard() {
  const navigate = useNavigate();
  const [msg, updateMsg] = useState(null);
  const [authenticated, setAuthenticated] = useState(false);
  const [activeMenu, setActiveMenu] = useState("Dashboard");
  const [component, setComponents] = useState(<AdminDefaultDetails/>);


  useEffect(() => {
    try {
      const admin = JSON.parse(localStorage.getItem("admin"));
      if (admin?.username && admin?.token && admin?.role === 1) {
        setAuthenticated(true);
      } else navigate("/admin/login");
    } catch (err) {
      console.error("Error parsing user from localStorage:", err);
      navigate("/admin/login");
    }
  }, [navigate]);

  const handleLogout = () => {
    localStorage.removeItem("admin");
    setAuthenticated(false);
    navigate("/admin/login");
  };

  const styles = {
    searchInput: {
      padding: "10px",
      width: "85%",
      borderRadius: "5px",
      border: "1px solid #ccc",
      marginRight: "10px",
      marginBottom:"10px",
    },
    searchButton: {
      padding: "10px 15px",
      backgroundColor: "#1b3c74",
      color: "#fff",
      border: "none",
      borderRadius: "5px",
      cursor: "pointer",
      marginBottom:"10px",
    },
    imageGroup: {
    display: 'flex',
    gap: '10px',
    flexWrap: 'wrap',
    justifyContent: 'center',
  },
    dashboardContainer: {
      display: 'flex',
      height: '100vh',
      fontFamily: 'Arial, sans-serif',
      backgroundColor: '#f9fafb',
    },
    sidebar: {
      width: '250px',
      backgroundColor: '#1e3a8a',
      color: 'white',
      padding: '20px',
    },
    sidebarHeading: {
      color: 'orange',
      fontSize: '24px',
      marginBottom: '20px',
    },
    listItem: (isActive) => ({
      marginBottom: '12px',
      padding: '10px 15px',
      fontWeight: '500',
      borderRadius: '6px',
      backgroundColor: isActive ? '#3b82f6' : 'transparent',
      cursor: 'pointer',
      transition: 'background-color 0.2s',
    }),
    mainContent: {
      flex: 1,
      padding: '30px',
      overflowY: 'auto',
    },
    mainHeading: {
      textAlign: 'center',
      fontSize: '28px',
      fontWeight: 'bold',
      marginBottom: '30px',
      color: '#1e3a8a',
    },
    cardsGrid: {
      display: 'grid',
      gridTemplateColumns: 'repeat(1, minmax(400px, 1fr))',
      gap: '20px',
    },
    card: {
      backgroundColor: 'white',
      borderRadius: '12px',
      padding: '20px',
      boxShadow: '0 2px 8px rgba(0, 0, 0, 0.1)',
      border: '1px solid #e5e7eb',
    },
    cardHeader: {
      display: 'flex',
      justifyContent: 'space-between',
      alignItems: 'center',
      marginBottom: '15px',
      borderBottom: '2px solid #f3f4f6',
      paddingBottom: '10px',
    },
    cardHeading: {
      color: '#1e3a8a',
      fontWeight: 'bold',
      fontSize: '18px',
      margin: 0,
    },
    statusBadge: (status) => ({
      padding: '4px 12px',
      borderRadius: '20px',
      fontSize: '12px',
      fontWeight: 'bold',
      color: status.color,
      backgroundColor: status.bg,
    }),
    imageSection: {
      marginBottom: '15px',
    },
    imageContainer: {
      position: 'relative',
      marginBottom: '10px',
    },
    image: {
      width: '235px',
      height: '250px',
      objectFit: 'cover',
      borderRadius: '8px',
    },
    imageControls: {
      display: 'flex',
      justifyContent: 'space-between',
      alignItems: 'center',
      marginTop: '8px',
    },
    arrowBtn: {
      backgroundColor: '#1e3a8a',
      color: 'white',
      border: 'none',
      borderRadius: '6px',
      padding: '6px 12px',
      cursor: 'pointer',
      fontSize: '14px',
    },
    imageCounter: {
      fontSize: '14px',
      color: '#6b7280',
      fontWeight: '500',
    },
    infoSection: {
      display: 'grid',
      gridTemplateColumns: '1fr 1fr',
      gap: '10px',
      marginBottom: '15px',
    },
    infoItem: {
      marginBottom: '8px',
    },
    infoLabel: {
      fontWeight: 'bold',
      color: '#374151',
      fontSize: '14px',
    },
    infoValue: {
      color: '#6b7280',
      fontSize: '14px',
      marginTop: '2px',
    },
    locationSection: {
      backgroundColor: '#f9fafb',
      padding: '12px',
      borderRadius: '8px',
      marginBottom: '15px',
    },
    locationTitle: {
      fontWeight: 'bold',
      color: '#1e3a8a',
      marginBottom: '8px',
      fontSize: '14px',
    },
    engagementStats: {
      display: 'flex',
      justifyContent: 'space-around',
      backgroundColor: '#f3f4f6',
      padding: '10px',
      borderRadius: '8px',
      marginBottom: '15px',
    },
    stat: {
      textAlign: 'center',
    },
    statValue: {
      fontWeight: 'bold',
      fontSize: '18px',
      color: '#1e3a8a',
    },
    statLabel: {
      fontSize: '12px',
      color: '#6b7280',
    },
    actionButtons: {
      display: 'flex',
      gap: '10px',
      marginTop: '15px',
    },
    actionBtn: (bgColor) => ({
      flex: 1,
      padding: '8px 12px',
      border: 'none',
      borderRadius: '6px',
      color: 'white',
      backgroundColor: bgColor,
      cursor: 'pointer',
      fontSize: '14px',
      fontWeight: '500',
    }),
    afterWorkSection: {
      marginTop: '15px',
      padding: '12px',
      backgroundColor: '#ecfdf5',
      borderRadius: '8px',
      border: '1px solid #10b981',
    },
    alertDiv: {
      backgroundColor: '#fef2f2',
      border: '1px solid #fca5a5',
      borderRadius: '8px',
      padding: '12px',
      marginBottom: '20px',
    },
    alertText: {
      color: '#dc2626',
      margin: 0,
    },
  };

  const changeComponents = (item) => {
    switch (item) {
      case "Manage Posts":setComponents(<AdminDefaultDetails/>);
      break;
      case "Manage Campaigns":setComponents(<ManageCampaigns/>);
      break;
      case "Profile":setComponents(<AdminProfileCard/>);
      break;
      case "User Access Control":setComponents(<UserDirectory/>);
      break;
      case "Admin Access Control":setComponents(<AdminProfileList/>);
      break;
        
    }
  };

  const menuItems = [
    'Manage Posts',
    'Manage Campaigns',
    'User Access Control',
    'Admin Access Control',
    'Profile',
    'Logout',
  ];

  if (!authenticated) return null;

  return (
    <div style={styles.dashboardContainer}>
      <div style={styles.sidebar}>
        <h2 style={styles.sidebarHeading}>Admin Dashboard</h2>
        <ul style={{ listStyle: 'none', padding: 0 }}>
          {menuItems.map((item) => (
            <li
              key={item}
              style={styles.listItem(activeMenu === item)}
              onClick={() => {
                if (item === "Logout") handleLogout();
                else {
                  setActiveMenu(item);
                  changeComponents(item);
                }
              }}
              onMouseOver={(e) => (e.currentTarget.style.backgroundColor = '#2563eb')}
              onMouseOut={(e) => (e.currentTarget.style.backgroundColor = activeMenu === item ? '#3b82f6' : 'transparent')}
            >
              {item}
            </li>
          ))}
        </ul>
      </div>
      {component}
    </div>
  );
}