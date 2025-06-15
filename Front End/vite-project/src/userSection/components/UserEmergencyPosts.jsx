import React, { useState , useEffect} from "react";
import { useNavigate } from "react-router-dom";
import LoadingOverlay from "../../loadingComponents/Loading";
import axios from "axios";

export default function UserEmergencyPostsComponent() {
  const navigate = useNavigate();
    const [msg,updateMsg] = useState(null);
    const [authenticated, setAuthenticated] = useState(false);
    const [emergencyPosts, setEmergencyPosts] = useState([]);
    const [processing, setProcessing] = useState(false);
    const [deleteConfirmId, setDeleteConfirmId] = useState(null);

  useEffect(() => {
      try {
        const user = JSON.parse(localStorage.getItem("user"));
        console.log("Fetched user:", user);
        if (user?.username && user?.role === 0){ setAuthenticated(true); fetchUserEmergencyPost();}
        else navigate("/user/login");
      } catch (err) {
        console.error("Error parsing user from localStorage:", err);
        navigate("/user/login");
      }
    }, [navigate]);

  const refreshEmergencyPosts= async ()=>{
    try{
      const user = JSON.parse(localStorage.getItem("user"));
      setProcessing(true);
      const response=await axios.get("http://localhost:8080/emergency-post/user/all",
        {
          headers:{
              "Authorization": "Bearer "+user.token,
              "Content-Type": "application/json"
          }
        }
      );
      if(response.status===200){
        setProcessing(false);
        setEmergencyPosts(response.data.objects);
      }else if(response.status===202){
        setProcessing(false);
        updateMsg(response.data.msg);
      }
    }catch(exception){
      console.log(exception);
      navigate("/user/login");
    }
  };

  const fetchUserEmergencyPost = async()=>{
    try{
      const user = JSON.parse(localStorage.getItem("user"));
      if(emergencyPosts.length===0){
        setProcessing(true);
        const response=await axios.get("http://localhost:8080/emergency-post/user/all",
          {
            headers:{
                "Authorization": "Bearer "+user.token,
                "Content-Type": "application/json"
            }
          }
        );
        if(response.status===200){
          setEmergencyPosts(response.data.objects);
          setProcessing(false);
        }else if(response.status===202){
          setProcessing(false);
          updateMsg(response.data.msg);
        }
      }
    }catch(exception){
      console.log(exception);
      navigate("/user/login");
    }
  };

  const resolveEmergency = async(id)=>{
    try{
      const user = JSON.parse(localStorage.getItem("user"));
      setProcessing(true);
      const response=await axios.post("http://localhost:8080/emergency-post/user/resolve",id,
        {
          headers:{
              "Authorization": "Bearer "+user.token,
              "Content-Type": "application/json"
          }
        }
      );
      if(response.status===200){
        if(emergencyPosts.length==0)setEmergencyPosts(response.data.objects);
        else setEmergencyPosts(
          emergencyPosts.map(emergencyPost =>
        emergencyPost.emergencyPostId === response.data.object.emergencyPostId?
        response.data.object:emergencyPost
        ));
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

  const deleteEmergency = async(id)=>{
    setDeleteConfirmId(null);
    try{
      const user = JSON.parse(localStorage.getItem("user"));
      setProcessing(true);
      const response=await axios.delete(`http://localhost:8080/emergency-post/delete/${id}`,
        {
          headers:{
              "Authorization": "Bearer "+user.token,
              "Content-Type": "application/json"
          }
        }
      );
      if(response.status===200 && response.data.object){
        const newPosts = emergencyPosts.filter(
          (item) => item.emergencyPostId !== Number(id));
        setEmergencyPosts([...newPosts]);
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

  const styles = {
    cardGrid: {
      display: "grid",
      gridTemplateColumns: "repeat(auto-fit, minmax(150px, 1fr))",
      gap: "1.5rem",
      marginTop: "1rem",
    },
    card: {
      position: "relative",
      width:"400px",
      height: "auto",
      backgroundColor: "#fff8f8",
      padding: "1rem",
      borderRadius: "10px",
      border: "1px solid #f5c6cb",
      display: "flex",
      flexDirection: "column",
      gap: "0.75rem",
      boxShadow: "0 2px 5px rgba(0,0,0,0.05)",
    },
    imagePlaceholder: {
    backgroundColor: "#cce0ff",
    height: "190px",
    borderRadius: "8px",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    gap: "0.5rem",
    overflow: "hidden",
    },
    title: {
      fontSize: "1rem",
      fontWeight: "600",
      color: "#a94442",
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
  if (!authenticated) return null;
  return (
    <div>
      {processing?<LoadingOverlay/>:""}
      <h2 style={{ textAlign: "center", color: "#a94442", marginBottom: "1rem" }}>
        Your Emergency Posts
      </h2>
      <button
        onClick={refreshEmergencyPosts}
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
        {emergencyPosts.map((item) => (
          <div key={item.emergencyPostId} style={styles.card} >
            {deleteConfirmId === item.emergencyPostId && (
              <div style={styles.confirmOverlay}>
                <p style={{ marginBottom: "0.5rem", fontWeight: "600" }}>Confirm Delete?</p>
                <div>
                  <button
                    style={{ ...styles.confirmBtn, ...styles.confirmYes }}
                    onClick={() => {deleteEmergency(item.emergencyPostId);}}
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
            <div style={styles.imagePlaceholder} onClick={() => navigate(`/emergency/${item.emergencyPostId}`)}><img src={item.imagePath1!==null & item.imagePath1!==undefined?
            `http://localhost:8080/media${item.imagePath1.replace("\\", "/")}`:"Image"}
            style={{
            height: "100%",
            objectFit: "cover",
            borderRadius: "8px",
            flex: 1,
          }}/></div>
            <div style={styles.title} onClick={() => navigate(`/emergency/${item.emergencyPostId}`)}>{item.emergencyPostTitle}</div>
            <div style={styles.desc} onClick={() => navigate(`/emergency/${item.emergencyPostId}`)}>{item.emergencyPostDescription.length>120?
                                                                                                     item.emergencyPostDescription.substring(0, 120) + "...":
                                                                                                     item.emergencyPostDescription}</div>
            <div
              style={{
                fontWeight: "bold",
                color:
                  item.emergencyPostStatus === 1
                    ? "green"
                    :"#e67e22",
                backgroundColor:
                  item.emergencyPostStatus === 1
                    ? "#dff0d8"
                    : "#fcf8e3",
                padding: "0.3rem 0.6rem",
                borderRadius: "5px",
                display: "inline-block",
                fontSize: "0.85rem",
              }}
            onClick={() => navigate(`/emergency/${item.emergencyPostId}`)}>
              {item.emergencyPostStatus === 1
                ? "Resolved"
                :"Un-resolved"}
            </div>
            <div style={styles.actions}>
              {item.emergencyPostStatus===0?(
                <button style={{ ...styles.button, ...styles.editBtn }} onClick={()=>{resolveEmergency(item.emergencyPostId);}}>Resolve</button>
              ):null}
              <button
                style={{ ...styles.button, ...styles.deleteBtn }}
                onClick={() => setDeleteConfirmId(item.emergencyPostId)}
              >
                Delete
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
