import React, { useState , useEffect} from "react";
import { useNavigate } from "react-router-dom";
import LoadingOverlay from "../../loadingComponents/Loading";
import axios from "axios";

export default function UserProfile() {
  const navigate = useNavigate();
  const [msg,updateMsg] = useState(null);
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
  const [email, setEmail] = useState("");
  const [profileImage, setProfileImage] = useState("");
  const [processing, setProcessing] = useState(false);

  useEffect(() => {
  if (userProfile.userFirstName) setFirstName(userProfile.userFirstName);
  if (userProfile.userLastName) setLastName(userProfile.userLastName);
  if (userProfile.userPhoneNumber) setPhoneNumber(userProfile.userPhoneNumber);
  if (userProfile.street) setAddress(userProfile.street);
  if (userProfile.city) setCity(userProfile.city);
  if (userProfile.state) setState(userProfile.state);
  if (userProfile.country) setCountry(userProfile.country);
  if (userProfile.zipCode) setZip(userProfile.zipCode);
  if (userProfile.userEmailId) setEmail(userProfile.userEmailId);
  if (userProfile.profileImagePath) setProfilePreview(userProfile.profileImagePath);
}, [userProfile]);

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
        setProfileImage(file);
      };
      reader.readAsDataURL(file);
    }
  };

  const fetchUserProfile = async () =>{
    try{
      setProcessing(true);
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
      }else if(response.status===202){
        setProcessing(false);
        updateMsg(response.data);
      }
    }catch(exception){
      console.log(exception);
      updateMsg("Token expired please login again.");
      navigate("/user/login");
    }
  };

  const handleUpdate = async () => {
    try{
      const user=JSON.parse(localStorage.getItem("user"));
      const formData=new FormData();
      const newUser={
        "userFirstName":firstName,
        "userLastName":lastName,
        "userEmailId":userProfile.userEmailId,
        "userPhoneNumber":phoneNumber,
        "latitude":0,
        "longitude":0,
        "street":address,
        "city":city,
        "state":state,
        "country":country,
        "zipCode":zip
      };
      formData.append("user",JSON.stringify(newUser));
      formData.append("uname",user.username);
      formData.append("profileImage", profileImage);
      const response = await axios.put("http://localhost:8080/user/update-user",formData,{
        headers:{
            "Authorization": "Bearer "+user.token,
            "Content-Type": "multipart/form-data"
          }
      });
      if(response.status===200){
        setProcessing(false);
        updateMsg("Profile updated");
        setUserProfile(response.data);
      }
      else if(response.status===202){
        setProcessing(false);
        updateMsg(response.data);
      }
    }catch(exception){
      setProcessing(false);
      console.log(exception);
      if(exception.response && (exception.response.status===401 || exception.response.status===403)){
       updateMsg("Token expired login again!");
       navigate("/user/login");
      }
    }
  };

  const handleDelete = async () => {
    try{
      setProcessing(true);
      const user = JSON.parse(localStorage.getItem("user"));
      const response = await axios.delete("http://localhost:8080/user/delete/profile",
        {
          headers:{
              "Authorization": "Bearer "+user.token,
              "Content-Type": "application/json"
          }
        }
      );
      if(response.status===200 && response.data.object){
        localStorage.removeItem("user");
        updateMsg("Your profile deleted.");
        setProcessing(false);
        navigate("/user/signup");
      }
      else if(response.status===202){
        updateMsg(response.data.msg);
        setProcessing(false);
      }
    }catch(exception){
      console.log(exception);
      updateMsg("Failed to delete your profile.");
      setProcessing(false);
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
      {processing?<LoadingOverlay/>:""}
      <h2 style={styles.title}>User Profile</h2>
      {msg!=null?(<div style={styles.alertDiv}>
            <h3 style={styles.alertText}>{msg}</h3>
          </div>):""}
      <div style={styles.profileImageWrapper}>
        <img
          src={
            profilePreview.startsWith("data:image")
            ? profilePreview
            : `http://localhost:8080/media${profilePreview.replace("\\", "/")}`
          }
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

      <form style={styles.form} onSubmit={(e) => {e.preventDefault();}}>
        <label style={styles.label} htmlFor="firstName">
          First Name
        </label>
        <input
          id="firstName"
          name="firstName"
          type="text"
          value={firstName}
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
          value={lastName}
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
          value={phoneNumber}
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
          value={country}
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
          defaultValue={userProfile.civicTrustScore}
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
          value={address}
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
          value={city}
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
          value={state}
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
          value={zip}
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
          defaultValue={email}
          disabled
          style={{ ...styles.input, ...styles.disabledInput, gridColumn: "1 / -1" }}
        />
      </form>

      <div style={styles.buttonsWrapper}>
        <button
          style={{ ...styles.button, ...styles.updateBtn }}
          onMouseOver={e => (e.currentTarget.style.backgroundColor = styles.updateBtnHover.backgroundColor)}
          onMouseOut={e => (e.currentTarget.style.backgroundColor = styles.updateBtn.backgroundColor)}
          onClick={() => handleUpdate()}
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
            <div style={styles.confirmText}>
              Deleting your profile is permanent and cannot be undone. Are you sure you want to continue?</div>
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
