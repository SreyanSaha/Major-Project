import React, { useState , useEffect} from "react";
import { useNavigate } from "react-router-dom";
import LoadingOverlay from "../../loadingComponents/Loading";

export default function UserUploadedPostsComponent() {
  const navigate = useNavigate();
  const [authenticated, setAuthenticated] = useState(false);

  const [posts, setPosts] = useState([
    {
      id: 1,
      title: "Clean Water Campaign",
      description: "Promoting clean water access in rural areas.",
      image: "",
    },
    {
      id: 2,
      title: "Emergency Road Block",
      description: "Blocked road near Sector 5, Salt Lake. Needs attention.",
      image: "",
    },
    {
      id: 3,
      title: "Food Distribution Drive",
      description: "Join us in helping the underprivileged this weekend.",
      image: "",
    },
  ]);

  const [deleteConfirmId, setDeleteConfirmId] = useState(null);

  useEffect(() => {
    try {
      const user = JSON.parse(localStorage.getItem("user"));
      console.log("Fetched user:", user);
      if (user?.username && user?.role === 0) setAuthenticated(true);
      else navigate("/user/login");
    } catch (err) {
      console.error("Error parsing user from localStorage:", err);
      navigate("/user/login");
    }
  }, [navigate]);

  const handleDelete = (id) => {
    setPosts(posts.filter((post) => post.id !== id));
    setDeleteConfirmId(null);
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
      height: "180px",
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
  // <LoadingOverlay/>
  return (
    <div>
      <h2 style={{ textAlign: "center", color: "#11398f", marginBottom: "1rem" }}>
        Your Uploaded Posts
      </h2>
      <div style={styles.cardGrid}>
        {posts.map((post) => (
          <div key={post.id} style={styles.card}>
            {deleteConfirmId === post.id && (
              <div style={styles.confirmOverlay}>
                <p style={{ marginBottom: "0.5rem", fontWeight: "600" }}>Confirm Delete?</p>
                <div>
                  <button
                    style={{ ...styles.confirmBtn, ...styles.confirmYes }}
                    onClick={() => handleDelete(post.id)}
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
            <div style={styles.imagePlaceholder}></div>
            <div style={styles.title}>{post.title}</div>
            <div style={styles.desc}>{post.description}</div>
            <div style={styles.actions}>
              <button style={{ ...styles.button, ...styles.editBtn }}>Edit</button>
              <button
                style={{ ...styles.button, ...styles.deleteBtn }}
                onClick={() => setDeleteConfirmId(post.id)}
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
