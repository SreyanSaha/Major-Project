import React, { useState , useEffect} from "react";
import { useParams } from 'react-router-dom';
import { useNavigate } from "react-router-dom";
import axios from "axios";
import LoadingOverlay from "../../loadingComponents/Loading";

const userStatusLabels = {
  0: "Inactive / Timeout",
  1: "Active",
  2: "Deleted",
  3: "Blacklisted",
};

// Function to get the color based on trust score
const getTrustColor = (score) => {
  if (score >= 0 && score <= 299) return "#f44336";    // Red
  if (score >= 300 && score <= 599) return "#ff9800";  // Orange
  if (score >= 600 && score <= 849) return "#ffc107";  // Yellow
  if (score >= 850 && score <= 1000) return "#4caf50"; // Green
  return "#3366ff"; // Default Blue if outside range
};

const AllUserProfile = () => {
  const { userId } = useParams();
  const navigate = useNavigate();
  const [authenticated, setAuthenticated] = useState(false);
  const [msg,updateMsg] = useState(null);
  const [processing, setProcessing] = useState(false);
  const [user, setUser] = useState({});

  // const user = {
  //   userId: 101,
  //   userFirstName: "Alice",
  //   userLastName: "Smith",
  //   userEmailId: "alice.smith@example.com",
  //   userPhoneNumber: 9876543210,
  //   profileImagePath: "https://randomuser.me/api/portraits/women/44.jpg",
  //   civicTrustScore: 920,
  //   userStatus: 1,
  //   signupDateTime: "2024-06-01T14:20:00",
  //   timeOutEndTime: null,
  //   country: "Canada",
  //   zipCode: "M5V 3L9",
  //   userRole: "Community Member",
  // };
  
  const checkTokenHealth = async() =>{
    try{
      let user = localStorage.getItem("user");
      if(user===null)return false;
      else user = JSON.parse(user);
      const response = await axios.post("http://localhost:8080/token/health",{},
        {
          headers:{
            "Authorization": "Bearer "+user.token,
            "Content-Type": "application/json"
          }
        }
      );
      if(response.status===200 && response.data==="validated.")return true;
    }catch(exception){
      if(exception.response && (exception.response.status===401 || exception.response.status===403)){
        updateMsg("Please log in to access updates — your session may have expired or you're not signed in.");
        localStorage.removeItem("user");
      }
    }
    return false;
  }
useEffect(() => {
    if (checkTokenHealth() && localStorage.getItem("user")!==null){
      setAuthenticated(true);
      loadUserProfile();
    } else{
      setAuthenticated(false);
      navigate("/user/login");
    }
  }, [navigate]);

  const loadUserProfile = async ()=>{
    try{
      const user = JSON.parse(localStorage.getItem("user"));
      setProcessing(true);
      const response=await axios.get(`http://localhost:8080/user/other-users/${userId}`,
        {
          headers:{
            "Authorization": "Bearer "+user.token,
            "Content-Type": "application/json"
          }
        }
      );
      if(response.status===200){
        setUser(response.data.object);
        setProcessing(false);
      }else if(response.status===202){
        setProcessing(false);
        updateMsg(response.data.msg);
      }
    }catch(exception){
      console.log(exception);
      setProcessing(false);
      updateMsg(exception?.response?.data?.msg || exception.message );
      navigate("/user/login");
    }
  };

  const reportUser = async()=>{
    try{
      const user = JSON.parse(localStorage.getItem("user"));
      setProcessing(true);
      const response=await axios.post("http://localhost:8080/user/report",userId,
        {
          headers:{
            "Authorization": "Bearer "+user.token,
            "Content-Type": "application/json"
          }
        }
      );
      if(response.status===200){
        setUser(response.data.object);
        updateMsg(response.data.msg);
        setProcessing(false);
      }else if(response.status===202){
        setProcessing(false);
        updateMsg(response.data.msg);
      }
    }catch(exception){
      console.log(exception);
      setProcessing(false);
      updateMsg(exception?.response?.data?.msg || exception.message );
      navigate("/user/login");
    }
  };
  
  const trustPercent = Math.min((user.civicTrustScore / 1000) * 100, 100);
  const trustColor = getTrustColor(user.civicTrustScore);

  return (<>
    <div style={styles.header1}>User Dashboard</div>
    {processing?<LoadingOverlay/>:""}
      {msg!=null?(<div style={styles.alertDiv}>
            <h3 style={styles.alertText}>{msg}</h3>
          </div>):""}
    <div style={styles.pageContainer}>
      <button onClick={()=>{navigate(-1);}} style={styles.backButton} aria-label="Go Back">
        ← Back
      </button>

      <div style={styles.profileCard}>
        <div style={styles.headerSection}>
            <img
              src={user.profileImagePath!==null && user.profileImagePath!==undefined?
                  `http://localhost:8080/media${user.profileImagePath.replace("\\", "/")}`:
                "profile image"}
              alt={`${user.userFirstName} ${user.userLastName}`}
              style={styles.profileImage}
              loading="lazy"
            />
          <div>
            <h1 style={styles.name}>
              {user.userFirstName} {user.userLastName}
            </h1>
            {/* <p style={styles.userRole}>{user.userRole}</p> */}
          </div>
        </div>

        <section style={styles.section}>
          <h2 style={styles.sectionTitle}>Civic Trust Score</h2>
          <div style={styles.trustBarBackground}>
            <div
              style={{ ...styles.trustBarFill, width: `${trustPercent}%`, backgroundColor: trustColor }}
              aria-valuenow={user.civicTrustScore}
              aria-valuemin={0}
              aria-valuemax={1000}
              role="progressbar"
            />
          </div>
          <p style={{ ...styles.trustScoreLabel, color: trustColor }}>
            {user.civicTrustScore} / 1000
          </p>
        </section>

        <section style={styles.section}>
          <h2 style={styles.sectionTitle}>Personal Info</h2>
          <div style={styles.infoRow}>
            <span style={styles.icon}>🏙️</span>
            <span style={styles.label}>City</span>
            <span style={styles.value}>{user.city}</span>
          </div>
          <div style={styles.infoRow}>
            <span style={styles.icon}>🗺️</span>
            <span style={styles.label}>State</span>
            <span style={styles.value}>{user.state}</span>
          </div>
          <div style={styles.infoRow}>
            <span style={styles.icon}>🌍</span>
            <span style={styles.label}>Country</span>
            <span style={styles.value}>{user.country}</span>
          </div>
          {/* <div style={styles.infoRow}>
            <span style={styles.icon}>🏷️</span>
            <span style={styles.label}>Zip Code</span>
            <span style={styles.value}>{user.zipCode}</span>
          </div> */}
        </section>

        <section style={styles.section}>
          <h2 style={styles.sectionTitle}>Account Details</h2>
          <div style={styles.infoRow}>
            <span style={styles.icon}>⚙️</span>
            <span style={styles.label}>Status</span>
            <span style={styles.value}>
              {userStatusLabels[user.userStatus] || "Unknown"}
            </span>
          </div>
          <div style={styles.infoRow}>
            <span style={styles.icon}>🗓️</span>
            <span style={styles.label}>Signup Date</span>
            <span style={styles.value}>
              {new Date(user.signupDateTime).toLocaleString(undefined, {
                dateStyle: "medium",
                timeStyle: "short",
              })}
            </span>
          </div>
          {user.timeOutEndTime!==null && user.timeOutEndTime!==undefined?(
            <div style={styles.infoRow}>
              <span style={styles.icon}>⏳</span>
              <span style={styles.label}>Timeout End</span>
                <span style={styles.value}>
                {new Date(user.timeOutEndTime).toLocaleString()}
              </span>
              </div>
              ):(
                <div style={styles.infoRow}>
                <span style={styles.icon}>⏳</span>
                <span style={styles.label}>Timeout End</span>
                <span style={styles.value}>Not in Time Out</span>
                </div>
              )}
        </section>
        <span style={styles.reports} onClick={()=>{reportUser();}}>🚩</span>
      </div>
    </div>
    </>
  );
};

const styles = {
  alertDiv: {
    textAlign: "center",
  },

  alertText: {
    backgroundColor: "rgb(255, 64, 57)",
    padding: "2px",
    color: "white",
    borderRadius: "8px",
    width: "50%",
    maxWidth: "80%",
    margin: "auto",
    marginBottom: "15px",
  },
  reports: { color: "#ff9800", cursor: "pointer" , fontSize:"20px"},
    header1: {
    backgroundColor: "#11398f",
    color: "#ffffff",
    padding: "1.2rem 2rem",
    fontSize: "1.5rem",
    fontWeight: "bold",
    textAlign: "center",
    position: "relative",
    marginTop: "-8px",
  },
  pageContainer: {
    minHeight: "100vh",
    background: "linear-gradient(135deg,rgb(255, 255, 255) 0%, #e6f0ff 100%)",
    padding: "30px 15px",
    fontFamily: "'Segoe UI', Tahoma, Geneva, Verdana, sans-serif",
    display: "flex",
    justifyContent: "center",
    alignItems: "flex-start",
  },
  backButton: {
    position: "fixed",
    top: 70,
    left: 20,
    backgroundColor: "#3366ff",
    color: "white",
    border: "none",
    padding: "10px 18px",
    borderRadius: "8px",
    cursor: "pointer",
    fontWeight: "600",
    boxShadow: "0 4px 8px rgba(51, 102, 255, 0.5)",
    transition: "background-color 0.3s ease",
    zIndex: 10,
    userSelect: "none",
  },
  profileCard: {
    backgroundColor: "white",
    borderRadius: "18px",
    padding: "35px 45px",
    width: "100%",
    maxWidth: "520px",
    boxShadow: "0 16px 40px rgba(51, 102, 255, 0.15)",
    display: "flex",
    flexDirection: "column",
    gap: "28px",
  },
  headerSection: {
    display: "flex",
    alignItems: "center",
    gap: "25px",
    borderBottom: "2px solid #e0e7ff",
    paddingBottom: "20px",
  },
  profileImage: {
    width: "140px",
    height: "140px",
    borderRadius: "50%",
    objectFit: "cover",
    border: "4px solid #3366ff",
    boxShadow: "0 4px 20px rgba(51, 102, 255, 0.3)",
  },
  profilePlaceholder: {
    width: "140px",
    height: "140px",
    borderRadius: "50%",
    backgroundColor: "#3366ff",
    color: "white",
    fontSize: "52px",
    fontWeight: "700",
    lineHeight: "140px",
    textAlign: "center",
    userSelect: "none",
  },
  name: {
    margin: 0,
    color: "#003399",
    fontWeight: "800",
    fontSize: "2.4rem",
    lineHeight: 1,
  },
  userRole: {
    margin: "8px 0 0",
    fontWeight: "600",
    fontSize: "1.1rem",
    color: "#3366ff",
  },
  section: {
    display: "flex",
    flexDirection: "column",
    gap: "14px",
  },
  sectionTitle: {
    fontSize: "1.4rem",
    fontWeight: "700",
    color: "#2244cc",
    borderBottom: "2px solid #3366ff",
    paddingBottom: "6px",
    marginBottom: "12px",
    userSelect: "none",
  },
  infoRow: {
    display: "grid",
    gridTemplateColumns: "30px 120px auto",
    alignItems: "center",
    padding: "8px 12px",
    borderRadius: "8px",
    backgroundColor: "#f0f4ff",
    fontSize: "1.1rem",
    color: "#1a1a1a",
    fontWeight: "500",
    userSelect: "text",
    transition: "background-color 0.2s ease",
  },
  icon: {
    fontSize: "1.3rem",
    color: "#3366ff",
    justifySelf: "center",
  },
  label: {
    fontWeight: "700",
    color: "#2244cc",
  },
  value: {
    textAlign: "right",
    userSelect: "text",
  },
  trustBarBackground: {
    width: "100%",
    height: "16px",
    backgroundColor: "#d1dbff",
    borderRadius: "10px",
    overflow: "hidden",
  },
  trustBarFill: {
    height: "100%",
    borderRadius: "10px 0 0 10px",
    transition: "width 0.8s ease, background-color 0.5s ease",
  },
  trustScoreLabel: {
    textAlign: "right",
    fontWeight: "600",
    marginTop: "6px",
    fontSize: "1rem",
  },
};

export default AllUserProfile;
