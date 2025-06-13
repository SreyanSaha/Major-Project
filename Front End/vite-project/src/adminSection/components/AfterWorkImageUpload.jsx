import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

export default function AfterWorkImageUploadPopup({ onClose, postId }) {
  const [afterWorkImages, setAfterWorkImages] = useState([null, null, null, null, null]);
  const navigate = useNavigate();

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
        onClose();
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

  return (
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
              onClose();
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
  );
}

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
};