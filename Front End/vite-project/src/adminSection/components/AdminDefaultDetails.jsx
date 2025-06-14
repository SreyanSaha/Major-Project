import React, { useState, useEffect } from 'react';
import { useNavigate } from "react-router-dom";
import AfterWorkImageUploadPopup from './AfterWorkImageUpload';
import LoadingOverlay from '../../loadingComponents/Loading';
import axios from "axios";

export default function AdminDefaultDetails(){
    const navigate = useNavigate();
  const [msg, updateMsg] = useState(null);
  const [authenticated, setAuthenticated] = useState(false);
  const [activeMenu, setActiveMenu] = useState("Dashboard");
  const [issues, setIssues] = useState([]);
  const [processing, setProcessing] = useState(false);
  const [searchString, setSearchString] = useState("");
  const [isUploading, setUploading] = useState(false);
  const [postId, setPostId] = useState(0);
  const [afterWorkImages, setAfterWorkImages] = useState([null, null, null, null, null]);

  useEffect(() => {
      try {
        const admin = JSON.parse(localStorage.getItem("admin"));
        if (admin?.username && admin?.token && admin?.role === 1) {
          setAuthenticated(true);
          fetchAllPosts();
        } else navigate("/admin/login");
      } catch (err) {
        console.error("Error parsing user from localStorage:", err);
        updateMsg("Failed to fetch your data, please login again.");
        navigate("/admin/login");
      }
    }, []);

  const updateWorkCompleated = async ()=>{
    try{
      const admin=JSON.parse(localStorage.getItem("admin"));
      const formData=new FormData();
      afterWorkImages.forEach((image)=>{
        formData.append("images", image);
      });
      formData.append("postId", postId.toString());
      const response = await axios.post("http://localhost:8080/admin/update/work-completed",formData,{
        headers:{
            "Authorization": "Bearer "+admin.token,
          }
      });
      if(response.status===200){
        setAfterWorkImages([null, null, null, null, null]);
        setUploading(false);setPostId(0);
        fetchAllPosts();
      }
    }catch(exception){
      console.error("Error details:", {
        status: exception.response?.status,
        statusText: exception.response?.statusText,
        data: exception.response?.data,
        headers: exception.response?.headers
      });
      
      if (exception.response?.status === 403) {
        console.log("403 Forbidden - Token might be expired or invalid");
        navigate("/admin/login");
      } else {
        alert(`Error: ${exception.response?.data?.message || exception.message}`);
      }
    }
  };
  
  const fetchAllPosts = async () => {
    try {
      setProcessing(true);
      const admin = JSON.parse(localStorage.getItem("admin"));
      const response = await axios.get("http://localhost:8080/admin/all-posts", {
        headers: {
          "Authorization": "Bearer " + admin.token,
          "Content-Type": "application/json"
        }
      });
      if (response.status === 200) {
        setProcessing(false);
        setIssues(response.data.objects);
      } else if (response.status === 202) {
        setProcessing(false);
        updateMsg(response.data.msg);
      }
    } catch (exception) {
      console.log(exception);
      setProcessing(false);
      navigate("/admin/login");
    }
  };

  const searchPost = async() =>{
    try{
      const admin = JSON.parse(localStorage.getItem("admin"));
      setProcessing(true);
      const response=await axios.get(`http://localhost:8080/admin/search/post/${searchString}`,
        {
          headers:{
            "Authorization": "Bearer "+admin.token,
            "Content-Type": "application/json"
          }
        }
      );
      if(response.status===200){
        setIssues(response.data.objects);
        setProcessing(false);
      }else if(response.status===202){
        updateMsg(response.data.msg);
        setProcessing(false);
      }
    }catch(exception){
      console.log(exception);
      setProcessing(false);
      updateMsg(exception?.response?.data?.msg || exception.message );
    }
  }

  const getStatusDisplay = (status) => {
    switch (status) {
      case -1: return { text: "Under Review", color: "#fbbf24", bg: "#fef3c7" };
      case 0: return { text: "Work In Progress", color: "#3b82f6", bg: "#dbeafe" };
      case 1: return { text: "Work Completed", color: "#10b981", bg: "#d1fae5" };
      default: return { text: "Unknown", color: "#6b7280", bg: "#f3f4f6" };
    }
  };

  const getImagePaths = (issue) => {
    return [
    issue.imagePath1,
    issue.imagePath2,
    issue.imagePath3,
    issue.imagePath4,
    issue.imagePath5
  ].filter(path => path);
  };

  const getAfterWorkImagePaths = (issue) => {
    return [
    issue.afterWorkImagePath1,
    issue.afterWorkImagePath2,
    issue.afterWorkImagePath3,
    issue.afterWorkImagePath4,
    issue.afterWorkImagePath5
  ].filter(path => path);
  };

  const updateIssueWIP = async (id) => {
    try {
      setProcessing(true);
      const admin = JSON.parse(localStorage.getItem("admin"));
      const response = await axios.post("http://localhost:8080/admin/update/wip", id, {
        headers: {
          "Authorization": "Bearer " + admin.token,
          "Content-Type": "application/json"
        }
      });
      if (response.status === 200 && response.data.object) {
        setProcessing(false);
        updateMsg(response.data.msg);
        fetchAllPosts(); // Refresh data
      } else if (response.status === 202) {
        setProcessing(false);
        updateMsg(response.data.msg);
      }
    } catch (exception) {
      console.log(exception);
      navigate("/admin/login");
    }
  };

  const deletePost= async(id)=>{
    try {
      setProcessing(true);
      const admin = JSON.parse(localStorage.getItem("admin"));
      const response = await axios.delete(`http://localhost:8080/admin/delete/post/${id}`, {
        headers: {
          "Authorization": "Bearer " + admin.token,
          "Content-Type": "application/json"
        }
      });
      if (response.status === 200 && response.data.object) {
        setProcessing(false);
        updateMsg(response.data.msg);
        fetchAllPosts(); 
      } else if (response.status === 202) {
        setProcessing(false);
        updateMsg(response.data.msg);
      }
    } catch (exception) {
      console.log(exception);
      navigate("/admin/login");
    }
  };

    const styles = {
        overlay: {
    position: "fixed",
    top: 0,
    left: 0,
    width: "100vw",
    height: "100vh",
    backgroundColor: "rgba(0, 0, 0, 0.5)",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    zIndex: 1000,
  },
  popup: {
    backgroundColor: "#fff",
    padding: "2rem",
    borderRadius: "12px",
    width: "90%",
    maxWidth: "600px",
    boxShadow: "0 4px 10px rgba(0,0,0,0.2)",
  },
  title: {
    textAlign: "center",
    color: "#004080",
    marginBottom: "1.5rem",
  },
  imageSectionContainer: {
    display: "grid",
    gridTemplateColumns: "repeat(auto-fit, minmax(100px, 1fr))",
    gap: "1rem",
  },
  imageSection: {
    backgroundColor: "#e6f0ff",
    border: "2px dashed #007bff",
    borderRadius: "10px",
    height: "120px",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    position: "relative",
    overflow: "hidden",
  },
  uploadLabel: {
    width: "100%",
    height: "100%",
    cursor: "pointer",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    flexDirection: "column",
    color: "#007bff",
    fontWeight: "bold",
  },
  placeholderText: {
    textAlign: "center",
  },
  hiddenInput: {
    display: "none",
  },
  previewImage: {
    width: "100%",
    height: "100%",
    objectFit: "cover",
  },
  cancelBtn: {
    backgroundColor: "#ccc",
    padding: "0.5rem 1rem",
    border: "none",
    borderRadius: "6px",
    cursor: "pointer",
  },
  submitBtn: {
    backgroundColor: "#007bff",
    color: "white",
    padding: "0.5rem 1rem",
    border: "none",
    borderRadius: "6px",
    cursor: "pointer",
  },
    searchInput: {
      padding: "10px",
      width: "85%",
      borderRadius: "5px",
      border: "1px solid #ccc",
      marginRight: "10px",
      marginBottom:"10px",
    },
    searchButton: {
      padding: "10px 15px",
      backgroundColor: "#1b3c74",
      color: "#fff",
      border: "none",
      borderRadius: "5px",
      cursor: "pointer",
      marginBottom:"10px",
    },
    imageGroup: {
    display: 'flex',
    gap: '10px',
    flexWrap: 'wrap',
    justifyContent: 'center',
  },
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
      gridTemplateColumns: 'repeat(1, minmax(400px, 1fr))',
      gap: '20px',
    },
    card: {
      backgroundColor: 'white',
      borderRadius: '12px',
      padding: '20px',
      boxShadow: '0 2px 8px rgba(0, 0, 0, 0.1)',
      border: '1px solid #e5e7eb',
    },
    cardHeader: {
      display: 'flex',
      justifyContent: 'space-between',
      alignItems: 'center',
      marginBottom: '15px',
      borderBottom: '2px solid #f3f4f6',
      paddingBottom: '10px',
    },
    cardHeading: {
      color: '#1e3a8a',
      fontWeight: 'bold',
      fontSize: '18px',
      margin: 0,
    },
    statusBadge: (status) => ({
      padding: '4px 12px',
      borderRadius: '20px',
      fontSize: '12px',
      fontWeight: 'bold',
      color: status.color,
      backgroundColor: status.bg,
    }),
    imageContainer: {
      position: 'relative',
      marginBottom: '10px',
    },
    image: {
      width: '235px',
      height: '250px',
      objectFit: 'cover',
      borderRadius: '8px',
    },
    imageControls: {
      display: 'flex',
      justifyContent: 'space-between',
      alignItems: 'center',
      marginTop: '8px',
    },
    arrowBtn: {
      backgroundColor: '#1e3a8a',
      color: 'white',
      border: 'none',
      borderRadius: '6px',
      padding: '6px 12px',
      cursor: 'pointer',
      fontSize: '14px',
    },
    imageCounter: {
      fontSize: '14px',
      color: '#6b7280',
      fontWeight: '500',
    },
    infoSection: {
      display: 'grid',
      gridTemplateColumns: '1fr 1fr',
      gap: '10px',
      marginBottom: '15px',
    },
    infoItem: {
      marginBottom: '8px',
    },
    infoLabel: {
      fontWeight: 'bold',
      color: '#374151',
      fontSize: '14px',
    },
    infoValue: {
      color: '#6b7280',
      fontSize: '14px',
      marginTop: '2px',
    },
    locationSection: {
      backgroundColor: '#f9fafb',
      padding: '12px',
      borderRadius: '8px',
      marginBottom: '15px',
    },
    locationTitle: {
      fontWeight: 'bold',
      color: '#1e3a8a',
      marginBottom: '8px',
      fontSize: '14px',
    },
    engagementStats: {
      display: 'flex',
      justifyContent: 'space-around',
      backgroundColor: '#f3f4f6',
      padding: '10px',
      borderRadius: '8px',
      marginBottom: '15px',
    },
    stat: {
      textAlign: 'center',
    },
    statValue: {
      fontWeight: 'bold',
      fontSize: '18px',
      color: '#1e3a8a',
    },
    statLabel: {
      fontSize: '12px',
      color: '#6b7280',
    },
    actionButtons: {
      display: 'flex',
      gap: '10px',
      marginTop: '15px',
    },
    actionBtn: (bgColor) => ({
      flex: 1,
      padding: '8px 12px',
      border: 'none',
      borderRadius: '6px',
      color: 'white',
      backgroundColor: bgColor,
      cursor: 'pointer',
      fontSize: '14px',
      fontWeight: '500',
    }),
    deleteButton: {
      backgroundColor: "#f44336",
      color: "#fff",
    },
    afterWorkSection: {
      marginTop: '15px',
      padding: '12px',
      backgroundColor: '#ecfdf5',
      borderRadius: '8px',
      border: '1px solid #10b981',
    },
    alertDiv: {
      backgroundColor: '#fef2f2',
      border: '1px solid #fca5a5',
      borderRadius: '8px',
      padding: '12px',
      marginBottom: '20px',
    },
    alertText: {
      color: '#dc2626',
      margin: 0,
    },
  };
    return(
    !isUploading?(<div style={styles.mainContent}>
        {processing?<LoadingOverlay/>:""}
      <h1 style={styles.mainHeading}>Post Management Dashboard</h1>
      <input
      placeholder="Search post by title or address..."
      type="text"
      value={searchString}
      onChange={(e) => setSearchString(e.target.value)}
      style={{
        padding: "10px",
        width: "85%",
        borderRadius: "5px",
        border: "1px solid rgb(204, 204, 204)",
        marginRight: "10px",
        marginBottom: "10px",
      }}
    />

      <button style={styles.searchButton} onClick={()=>searchPost()}>Search Posts</button>
      <div style={styles.cardsGrid}>
        {issues.map((issue) => {
          const imagePaths = getImagePaths(issue);
          const afterWorkImages = getAfterWorkImagePaths(issue);
          const status = getStatusDisplay(issue.postStatus);

          return (
            <div key={issue.postId} style={styles.card}>
              {/* Card Header */}
              <div style={styles.cardHeader}>
                <h3 style={styles.cardHeading}>Issue #{issue.postId}</h3>
                <span style={styles.statusBadge(status)}>{status.text}</span>
              </div>

              {/* Image Section */}
              <div style={styles.imageGroup}>
                {imagePaths.map((img, index) => (
                  <img
                    key={index}
                    src={`http://localhost:8080/media${img.replace(/\\/g, "/")}`}
                    alt={`Post ${issue.postId} image ${index + 1}`}
                    style={styles.image}
                  />
                ))}
              </div>

              {/* Basic Info */}
              <div style={styles.infoSection}>
                <div style={styles.infoItem}>
                  <div style={styles.infoLabel}>Author</div>
                  <div style={styles.infoValue}>{issue.authorProfileName}</div>
                </div>
                <div style={styles.infoItem}>
                  <div style={styles.infoLabel}>Posted</div>
                  <div style={styles.infoValue}>{new Date(issue.postUploadDateTime).toLocaleString()}</div>
                </div>
                <div style={styles.infoItem}>
                  <div style={styles.infoLabel}>User ID</div>
                  <div style={styles.infoValue}>{issue.userId}</div>
                </div>
                <div style={styles.infoItem}>
                  <div style={styles.infoLabel}>Trust Score</div>
                  <div style={styles.infoValue}>{issue.civicTrustScore}</div>
                </div>
              </div>

              {/* Title and Description */}
              <div style={styles.infoItem}>
                <div style={styles.infoLabel}>Title</div>
                <div style={styles.infoValue}>{issue.postTitle}</div>
              </div>
              <div style={styles.infoItem}>
                <div style={styles.infoLabel}>Description</div>
                <div style={styles.infoValue}>{issue.postDescription}</div>
              </div>

              {/* Location */}
              <div style={styles.locationSection}>
                <div style={styles.locationTitle}>📍 Location</div>
                <div style={styles.infoValue}>
                  {issue.street}, {issue.city}, {issue.state} {issue.postalCode}, {issue.country}
                </div>
                <div style={styles.infoValue}>
                  Coordinates: {issue.latitude}, {issue.longitude}
                </div>
              </div>

              {/* Engagement Stats */}
              <div style={styles.engagementStats}>
                <div style={styles.stat}>
                  <div style={styles.statValue}>👍 {issue.upVoteCount}</div>
                  <div style={styles.statLabel}>Upvotes</div>
                </div>
                <div style={styles.stat}>
                  <div style={styles.statValue}>👎 {issue.downVoteCount}</div>
                  <div style={styles.statLabel}>Downvotes</div>
                </div>
                <div style={styles.stat}>
                  <div style={styles.statValue}>💬 {issue.commentCount}</div>
                  <div style={styles.statLabel}>Comments</div>
                </div>
                <div style={styles.stat}>
                  <div style={styles.statValue}>🚩 {issue.postReports}</div>
                  <div style={styles.statLabel}>Reports</div>
                </div>
              </div>

              {/* After Work Images */}
              {afterWorkImages.length > 0 && (
                <div style={styles.afterWorkSection}>
                  <div style={styles.locationTitle}>✅ After Work Images</div>
                  <div style={styles.infoValue}>{afterWorkImages.length} images uploaded</div>
                </div>
              )}

              {/* Action Buttons */}
              <div style={styles.actionButtons}>
                {issue.postStatus !== 0?(<button
                  style={styles.actionBtn("#3b82f6")}
                  onClick={() => updateIssueWIP(issue.postId)}
                >
                  Mark WIP
                </button>):null}
                <button
                  style={styles.actionBtn("#10b981")}
                  onClick={() => {
                    setUploading(true);setPostId(issue.postId);
                  }}
                  disabled={issue.postStatus === 1}
                >
                  Complete Work
                </button>
                <button
                  style={styles.actionBtn("#f44336")}
                  onClick={() => {deletePost(issue.postId);}}
                >
                  Delete Post
                </button>
              </div>
            </div>
          );
        })}
      </div>
    </div>):(
        <div style={styles.overlay}>
      <div style={styles.popup}>
        <h2 style={styles.title}>Upload 5 After-Work Images, for the Issue #{postId}</h2>
        <div style={styles.imageSectionContainer}>
          {afterWorkImages.map((img, index) => (
            <div key={index} style={styles.imageSection}>
              <label htmlFor={`after-image-${index}`} style={styles.uploadLabel}>
                {img ? (
                  <img
                    src={URL.createObjectURL(img)}
                    alt={`After Image ${index + 1}`}
                    style={styles.previewImage}
                  />
                ) : (
                  <span style={styles.placeholderText}>Image {index + 1}</span>
                )}
              </label>
              <input
                type="file"
                id={`after-image-${index}`}
                accept="image/*"
                onChange={(e) => {
                  const updatedImages = [...afterWorkImages];
                  updatedImages[index] = e.target.files[0];
                  setAfterWorkImages(updatedImages);
                }}
                style={styles.hiddenInput}
              />
            </div>
          ))}
        </div>
        <div style={{ display: "flex", justifyContent: "space-between", marginTop: "1rem" }}>
          <button
            style={styles.cancelBtn}
            onClick={() => {
              setAfterWorkImages([null, null, null, null, null]);
              setUploading(false);setPostId(0);
            }}
          >
            Cancel
          </button>
          <button style={styles.submitBtn} onClick={()=>updateWorkCompleated()}>
            Submit
          </button>
        </div>
      </div>
    </div>
    )
  );
}