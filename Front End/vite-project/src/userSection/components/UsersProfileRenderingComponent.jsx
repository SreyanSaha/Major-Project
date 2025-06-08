import React, { useState , useEffect} from "react";
import { useParams } from 'react-router-dom';
import { useNavigate } from "react-router-dom";
import axios from "axios";
import LoadingOverlay from "../../loadingComponents/Loading";

const getTrustScoreLabel = (score) => {
  if (score >= 0 && score <= 299) return "Low Trust 🔴";
  if (score >= 300 && score <= 599) return "Moderate Trust 🟠";
  if (score >= 600 && score <= 849) return "High Trust 🟡";
  if (score >= 850 && score <= 1000) return "Trusted Citizen 🟢";
  return "";
};

const getTrustScoreStyle = (score) => {
  if (score >= 0 && score <= 299) return { color: "#f44336" };     // Red
  if (score >= 300 && score <= 599) return { color: "#ff9800" };   // Orange
  if (score >= 600 && score <= 849) return { color: "#ffc107" };   // Yellow
  if (score >= 850 && score <= 1000) return { color: "#4caf50" };  // Green
  return {};
};

export default function UserProfileCard({onBack, searchString}) {
  const navigate = useNavigate();
  const [users, setUsers] = useState([]);
  const [authenticated, setAuthenticated] = useState(false);
  const [msg,updateMsg] = useState(null);
  const [processing, setProcessing] = useState(false);
  const [lastPage, setLastPage] = useState(false);
  const [userPage, setUserPage]  = useState(0);
  const [searchTerm, setSearchTerm] = useState(searchString);
  const pageSize=15;

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
      loadProfiles();
    } else{
      setAuthenticated(false);
      navigate("/user/login");
    }
    }, [navigate]);

  const loadProfiles = async ()=>{
    try{
        const user = JSON.parse(localStorage.getItem("user"));
        setProcessing(true);
        const response=await axios.get(`http://localhost:8080/user/search/${searchString}/page/${userPage}/size/${pageSize}`,
          {
            headers:{
              "Authorization": "Bearer "+user.token,
              "Content-Type": "application/json"
            }
          }
        );
        if(response.status===200){
          setUsers(response.data.object.content);
          setLastPage(response.data.object.last);
          updateMsg(response.data.object.last?"No more users are available.":null);
          setUserPage(!response.data.object.last?userPage+1:userPage);
          setProcessing(false);
        }else if(response.status===202){
          setProcessing(false);
          updateMsg(response.data.msg);
        }
    }catch(exception){
      console.log(exception);
      setProcessing(false);
      updateMsg(exception?.response?.data?.msg || exception.message );
    }
  };

  const loadMoreProfiles = async()=>{
    try{
        if(!lastPage){
            const user = JSON.parse(localStorage.getItem("user"));
            setProcessing(true);
            const response=await axios.get(`http://localhost:8080/user/search/${searchTerm}/page/${userPage}/size/${pageSize}`,
              {
                headers:{
                  "Authorization": "Bearer "+user.token,
                  "Content-Type": "application/json"
                }
              }
            );
            if(response.status===200){
              setUsers(users.length===0?
                response.data.object.content
                :prevUsers=>[...prevUsers, ...response.data.object.content]);
              setLastPage(response.data.object.last);
              updateMsg(response.data.object.last?"No more users are available.":null);
              setUserPage(!response.data.object.last?userPage+1:userPage);
              setProcessing(false);
            }else if(response.status===202){
              setProcessing(false);
              updateMsg(response.data.msg);
            }
        }
    }catch(exception){
      console.log(exception);
      setProcessing(false);
      updateMsg(exception?.response?.data?.msg || exception.message );
    }
  };

  const searchProfile = async()=>{
    setUserPage(0);
    const page=0;
    try{
        const user = JSON.parse(localStorage.getItem("user"));
        setProcessing(true);
        const response=await axios.get(`http://localhost:8080/user/search/${searchTerm}/page/${page}/size/${pageSize}`,
          {
            headers:{
              "Authorization": "Bearer "+user.token,
              "Content-Type": "application/json"
            }
          }
        );
        if(response.status===200){
          setUsers(response.data.object.content);
          setLastPage(response.data.object.last);
          updateMsg(response.data.object.last?"No more users are available.":null);
          setUserPage(!response.data.object.last?userPage+1:userPage);
          setProcessing(false);
        }else if(response.status===202){
          setProcessing(false);
          updateMsg(response.data.msg);
        }
    }catch(exception){
      console.log(exception);
      setProcessing(false);
      updateMsg(exception?.response?.data?.msg || exception.message );
    }
  };

  const styles = {
    searchBoxWrapper: {
      marginTop: "1rem",
      marginBottom:"20px",
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
    addCommentBtn1: {
    backgroundColor: "#11398f",
    color: "#fff",
    width: "100%",
    border: "none",
    borderRadius: "6px",
    padding: "0.4rem 0.9rem",
    cursor: "pointer",
    marginTop: "0.3rem",
  },
    container: {
      padding: "20px",
    },
    backArrow: {
        left:-100,
      cursor: "pointer",
      fontSize: "1.5rem",
      color: "#004080",
      fontWeight: "bold",
      backgroundColor: "#e3f2fd",
      padding: "0.4rem 0.8rem",
      borderRadius: "6px",
      boxShadow: "0 2px 5px rgba(0, 0, 0, 0.1)",
      width: "fit-content",
      marginBottom: "2px",
    },
    grid: {
      display: "grid",
      gridTemplateColumns: "repeat(auto-fill, minmax(300px, 1fr))",
      gap: "20px",
    },
    card: {
      backgroundColor: "#ffffff",
      border: "1px solid #cce4ff",
      borderRadius: "12px",
      padding: "20px",
      boxShadow: "0 4px 10px rgba(0, 87, 184, 0.08)",
      cursor: "pointer",
      transition: "transform 0.25s ease, box-shadow 0.25s ease",
    },
    header: {
      display: "flex",
      alignItems: "center",
      gap: "14px",
      marginBottom: "12px",
    },
    profileImage: {
      width: "56px",
      height: "56px",
      borderRadius: "50%",
      objectFit: "cover",
      border: "2.5px solid #004aad",
    },
    profileName: {
      fontWeight: "700",
      fontSize: "1.1rem",
      color: "#004aad",
    },
    trustScoreLabel: {
      fontWeight: "500",
      fontSize: "0.85rem",
      marginTop: "2px",
    },
    divider: {
      height: "1px",
      backgroundColor: "#e3f1ff",
      margin: "12px 0",
    },
    stats: {
      display: "flex",
      flexDirection: "column",
      gap: "8px",
      fontSize: "0.95rem",
      color: "#11398f",
    },
    statRow: {
      display: "flex",
      alignItems: "center",
      gap: "8px",
    },
    icon: {
      fontSize: "1.1rem",
    },
  };

  return (
    <div style={styles.container}>
        {processing?<LoadingOverlay/>:""}
        {msg!=null?(<div style={styles.alertDiv}>
            <h3 style={styles.alertText}>{msg}</h3>
          </div>):""}
          <div style={styles.searchBoxWrapper}>
            <div onClick={onBack} style={styles.backArrow}>
        ← Back
      </div>
            <input
              type="text"
              placeholder={"Search for Users..."}
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              style={styles.searchBox}
            />
            <button style={styles.searchButton} onClick={()=>searchProfile()}>
              Search
            </button>
          </div>
      <div style={styles.grid}>
        {users.map((user) => (
          <div key={user.userId} style={styles.card} onClick={()=>navigate(`/profile/${user.userId}`)}>
            <div style={styles.header}>
              <img
                src={user.profileImagePath!==null && user.profileImagePath!==undefined 
                    ?`http://localhost:8080/media${user.profileImagePath.replace("\\", "/")}`
                    :"profile image."}
                alt={user.userFirstName}
                style={styles.profileImage}
              />
              <div>
                <div style={styles.profileName}>{user.userFirstName+" "+user.userLastName}</div>
                <div
                  style={{
                    ...styles.trustScoreLabel,
                    ...getTrustScoreStyle(user.civicTrustScore),
                  }}
                >
                  {getTrustScoreLabel(user.civicTrustScore)}
                </div>
              </div>
            </div>

            <div style={styles.divider} />

            <div style={styles.stats}>
              <div style={styles.statRow}>
                <span style={styles.icon}>📅</span>
                <span>Member since: {new Date(user.signupDateTime).toLocaleString()}</span>
              </div>
              <div style={styles.statRow}>
                <span style={styles.icon}>⭐</span>
                <span>Trust Score: {user.civicTrustScore}</span>
              </div>
              <div style={styles.statRow}>
                <span style={styles.icon}>📝</span>
                <span>Posts: {user.postCount}</span>
              </div>
              <div style={styles.statRow}>
                <span style={styles.icon}>🎯</span>
                <span>Campaigns: {user.campaignCount}</span>
              </div>
            </div>
          </div>
        ))}
      </div>
      <button
        style={{
          marginTop: "85%",
          padding: "0.75rem 3rem",
          fontSize: "1rem",
          backgroundColor: "#11398f",
          color: "white",
          border: "none",
          borderRadius: "8px",
          cursor: "pointer",
          position: "fixed",
          bottom: "30px",
          boxShadow: "0 4px 10px rgba(0,0,0,0.3)",
          zIndex: 1000,
          left: "30px",
        }}
        onClick={()=>loadMoreProfiles()}
      >
        Load More
      </button>
    </div>
  );
}
