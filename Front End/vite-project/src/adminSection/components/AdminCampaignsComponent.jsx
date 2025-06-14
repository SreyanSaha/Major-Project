import React, { useState, useMemo, useEffect} from 'react';
import { useNavigate } from "react-router-dom";
import LoadingOverlay from '../../loadingComponents/Loading';
import axios from "axios";

const ManageCampaigns = () => {
  const navigate = useNavigate();
  const [msg, updateMsg] = useState(null);
  const [campaigns, setCampaigns] = useState([]);
  const [searchStirng, setSearch] = useState("");

  useEffect(() => {
        try {
          const admin = JSON.parse(localStorage.getItem("admin"));
          if (admin?.username && admin?.token && admin?.role === 1) {
            fetchAllUnApprovedCampaigns();
          } else navigate("/admin/login");
        } catch (err) {
          console.error("Error parsing user from localStorage:", err);
          updateMsg("Failed to fetch your data, please login again.");
          navigate("/admin/login");
        }
      }, []);

  const searchCampaigns= async()=>{
    try {
      const admin = JSON.parse(localStorage.getItem("admin"));
      const response = await axios.get(`http://localhost:8080/admin/search/campaigns/${searchStirng}`, {
        headers: {
          "Authorization": "Bearer " + admin.token,
          "Content-Type": "application/json"
        }
      });
      if (response.status === 200) {
        setCampaigns(response.data.objects);
      } else if (response.status === 202) {
        updateMsg(response.data.msg);
      }
    } catch (exception) {
      console.log(exception);
      navigate("/admin/login");
    }
  };

  const fetchAllUnApprovedCampaigns = async()=>{
    try {
      const admin = JSON.parse(localStorage.getItem("admin"));
      const response = await axios.get("http://localhost:8080/admin/all/un-approved/campaigns", {
        headers: {
          "Authorization": "Bearer " + admin.token,
          "Content-Type": "application/json"
        }
      });
      if (response.status === 200) {
        setCampaigns(response.data.objects);
      } else if (response.status === 202) {
        updateMsg(response.data.msg);
      }
    } catch (exception) {
      console.log(exception);
      navigate("/admin/login");
    }
  };

  const approveCampaign= async (id) =>{
    try {
      const admin = JSON.parse(localStorage.getItem("admin"));
      const response = await axios.post("http://localhost:8080/admin/approve/campaign",id,{
        headers: {
          "Authorization": "Bearer " + admin.token,
          "Content-Type": "application/json"
        }
      });
      if (response.status === 200 && response.data.object) {
        fetchAllUnApprovedCampaigns();
      } else if (response.status === 202) {
        updateMsg(response.data.msg);
      }
    } catch (exception) {
      console.log(exception);
      navigate("/admin/login");
    }
  };

  const deleteCampaign= async (id)=>{
    try {
      const admin = JSON.parse(localStorage.getItem("admin"));
      const response = await axios.post("http://localhost:8080/admin/delete/campaign",id,{
        headers: {
          "Authorization": "Bearer " + admin.token,
          "Content-Type": "application/json"
        }
      });
      if (response.status === 200 && response.data.object) {
        fetchAllUnApprovedCampaigns();
      } else if (response.status === 202) {
        updateMsg(response.data.msg);
      }
    } catch (exception) {
      console.log(exception);
      navigate("/admin/login");
    }
  };

  const rejectCampaign= async(id)=>{
    try {
      const admin = JSON.parse(localStorage.getItem("admin"));
      const response = await axios.post("http://localhost:8080/admin/reject/campaign",id,{
        headers: {
          "Authorization": "Bearer " + admin.token,
          "Content-Type": "application/json"
        }
      });
      if (response.status === 200 && response.data.object) {
        fetchAllUnApprovedCampaigns();
      } else if (response.status === 202) {
        updateMsg(response.data.msg);
      }
    } catch (exception) {
      console.log(exception);
      navigate("/admin/login");
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
  container: {
    padding: "30px",
    backgroundColor: "#ffffff",
    fontFamily: "Arial, sans-serif",
    width: "100%",
    flex: 1,
    overflowY: 'auto',
  },
  heading: {
    fontSize: "28px",
    marginBottom: "20px",
    textAlign: "center",
    color: "#1b3c74",
  },
  searchInput: {
    padding: "10px",
    width: "85%",
    borderRadius: "5px",
    border: "1px solid #ccc",
    marginRight: "10px",
  },
  searchButton: {
    padding: "10px 15px",
    backgroundColor: "#1b3c74",
    color: "#fff",
    border: "none",
    borderRadius: "5px",
    cursor: "pointer",
  },
  cardsContainer: {
    marginTop: "30px",
    display: "grid",
    gridTemplateColumns: "repeat(1, minmax(400px, 1fr))",
    gap: "20px",
  },
  card: {
    padding: "20px",
    backgroundColor: "#f0f8ff",
    borderRadius: "10px",
    boxShadow: "0 4px 12px rgba(0,0,0,0.1)",
    display: "flex",
    flexDirection: "column",
  },
  imageGrid: {
    display: "grid",
    gridTemplateColumns: "repeat(4, minmax(100px, 1fr))",
    gap: "8px",
    marginTop: "10px",
    marginBottom: "10px",
  },
  image: {
    width: "220px",
    height: "100px",
    objectFit: "cover",
    borderRadius: "8px",
    backgroundColor: "#e0e0e0",
  },
  label: {
    fontWeight: "bold",
    color: "#1b3c74",
  },
  smallText: {
    fontSize: "14px",
    marginBottom: "6px",
  },
  sectionDivider: {
    margin: "10px 0",
    borderBottom: "1px solid #ccc",
  },
  actionButtons: {
  display: "flex",
  justifyContent: "space-between",
  marginTop: "15px",
  gap: "10px",
},

button: {
  flex: 1,
  padding: "10px",
  border: "none",
  borderRadius: "8px",
  fontWeight: "bold",
  cursor: "pointer",
  transition: "transform 0.2s ease, background-color 0.3s ease",
},

approveButton: {
  backgroundColor: "#4CAF50",
  color: "#fff",
},

rejectButton: {
  backgroundColor: "#FF9800",
  color: "#fff",
},

deleteButton: {
  backgroundColor: "#f44336",
  color: "#fff",
},
};

  const renderImages = (campaign) => {
    const base = "http://localhost:8080/media";
    const images = [campaign.imagePath1, campaign.imagePath2, campaign.imagePath3, campaign.imagePath4, campaign.imagePath5];

    return images.map((path, i) =>
      path ? <img key={i} src={base + path.replace(/\\/g, "/")} alt="Campaign" style={styles.image} /> : null
    );
  };

  return (
    <div style={styles.container}>
      <h2 style={styles.heading}>Manage Campaigns</h2>
      {msg!=null?(<div style={styles.alertDiv}>
              <h3 style={styles.alertText}>{msg}</h3>
            </div>):""}
      <div>
        <input
          type="text"
          placeholder="Enter Campaign name"
          value={searchStirng}
          onChange={(e) => setSearch(e.target.value)}
          style={styles.searchInput}
        />
        <button style={styles.searchButton} onClick={()=>{searchCampaigns();}}>Search</button>
      </div>

      <div style={styles.cardsContainer}>
        {campaigns.map(c => (
          <div key={c.campaignId} style={styles.card}>
            <h3 style={{ color: "#003366" }}>{c.campaignTitle}</h3>
            <h4 style={{ color: "#003366" }}>{c.campaignDescription}</h4>
            <div style={styles.imageGrid}>
            {renderImages(c)}
            </div>
            <p style={styles.smallText}><span style={styles.label}>Organizer:</span> {c.campaignOrganizerName} ({c.campaignOrganizerEmail})</p>
            <p style={styles.smallText}><span style={styles.label}>Contact:</span> {c.campaignOrganizerContact}</p>

            <div style={styles.sectionDivider}></div>

            <p style={styles.smallText}><span style={styles.label}>Location:</span> {c.street}, {c.city}, {c.state}, {c.country} - {c.postalCode}</p>
            <p style={styles.smallText}><span style={styles.label}>Created:</span> {new Date(c.campaignCreationTime).toLocaleString()}</p>

            <p style={styles.smallText}><span style={styles.label}>Upvotes:</span> {c.upVoteCount} | <span style={styles.label}>Downvotes:</span> {c.downVoteCount}</p>
            <p style={styles.smallText}><span style={styles.label}>Reports:</span> {c.campaignReports}</p>
            <p style={styles.smallText}><span style={styles.label}>Civic Trust Score:</span> {c.civicTrustScore}</p>

            <div style={styles.sectionDivider}></div>

            <p style={styles.smallText}><span style={styles.label}>UPI Payment Proof:</span></p>
            {c.upiImage && <img src={`http://localhost:8080/media${c.upiImage.replace(/\\/g, "/")}`} alt="UPI Proof" style={styles.image} />}

            <div style={styles.actionButtons}>
              {c.status===-1?(<>
                <button
                style={{ ...styles.button, ...styles.approveButton }}
                onMouseOver={e => e.currentTarget.style.transform = "scale(1.05)"}
                onMouseOut={e => e.currentTarget.style.transform = "scale(1)"}
                onClick={()=>{approveCampaign(c.campaignId);}}
              >
                Approve
              </button>
              <button
                style={{ ...styles.button, ...styles.deleteButton }}
                onMouseOver={e => e.currentTarget.style.transform = "scale(1.05)"}
                onMouseOut={e => e.currentTarget.style.transform = "scale(1)"}
                onClick={()=>{deleteCampaign(c.campaignId);}}
              >
                Delete
              </button></>
            ):c.status===0?(<>
                <button
                style={{ ...styles.button, ...styles.rejectButton }}
                onMouseOver={e => e.currentTarget.style.transform = "scale(1.05)"}
                onMouseOut={e => e.currentTarget.style.transform = "scale(1)"}
                onClick={()=>{rejectCampaign(c.campaignId);}}
              >
                Reject
              </button>
              <button
                style={{ ...styles.button, ...styles.deleteButton }}
                onMouseOver={e => e.currentTarget.style.transform = "scale(1.05)"}
                onMouseOut={e => e.currentTarget.style.transform = "scale(1)"}
                onClick={()=>{deleteCampaign(c.campaignId);}}
              >
                Delete
              </button>
              </>):(
                <button
                style={{ ...styles.button, ...styles.deleteButton }}
                onMouseOver={e => e.currentTarget.style.transform = "scale(1.05)"}
                onMouseOut={e => e.currentTarget.style.transform = "scale(1)"}
                onClick={()=>{deleteCampaign(c.campaignId);}}
              >
                Delete
              </button>
              )}
            </div>

          </div>
        ))}
      </div>
    </div>
  );
};

export default ManageCampaigns;
