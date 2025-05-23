import React, { useState , useEffect} from "react";
import { useNavigate } from "react-router-dom";
import LoadingOverlay from "../../loadingComponents/Loading";
import axios from "axios";

export default function UserProfile() {
  const navigate = useNavigate();
  const [authenticated, setAuthenticated] = useState(false);
  const [confirmDelete, setConfirmDelete] = useState(false);
  const [profilePreview, setProfilePreview] = useState("");
  const [userProfile, setUserProfile] = useState({});
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");
  const [country, setCountry] = useState("");
  const [address, setAddress] = useState("");
  const [city, setCity] = useState("");
  const [state, setState] = useState("");
  const [zip, setZip] = useState("");
  const [profileImage, setProfileImage] = useState("");
  const [processing, setProcessing] = useState(false);

  useEffect(() => {
      try {
        const user = JSON.parse(localStorage.getItem("user"));
        console.log("Fetched user's username:", user.username);
        if (user?.username && user?.token && user?.role === 0) {setAuthenticated(true);fetchUserProfile();}
        else navigate("/user/login");
      } catch (err) {
        console.error("Error parsing user from localStorage:", err);
        updateMsg("Failed to fetch your data, please login again.");
        navigate("/user/login");
      }
    }, [navigate]);


  const handleImageChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setProfilePreview(reader.result);
        setFormData(prev => ({ ...prev, profileImage: reader.result }));
      };
      reader.readAsDataURL(file);
    }
  };

  const fetchUserProfile = async (username) =>{
    try{
      if(userProfile===null){
        const user = JSON.parse(localStorage.getItem("user"));
        const response = await axios.get(`http://localhost:8080/user/profile?uname=${user.username}`,
          {
            headers:{
                "Authorization": "Bearer "+user.token,
                "Content-Type": "application/json"
            }
          }
        );
        if(response.status===200){
          setProcessing(false);
          setUserProfile(response.data);
          console.log(response.data);
        }else if(response.status===202){
          setProcessing(false);
          
        }
      }
    }catch(exception){
      console.log(exception);
    }
  };

  const handleUpdate = () => {
    
  };

  const handleDelete = () => {
    
  };

  // if (!user)
  //   return (
  //     <div style={{ textAlign: "center", marginTop: "3rem", color: "#11398f" }}>
  //       User not found.
  //     </div>
  //   );

  const styles = {
    container: {
      width: "700px",
      margin: "2rem auto",
      padding: "2rem",
      backgroundColor: "#ffffff",
      borderRadius: 12,
      boxShadow: "0 6px 18px rgba(17, 57, 143, 0.15)",
      fontFamily: "'Segoe UI', Tahoma, Geneva, Verdana, sans-serif",
      color: "#11398f",
    },
    title: {
      textAlign: "center",
      marginBottom: "2rem",
      fontSize: "2rem",
      fontWeight: "700",
      letterSpacing: "1.2px",
      color: "#0b2768",
    },
    profileImageWrapper: {
      display: "flex",
      justifyContent: "center",
      marginBottom: "1.8rem",
    },
    profileImage: {
      width: 130,
      height: 130,
      borderRadius: "50%",
      objectFit: "cover",
      border: "4px solid #1e3cb4",
      boxShadow: "0 4px 12px rgba(17, 57, 143, 0.3)",
    },
    fileInputWrapper: {
      display: "flex",
      justifyContent: "center",
      marginBottom: "2rem",
    },
    fileInput: {
      cursor: "pointer",
      fontWeight: "600",
      color: "#1e3cb4",
    },
    form: {
      marginRight: "25px",
      display: "grid",
      gridTemplateColumns: "1fr 1fr",
      gap: "1.5rem 2rem",
    },
    fullWidth: {
      gridColumn: "1 / -1",
    },
    label: {
      display: "block",
      marginBottom: "0.4rem",
      fontWeight: "600",
      fontSize: "0.9rem",
      color: "#1e3cb4",
    },
    input: {
      width: "100%",
      padding: "0.6rem 1rem",
      borderRadius: 8,
      border: "1.8px solid #b5c7ff",
      fontSize: "1rem",
      color: "#11398f",
      outline: "none",
      transition: "border-color 0.3s ease",
    },
    inputFocus: {
      borderColor: "#0b2768",
      boxShadow: "0 0 6px #0b2768",
    },
    disabledInput: {
      backgroundColor: "#f0f4ff",
      color: "#6986cb",
      cursor: "not-allowed",
    },
    buttonsWrapper: {
      marginTop: "2.5rem",
      display: "flex",
      justifyContent: "space-between",
    },
    button: {
      flex: "1 1 45%",
      padding: "0.75rem 0",
      borderRadius: 8,
      fontWeight: "700",
      fontSize: "1.1rem",
      border: "none",
      cursor: "pointer",
      transition: "background-color 0.3s ease",
      boxShadow: "0 3px 8px rgba(17, 57, 143, 0.3)",
    },
    updateBtn: {
      backgroundColor: "#1e3cb4",
      color: "white",
    },
    updateBtnHover: {
      backgroundColor: "#11398f",
    },
    deleteBtn: {
      backgroundColor: "#d9534f",
      color: "white",
    },
    deleteBtnHover: {
      backgroundColor: "#b53c3a",
    },
    confirmOverlay: {
      position: "fixed",
      top: 0,
      left: 0,
      right: 0,
      bottom: 0,
      backgroundColor: "rgba(17, 57, 143, 0.8)",
      display: "flex",
      justifyContent: "center",
      alignItems: "center",
      zIndex: 9999,
    },
    confirmBox: {
      backgroundColor: "white",
      padding: "2rem",
      borderRadius: 12,
      maxWidth: 400,
      width: "90%",
      textAlign: "center",
      boxShadow: "0 6px 20px rgba(17, 57, 143, 0.3)",
      color: "#11398f",
    },
    confirmText: {
      marginBottom: "1.5rem",
      fontWeight: "700",
      fontSize: "1.2rem",
    },
    confirmBtns: {
      display: "flex",
      justifyContent: "center",
      gap: "1rem",
    },
    confirmYesBtn: {
      backgroundColor: "#d9534f",
      color: "white",
      padding: "0.6rem 1.6rem",
      borderRadius: 8,
      border: "none",
      cursor: "pointer",
      fontWeight: "700",
      fontSize: "1rem",
      boxShadow: "0 3px 8px rgba(217, 83, 79, 0.5)",
    },
    confirmNoBtn: {
      backgroundColor: "#1e3cb4",
      color: "white",
      padding: "0.6rem 1.6rem",
      borderRadius: 8,
      border: "none",
      cursor: "pointer",
      fontWeight: "700",
      fontSize: "1rem",
      boxShadow: "0 3px 8px rgba(30, 60, 180, 0.5)",
    },
  };
  if (!authenticated) return null;
  return (
    <div style={styles.container}>
      <h2 style={styles.title}>User Profile</h2>

      <div style={styles.profileImageWrapper}>
        <img
          src={profilePreview || ""}
          alt="Profile"
          style={styles.profileImage}
        />
      </div>
      <div style={styles.fileInputWrapper}>
        <input
          type="file"
          accept="image/*"
          onChange={handleImageChange}
          style={styles.fileInput}
          aria-label="Upload profile image"
        />
      </div>

      <form style={styles.form} onSubmit={(e) => {e.preventDefault(); handleUpdate();}}>
        <label style={styles.label} htmlFor="firstName">
          First Name
        </label>
        <input
          id="firstName"
          name="firstName"
          type="text"
          value={userProfile.userFirstName || ""}
          onChange={(e) => setFirstName(e.target.value)}
          style={styles.input}
          required
        />

        <label style={styles.label} htmlFor="lastName">
          Last Name
        </label>
        <input
          id="lastName"
          name="lastName"
          type="text"
          value={userProfile.userLastName || ""}
          onChange={(e) => setLastName(e.target.value)}
          style={styles.input}
          required
        />

        <label style={styles.label} htmlFor="lastName">
          Phone Number
        </label>
        <input
          id="Phone Number"
          name="phoneNumber"
          type="text"
          value={userProfile.userPhoneNumber || ""}
          onChange={(e) => setPhoneNumber(e.target.value)}
          style={styles.input}
          required
        />

        <label style={styles.label} htmlFor="lastName">
          Country
        </label>
        <input
          id="Country"
          name="country"
          type="text"
          value={"India"}
          style={styles.input}
          required
        />

        <label style={styles.label} htmlFor="lastName">
          Civic Trust Score
        </label>
        <input
          id="Civic Trust Score"
          name="civicTrustScore"
          type="text"
          value={userProfile.civicTrustScore}
          style={{...styles.input, ...styles.disabledInput, }}
          required
        />

        <label style={styles.label} htmlFor="address">
          Address
        </label>
        <input
          id="address"
          name="address"
          type="text"
          value={userProfile.street || ""}
          onChange={(e) => setAddress(e.target.value)}
          style={{ ...styles.input, gridColumn: "1 / -1" }}
          required
        />

        <label style={styles.label} htmlFor="city">
          City
        </label>
        <input
          id="city"
          name="city"
          type="text"
          value={userProfile.city || ""}
          onChange={(e) => setCity(e.target.value)}
          style={styles.input}
        />

        <label style={styles.label} htmlFor="state">
          State
        </label>
        <input
          id="state"
          name="state"
          type="text"
          value={userProfile.state || ""}
          onChange={(e) => setState(e.target.value)}
          style={styles.input}
        />

        <label style={styles.label} htmlFor="zip">
          Zip Code
        </label>
        <input
          id="zip"
          name="zip"
          type="text"
          value={userProfile.zip || ""}
          onChange={(e) => setZip(e.target.value)}
          style={styles.input}
        />

        <label style={{ ...styles.label, gridColumn: "1 / -1" }} htmlFor="email">
          Email (read-only)
        </label>
        <input
          id="email"
          name="email"
          type="email"
          value={userProfile.userEmailId}
          disabled
          style={{ ...styles.input, ...styles.disabledInput, gridColumn: "1 / -1" }}
        />
      </form>

      <div style={styles.buttonsWrapper}>
        <button
          style={{ ...styles.button, ...styles.updateBtn }}
          onMouseOver={e => (e.currentTarget.style.backgroundColor = styles.updateBtnHover.backgroundColor)}
          onMouseOut={e => (e.currentTarget.style.backgroundColor = styles.updateBtn.backgroundColor)}
        >
          Update
        </button>

        <button
          onClick={() => setConfirmDelete(true)}
          style={{ ...styles.button, ...styles.deleteBtn }}
          onMouseOver={e => (e.currentTarget.style.backgroundColor = styles.deleteBtnHover.backgroundColor)}
          onMouseOut={e => (e.currentTarget.style.backgroundColor = styles.deleteBtn.backgroundColor)}
        >
          Delete
        </button>
      </div>

      {confirmDelete && (
        <div style={styles.confirmOverlay}>
          <div style={styles.confirmBox}>
            <div style={styles.confirmText}>Are you sure you want to delete your profile?</div>
            <div style={styles.confirmBtns}>
              <button style={styles.confirmYesBtn} onClick={handleDelete}>
                Yes
              </button>
              <button style={styles.confirmNoBtn} onClick={() => setConfirmDelete(false)}>
                No
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
