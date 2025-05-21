import React, { useState , useEffect} from "react";
import { useNavigate } from "react-router-dom";

const CampaignPostCard = (props) => {
  const navigate = useNavigate();
  const [imageIndex, setImageIndex] = useState(0);
  const [showComments, setShowComments] = useState(false);

  useEffect(() => {
        try {
          const user = JSON.parse(localStorage.getItem("user"));
          console.log("Fetched user:", user);
          if (user?.username && user?.role === 0) {setAuthenticated(true);fetchUserCampaigns();}
          else navigate("/user/login");
        } catch (err) {
          console.error("Error parsing user from localStorage:", err);
          navigate("/user/login");
        }
      }, [navigate]);

  const campaign = {
    campaignTitle: "Beach Cleanup Drive",
    campaignDescription: "Join us this weekend to clean the local beach and make it better for all!",
    organizerName: "Eco Warriors",
    organizerImage: "https://via.placeholder.com/100x100.png?text=Organizer",
    images: [
      "https://via.placeholder.com/600x300.png?text=Campaign+Image+1",
      "https://via.placeholder.com/600x300.png?text=Campaign+Image+2",
      "https://via.placeholder.com/600x300.png?text=Campaign+Image+3"
    ],
    paymentQr: "https://via.placeholder.com/150x150.png?text=Payment+QR"
  };

  const nextImage = () => {
    setImageIndex((prev) => (prev + 1) % campaign.images.length);
  };

  const prevImage = () => {
    setImageIndex((prev) => (prev - 1 + campaign.images.length) % campaign.images.length);
  };

  const styles = {
    card: {
      backgroundColor: "#ffffff",
      border: "1px solid #cce0ff",
      borderRadius: "12px",
      padding: "1rem",
      maxWidth: "600px",
      margin: "2rem auto",
      boxShadow: "0 2px 10px rgba(0, 0, 255, 0.1)",
      fontFamily: "Arial, sans-serif",
    },
    header: {
      display: "flex",
      alignItems: "center",
      marginBottom: "1rem",
    },
    organizerImage: {
      width: "50px",
      height: "50px",
      borderRadius: "50%",
      marginRight: "1rem",
      border: "2px solid #007bff",
      objectFit: "cover",
    },
    title: {
      fontSize: "1.3rem",
      fontWeight: "bold",
      color: "#004080",
    },
    description: {
      margin: "0.5rem 0",
      color: "#333",
    },
    imageContainer: {
      position: "relative",
      height: "300px",
      marginBottom: "1rem",
    },
    campaignImage: {
      width: "100%",
      height: "100%",
      objectFit: "cover",
      borderRadius: "10px",
    },
    arrow: {
      position: "absolute",
      top: "50%",
      transform: "translateY(-50%)",
      backgroundColor: "#007bff",
      color: "#fff",
      border: "none",
      borderRadius: "50%",
      width: "35px",
      height: "35px",
      fontSize: "1.2rem",
      cursor: "pointer",
      zIndex: 2,
    },
    leftArrow: {
      left: "10px",
    },
    rightArrow: {
      right: "10px",
    },
    qrImage: {
      width: "150px",
      height: "150px",
      objectFit: "contain",
      border: "1px solid #cce0ff",
      borderRadius: "8px",
      marginTop: "1rem",
    },
    commentToggle: {
      marginTop: "1rem",
      backgroundColor: "#007bff",
      color: "#fff",
      border: "none",
      borderRadius: "6px",
      padding: "0.5rem 1rem",
      cursor: "pointer",
    },
    commentSection: {
      marginTop: "1rem",
      padding: "0.75rem",
      border: "1px solid #cce0ff",
      borderRadius: "8px",
      backgroundColor: "#f0f8ff",
    },
  };

  return (
    <div style={styles.card}>
      {/* Organizer Info */}
      <div style={styles.header}>
        <img
          src={campaign.organizerImage}
          alt="Organizer"
          style={styles.organizerImage}
        />
        <div>
          <div style={styles.title}>{campaign.campaignTitle}</div>
          <div style={{ color: "#555" }}>By: {campaign.organizerName}</div>
        </div>
      </div>

      {/* Image Carousel */}
      <div style={styles.imageContainer}>
        <img
          src={campaign.images[imageIndex]}
          alt="Campaign"
          style={styles.campaignImage}
        />
        <button style={{ ...styles.arrow, ...styles.leftArrow }} onClick={prevImage}>
          ‹
        </button>
        <button style={{ ...styles.arrow, ...styles.rightArrow }} onClick={nextImage}>
          ›
        </button>
      </div>

      {/* Description */}
      <p style={styles.description}>{campaign.campaignDescription}</p>

      {/* Payment QR */}
      <div>
        <h4 style={{ color: "#004080" }}>Payment QR</h4>
        <img
          src={campaign.paymentQr}
          alt="Payment QR"
          style={styles.qrImage}
        />
      </div>

      {/* Comment Section */}
      <button style={styles.commentToggle} onClick={() => setShowComments(!showComments)}>
        {showComments ? "Hide Comments" : "Show Comments 💬"}
      </button>
      {showComments && (
        <div style={styles.commentSection}>
          <p><strong>Comments:</strong></p>
          <p>👍 Great initiative!</p>
          <p>🙌 Count me in!</p>
          <p>💡 Maybe include a cleanup checklist?</p>
        </div>
      )}
    </div>
  );
};

export default CampaignPostCard;
