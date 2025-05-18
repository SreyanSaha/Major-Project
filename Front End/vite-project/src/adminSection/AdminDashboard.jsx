import React, { useState , useEffect} from 'react';
import { useNavigate } from "react-router-dom";
import EmergencyPanel from './components/AdminEmergencyComponent';
import AssignVolunteers from './components/AssignVolunteersComponent';
import ManageCampaigns from './components/AdminCampaignsComponent';
const issues = [
  {
    id: 1,
    name: 'John Doe',
    personId: 'USR123',
    location: 'Sector 21, Noida',
    link: 'https://www.google.com/maps?q=Sector+21,+Noida',
    description: 'Broken road near sector 21.',
    images: [
      'https://via.placeholder.com/300x120?text=Road+Image+1',
      'https://via.placeholder.com/300x120?text=Road+Image+2',
    ],
  },
  {
    id: 3,
    name: 'Jane Smith',
    personId: 'USR125',
    location: 'Andheri East, Mumbai',
    link: 'https://www.google.com/maps?q=Andheri+East,+Mumbai',
    description: 'Potholes causing frequent vehicle breakdowns.',
    images: [
      'https://via.placeholder.com/300x120?text=Pothole+1',
      'https://via.placeholder.com/300x120?text=Pothole+2',
      'https://via.placeholder.com/300x120?text=Pothole+3',
    ],
  },
  {
    id: 4,
    name: 'Bob Lee',
    personId: 'USR126',
    location: 'Patia, Bhubaneswar',
    link: 'https://www.google.com/maps?q=Patia,+Bhubaneswar',
    description: 'Water logging near bus stop.',
    images: [
      'https://via.placeholder.com/300x120?text=Water+Log+1',
      'https://via.placeholder.com/300x120?text=Water+Log+2',
    ],
  },
];

export default function AdminDashboard() {
  const navigate = useNavigate();
  const [authenticated, setAuthenticated] = useState(false);
  const [activeMenu, setActiveMenu] = useState("Dashboard");
  const [imageIndexes, setImageIndexes] = useState(
    issues.reduce((acc, issue) => {
      acc[issue.id] = 0;
      return acc;
    }, {})
  );

  useEffect(() => {
      try {
        const admin = JSON.parse(localStorage.getItem("admin"));
        console.log("Fetched amdin's username:", admin?.username);
        if (admin?.username && admin?.token && admin?.role === 1) setAuthenticated(true);
        else navigate("/admin/login");
      } catch (err) {
        console.error("Error parsing user from localStorage:", err);
        updateMsg("Failed to fetch your data, please login again.");
        navigate("/admin/login");
      }
    }, [navigate]);

  const handleImageChange = (id, direction) => {
    setImageIndexes((prev) => {
      const totalImages = issues.find((issue) => issue.id === id).images.length;
      const newIndex = (prev[id] + direction + totalImages) % totalImages;
      return { ...prev, [id]: newIndex };
    });
  };

  const handleLogout=()=>{
    localStorage.removeItem("admin");
    setAuthenticated(false);
    navigate("/admin/login");
  };

  const styles = {
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
      gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))',
      gap: '20px',
    },
    card: {
      backgroundColor: 'white',
      borderRadius: '12px',
      padding: '15px',
      boxShadow: '0 2px 8px rgba(0, 0, 0, 0.1)',
      transition: 'transform 0.3s, box-shadow 0.3s',
    },
    cardHover: {
      transform: 'scale(1.02)',
      boxShadow: '0 4px 12px rgba(0, 0, 0, 0.2)',
    },
    cardHeading: {
      color: '#1e3a8a',
      fontWeight: 'bold',
      marginBottom: '10px',
    },
    image: {
      width: '100%',
      height: '120px',
      objectFit: 'cover',
      borderRadius: '6px',
      marginBottom: '10px',
    },
    imageControls: {
      display: 'flex',
      justifyContent: 'space-between',
      marginBottom: '10px',
    },
    arrowBtn: {
      backgroundColor: '#1e3a8a',
      color: 'white',
      border: 'none',
      borderRadius: '4px',
      padding: '4px 10px',
      cursor: 'pointer',
    },
    paragraph: {
      margin: '5px 0',
    },
    link: {
      color: '#2563eb',
      textDecoration: 'underline',
    },
    thumbs: {
      fontSize: '20px',
      margin: '10px 0',
    },
    dropdown: {
      width: '100%',
      padding: '6px',
      border: '1px solid #ccc',
      borderRadius: '6px',
      marginTop: '5px',
    },
  };

  const defaultDetails=(<div style={styles.mainContent}>
    <h1 style={styles.mainHeading}>Road Management Dashboard</h1>
    <div style={styles.cardsGrid}>
      {issues.map((issue) => (
        <div
          key={issue.id}
          style={styles.card}
          onMouseOver={(e) => {
            Object.assign(e.currentTarget.style, styles.cardHover);
          }}
          onMouseOut={(e) => {
            Object.assign(e.currentTarget.style, styles.card);
          }}
        >
          <h3 style={styles.cardHeading}>Issue #{issue.id}</h3>
          <div style={styles.imageControls}>
            <button
              style={styles.arrowBtn}
              onClick={() => handleImageChange(issue.id, -1)}
            >
              ◀
            </button>
            <button
              style={styles.arrowBtn}
              onClick={() => handleImageChange(issue.id, 1)}
            >
              ▶
            </button>
          </div>
          <img
            src={issue.images[imageIndexes[issue.id]]}
            alt={`Issue ${issue.id}`}
            style={styles.image}
          />
          <p style={styles.paragraph}>
            <strong>Name:</strong> {issue.name}
          </p>
          <p style={styles.paragraph}>
            <strong>Person ID:</strong> {issue.personId}
          </p>
          <p style={styles.paragraph}>
            <strong>Location:</strong>{' '}
            <a
              href={issue.link}
              target="_blank"
              rel="noreferrer"
              style={styles.link}
            >
              {issue.location}
            </a>
          </p>
          <p style={styles.paragraph}>
            <strong>Description:</strong> {issue.description}
          </p>
          <div style={styles.thumbs}>👍 👎</div>
          <label>
            <strong>Status:</strong>
          </label>
          <select style={styles.dropdown}>
            <option>Pending</option>
            <option>In Progress</option>
            <option>Completed</option>
          </select>
        </div>
      ))}
    </div>
    </div>);
  const [component, setComponents] = useState(defaultDetails);
  
  const chnageComponents=(item)=>{
    switch(item){
      case "Dashboard":setComponents(defaultDetails);
      break;
      case "Assign Volunteers":setComponents(AssignVolunteers);
      break;
      case "Emergency Issues":setComponents(EmergencyPanel);
      break;
      case "Manage Campaigns":setComponents(ManageCampaigns);
      break;
      case "":
      break;
    }
  }
  const menuItems = [
    'Dashboard',
    'Assign Volunteers',
    'Emergency Issues',
    'Manage Campaigns',
    'User Access Control',
    'Push Notification',
    'Communication Panel',
    'Emergency Panels',
    'Logout',
  ];
  if (!authenticated) return null;
  return (
    <div style={styles.dashboardContainer}>
      <div style={styles.sidebar}>
        <h2 style={styles.sidebarHeading}>Admin Dashboard</h2>
        <ul style={{ listStyle: 'none', padding: 0 }}>
          {menuItems.map((item) => (
              <li key={item} style={styles.listItem(activeMenu === item)}
              onClick={() => {
                if(item==="Logout")handleLogout();
                else{setActiveMenu(item);chnageComponents(item);}}}
              onMouseOver={(e) =>(e.currentTarget.style.backgroundColor = '#2563eb')}
              onMouseOut={(e) =>(e.currentTarget.style.backgroundColor = activeMenu === item ? '#3b82f6' : 'transparent')}
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
