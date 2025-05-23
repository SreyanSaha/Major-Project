import React, { useState , useEffect} from "react";
import { useNavigate } from "react-router-dom";
import LoadingOverlay from "../../loadingComponents/Loading";

export default function UserEmergencyPostsComponent() {
  const navigate = useNavigate();
  const [authenticated, setAuthenticated] = useState(false);
  const [emergencies, setEmergencies] = useState([
    {
      id: 1,
      title: "Fire in Local Market",
      description: "Urgent help needed! Fire broke out near Lake Town Market.",
      image: "",
    },
    {
      id: 2,
      title: "Accident on Bypass",
      description: "Severe accident with multiple injured. Need ambulance.",
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
    setEmergencies(emergencies.filter((item) => item.id !== id));
    setDeleteConfirmId(null);
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
      maxWidth:"400px",
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
      backgroundColor: "#ffcccc",
      height: "180px",
      borderRadius: "8px",
    },
    title: {
      fontSize: "1rem",
      fontWeight: "600",
      color: "#a94442",
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

  return (
    <div>
      <h2 style={{ textAlign: "center", color: "#a94442", marginBottom: "1rem" }}>
        Your Emergency Posts
      </h2>
      <div style={styles.cardGrid}>
        {emergencies.map((item) => (
          <div key={item.id} style={styles.card}>
            {deleteConfirmId === item.id && (
              <div style={styles.confirmOverlay}>
                <p style={{ marginBottom: "0.5rem", fontWeight: "600" }}>Confirm Delete?</p>
                <div>
                  <button
                    style={{ ...styles.confirmBtn, ...styles.confirmYes }}
                    onClick={() => handleDelete(item.id)}
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
            <div style={styles.title}>{item.title}</div>
            <div style={styles.desc}>{item.description}</div>
            <div style={styles.actions}>
              <button style={{ ...styles.button, ...styles.editBtn }}>Edit</button>
              <button
                style={{ ...styles.button, ...styles.deleteBtn }}
                onClick={() => setDeleteConfirmId(item.id)}
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
