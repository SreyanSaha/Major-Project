import React, { useState , useEffect} from "react";
import { useNavigate } from "react-router-dom";
import LoadingOverlay from "../../loadingComponents/Loading";
import axios from "axios";
import EditCampaign from "./EditCampaignComponent";

export default function UserCampaignPostsComponent() {
  const navigate = useNavigate();
  const [authenticated, setAuthenticated] = useState(false);
  const [processing, setProcessing] = useState(false);
  const [campaigns, setCampaigns] = useState([]);
  const [msg,updateMsg] = useState(null);
  const [deleteConfirmId, setDeleteConfirmId] = useState(null);
  const [editing, setEditing] = useState(false);
  const [campaignId, setCampaignId] = useState(-1);

  const fetchUserCampaigns=async()=>{
    try{
      const user = JSON.parse(localStorage.getItem("user"));
      if(campaigns.length===0){
        setProcessing(true);
        const response=await axios.get("http://localhost:8080/campaign/user/all-campaigns",
          {
            headers:{
                "Authorization": "Bearer "+user.token,
                "Content-Type": "application/json"
            }
          }
        );
        if(response.status===200){
          setProcessing(false);
          updateMsg(response.data);
        }else if(response.status===202){
          setProcessing(false);
          setCampaigns(response.data);
        }
    }
    }catch(exception){
      console.log(exception);
      navigate("/user/login");
    }
  };

  const refreshCampaigns= async ()=>{
    try{
      const user = JSON.parse(localStorage.getItem("user"));
      setProcessing(true);
      const response=await axios.get("http://localhost:8080/campaign/user/all-campaigns",
        {
          headers:{
              "Authorization": "Bearer "+user.token,
              "Content-Type": "application/json"
          }
        }
      );
      if(response.status===200){
        setProcessing(false);
        updateMsg(response.data);
      }else if(response.status===202){
        setProcessing(false);
        setCampaigns(response.data);
      }
    
    }catch(exception){
      console.log(exception);
      navigate("/user/login");
    }
  };

  useEffect(() => {
      try {
        const user = JSON.parse(localStorage.getItem("user"));
        if (user?.username && user?.role === 0) {setAuthenticated(true);fetchUserCampaigns();}
        else navigate("/user/login");
      } catch (err) {
        console.error("Error parsing user from localStorage:", err);
        navigate("/user/login");
      }
    }, [navigate]);

  const handleDelete= async (id) => {
    try{
      setProcessing(true);
      const user = JSON.parse(localStorage.getItem("user"));
        const response=await axios.delete(`http://localhost:8080/campaign/delete/${id}`,
          {
            headers:{
                "Authorization": "Bearer "+user.token,
                "Content-Type": "application/json"
            }
          }
        );
        if(response.status===200 && response.data.object){
        const newPosts = campaigns.filter(
          (item) => item.campaignId !== Number(id));
        setCampaigns([...newPosts]);
        updateMsg(response.data.msg);
        setProcessing(false);
      }else if(response.status===202){
        setProcessing(false);
        updateMsg(response.data.msg);
      }
    }catch(exception){
      console.log(exception);
      navigate("/user/login");
    }
  };

  const completeCampaign= async(id)=>{
    try{
      setProcessing(true);
      const user = JSON.parse(localStorage.getItem("user"));
        const response=await axios.post("http://localhost:8080/campaign/complete",id,
          {
            headers:{
                "Authorization": "Bearer "+user.token,
                "Content-Type": "application/json"
            }
          }
        );
        if(response.status===200 && response.data.object){
        updateMsg(response.data.msg);
        refreshCampaigns();
        setProcessing(false);
      }else if(response.status===202){
        setProcessing(false);
        updateMsg(response.data.msg);
      }
    }catch(exception){
      console.log(exception);
      navigate("/user/login");
    }
  };

  const styles = {
    alertDiv:{
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
    cardGrid: {
      display: "grid",
      gridTemplateColumns: "repeat(auto-fit, minmax(250px, 1fr))",
      gap: "1.5rem",
      marginTop: "1rem",
    },
    card: {
      position: "relative",
      width:"300px",
      height: "auto",
      backgroundColor: "#ffffff",
      padding: "1rem",
      borderRadius: "10px",
      border: "1px solid #dce4f1",
      display: "flex",
      flexDirection: "column",
      gap: "0.75rem",
      boxShadow: "0 2px 5px rgba(0,0,0,0.05)",
    },
    imagePlaceholder: {
      backgroundColor: "#d2f0d2",
      height: "180px",
      borderRadius: "8px",
    },
    title: {
      fontSize: "1rem",
      fontWeight: "600",
      color: "#2d7a2d",
    },
    desc: {
      fontSize: "0.9rem",
      color: "#555",
      overflow: "hidden"
    },
    actions: {
      display: "flex",
      justifyContent: "space-between",
      marginTop: "0.5rem",
    },
    button: {
      padding: "0.4rem 1rem",
      borderRadius: "6px",
      fontSize: "0.9rem",
      cursor: "pointer",
      border: "none",
    },
    editBtn: {
      backgroundColor: "#f0ad4e",
      color: "white",
    },
    deleteBtn: {
      backgroundColor: "#d9534f",
      color: "white",
    },
    confirmOverlay: {
      position: "absolute",
      top: 0,
      left: 0,
      right: 0,
      bottom: 0,
      backgroundColor: "rgba(255,255,255,0.95)",
      display: "flex",
      flexDirection: "column",
      justifyContent: "center",
      alignItems: "center",
      borderRadius: "10px",
      zIndex: 10,
    },
    confirmBtn: {
      margin: "0.3rem",
      padding: "0.4rem 1rem",
      borderRadius: "6px",
      fontSize: "0.9rem",
      cursor: "pointer",
      border: "none",
    },
    confirmYes: {
      backgroundColor: "#d9534f",
      color: "white",
    },
    confirmNo: {
      backgroundColor: "#5bc0de",
      color: "white",
    },
  };

  return (
    !editing?(
      <div>
      {processing?<LoadingOverlay/>:""}
      <h2 style={{ textAlign: "center", color: "#2d7a2d", marginBottom: "1rem" }}>
        Your Campaign Posts
      </h2>
      <button
        onClick={refreshCampaigns}
        style={{
          margin: "0 auto 1rem auto",
          display: "block",
          padding: "0.5rem 1.2rem",
          backgroundColor: "#2d7a2d",
          color: "white",
          border: "none",
          borderRadius: "6px",
          fontSize: "0.95rem",
          cursor: "pointer",
        }}
      >
        🔄 Refresh Feed
      </button>
      {msg!=null?(<div style={styles.alertDiv}>
            <h3 style={styles.alertText}>{msg}</h3>
          </div>):""}
      <div style={styles.cardGrid}>
        {campaigns.map((item) => (
          <div key={item.campaignId} style={styles.card}>
            {deleteConfirmId === item.campaignId && (
              <div style={styles.confirmOverlay}>
                <p style={{ marginBottom: "0.5rem", fontWeight: "600" }}>Confirm Delete?</p>
                <div>
                  <button
                    style={{ ...styles.confirmBtn, ...styles.confirmYes }}
                    onClick={() => handleDelete(item.campaignId)}
                  >
                    Yes
                  </button>
                  <button
                    style={{ ...styles.confirmBtn, ...styles.confirmNo }}
                    onClick={() => setDeleteConfirmId(null)}
                  >
                    No
                  </button>
                </div>
              </div>
            )}
            <div style={styles.imagePlaceholder} onClick={()=>navigate(`/campaign/${item.campaignId}`)}><img style={{ width: "100%", height: "180px", objectFit: "contain" }} src={`http://localhost:8080/media${item.imagePath1.replace("\\", "/")}`}/></div>
            <div style={styles.title} onClick={()=>navigate(`/campaign/${item.campaignId}`)}>{item.campaignTitle}</div>
            <div style={styles.desc} onClick={()=>navigate(`/campaign/${item.campaignId}`)}>{item.campaignDescription.length>120?
                                                                                             item.campaignDescription.substring(0, 120) + "...":
                                                                                             item.campaignDescription}</div>

            <div
              style={{
                ...styles.statusBadge,
                backgroundColor:
                item.status === 0
                ? "#d4edda"
                : item.status === 1
                ? "#cce5ff"
                : "#f8d7da",
                color:
                  item.status === 0
                  ? "#155724"
                  : item.status === 1
                  ? "#004085"
                  : "#721c24",
                  padding: "0.3rem 0.5rem",
                  borderRadius: "5px",
                  textAlign: "center",
                  fontWeight: "600",
              }}
            >
          {item.status === 0
            ? "Approved"
            : item.status === 1
            ? "Completed"
            : "Not Approved"}
            </div>

            <div style={styles.actions}>
              <button style={{ ...styles.button, ...styles.editBtn }} onClick={()=>{setEditing(true); setCampaignId(item.campaignId);}}>Edit</button>
              {item.status === 0?(<button style={{ ...styles.button, ...styles.editBtn }} onClick={()=>{completeCampaign(item.campaignId);}}>Complete</button>):null}
              <button
                style={{ ...styles.button, ...styles.deleteBtn }}
                onClick={() => setDeleteConfirmId(item.campaignId)}
              >
                Delete
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
    ):(<EditCampaign onBack={()=>{setEditing(false); refreshCampaigns();}} campaignId={campaignId}/>)
  );
}
