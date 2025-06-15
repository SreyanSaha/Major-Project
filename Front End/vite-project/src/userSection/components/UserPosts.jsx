import React, { useState , useEffect} from "react";
import { useNavigate } from "react-router-dom";
import LoadingOverlay from "../../loadingComponents/Loading";
import axios from "axios";
import UpdatePostForm from "./EditPostComponent";

export default function UserUploadedPostsComponent() {
  const navigate = useNavigate();
  const [msg,updateMsg] = useState(null);
  const [authenticated, setAuthenticated] = useState(false);
  const [posts, setPosts] = useState([]);
  const [processing, setProcessing] = useState(false);
  const [deleteConfirmId, setDeleteConfirmId] = useState(null);
  const [editing, setEditing] = useState(false);
  const [postId, setPostId] = useState(-1);

  useEffect(() => {
    try {
      const user = JSON.parse(localStorage.getItem("user"));
      console.log("Fetched user:", user);
      if (user?.username && user?.role === 0) {setAuthenticated(true);fetchUserPosts();}
      else navigate("/user/login");
    } catch (err) {
      console.error("Error parsing user from localStorage:", err);
      navigate("/user/login");
    }
  }, [navigate]);

  const handleDelete = async (id) => {
    try{
      setProcessing(true);
      const user = JSON.parse(localStorage.getItem("user"));
        const response=await axios.delete(`http://localhost:8080/post/delete/${id}`,
          {
            headers:{
                "Authorization": "Bearer "+user.token,
                "Content-Type": "application/json"
            }
          }
        );
        if(response.status===200 && response.data.object){
        const newPosts = posts.filter(
          (item) => item.postId !== Number(id));
        setPosts([...newPosts]);
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

  const fetchUserPosts=async()=>{
    try{
      setProcessing(true);
      const user = JSON.parse(localStorage.getItem("user"));
      if(posts.length===0){
        const response=await axios.get("http://localhost:8080/post/user/all-posts",
          {
            headers:{
                "Authorization": "Bearer "+user.token,
                "Content-Type": "application/json"
            }
          }
        );
        if(response.status===200){
          setProcessing(false);
          setPosts(response.data.objects);
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

  const refreshPosts= async ()=>{
    try{
      const user = JSON.parse(localStorage.getItem("user"));
      setProcessing(true);
      const response=await axios.get("http://localhost:8080/post/user/all-posts",
        {
          headers:{
              "Authorization": "Bearer "+user.token,
              "Content-Type": "application/json"
          }
        }
      );
      if(response.status===200){
        setProcessing(false);
        setPosts(response.data.objects);
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
      gridTemplateColumns: "repeat(auto-fit, minmax(350px, 1fr))",
      gap: "1.5rem",
      marginTop: "1rem",
    },
    card: {
      position: "relative",
      maxWidth:"550px",
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
      color: "#11398f",
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
    !editing?(
      <div>
      {processing?<LoadingOverlay/>:""}
      <h2 style={{ textAlign: "center", color: "#11398f", marginBottom: "1rem" }}>
        Your Uploaded Posts
      </h2>
      <button
        onClick={refreshPosts}
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
        {posts.map((post) => (
          <div key={post.postId} style={styles.card}>
            {deleteConfirmId === post.postId && (
              <div style={styles.confirmOverlay}>
                <p style={{ marginBottom: "0.5rem", fontWeight: "600" }}>Confirm Delete?</p>
                <div>
                  <button
                    style={{ ...styles.confirmBtn, ...styles.confirmYes }}
                    onClick={() => handleDelete(post.postId)}
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
            <div style={styles.imagePlaceholder} onClick={()=>navigate(`/post/${post.postId}`)}><img src={`http://localhost:8080/media${post.imagePath1.replace("\\", "/")}`}
            style={{
            height: "100%",
            objectFit: "cover",
            borderRadius: "8px",
            flex: 1,
          }}/></div>
            <div style={styles.title} onClick={()=>navigate(`/post/${post.postId}`)}>{post.postTitle}</div>
            <div style={styles.desc} onClick={()=>navigate(`/post/${post.postId}`)}>{post.postDescription.length > 120?
                                  post.postDescription.substring(0, 120) + "..."
                                  :post.postDescription}</div>
            <div
              style={{
                fontWeight: "bold",
                color:
                  post.postStatus === 1
                    ? "green"
                    : post.postStatus === 0
                    ? "#e67e22"
                    : "#d9534f",
                backgroundColor:
                  post.postStatus === 1
                    ? "#dff0d8"
                    : post.postStatus === 0
                    ? "#fcf8e3"
                    : "#f2dede",
                padding: "0.3rem 0.6rem",
                borderRadius: "5px",
                display: "inline-block",
                fontSize: "0.85rem",
              }}
            >
              {post.postStatus === 1
                ? "Completed"
                : post.postStatus === 0
                ? "Work in Progress"
                : "Under Review"}
            </div>
            <div style={styles.actions}>
              <button style={{ ...styles.button, ...styles.editBtn }} onClick={()=>{setEditing(true); setPostId(post.postId);}}>Edit</button>
              <button
                style={{ ...styles.button, ...styles.deleteBtn }}
                onClick={() => setDeleteConfirmId(post.postId)}
              >
                Delete
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
    ):(<UpdatePostForm onBack={()=>{setEditing(false); refreshPosts();}} postId={postId}/>)
  );
}
