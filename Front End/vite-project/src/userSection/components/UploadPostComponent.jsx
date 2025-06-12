import { useState , useEffect} from "react";
import { useNavigate } from "react-router-dom";
import LoadingOverlay from "../../loadingComponents/Loading";
import axios from "axios";

function UploadPost() {
  const navigate = useNavigate();
  const [msg,updateMsg] = useState(null);
  const [processing, setProcessing] = useState(false);
  const [authenticated, setAuthenticated] = useState(false);
  const [images, setImages] = useState([null, null, null, null, null]);
  const [location, setLocation] = useState("");
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [address, setAddress] = useState("");
  const [city, setCity] = useState("");
  const [state, setState] = useState("");
  const [zip, setZip] = useState("");
  const [aiAvailable, setAi] = useState(false);
  const [latitude, setLatitude] = useState("");
  const [longitude, setLongitude] = useState("");

  const loadSubscriptionDetails = async()=>{
    try{
        const user = JSON.parse(localStorage.getItem("user"));
        setProcessing(true);
        const response=await axios.get("http://localhost:8080/user/subscription/details",
          {
            headers:{
              "Authorization": "Bearer "+user.token,
              "Content-Type": "application/json"
            }
          }
        );
        if(response.status===200 && response.data.object.log===1){
          setAi(response.data.object.log===1?true:false);
          updateMsg(response.data.msg);
          setProcessing(false);
        }
        else if(response.status===202){
          setAi(false);
          updateMsg(response.data.msg);
          setProcessing(false);
        }
    }catch(exception){
      console.log(exception);
      setProcessing(false);
      updateMsg("Connection with the server failed.");
    }
  };

  const fetchLocation = () => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          setLocation(`Latitude: ${position.coords.latitude}, Longitude: ${position.coords.longitude}`);
          setLatitude(position.coords.latitude);
          setLongitude(position.coords.longitude);
        },
        (error) => {
          console.error("Error fetching location:", error);
          alert("Unable to fetch location. Please allow location access.");
        }
      );
    } else {
      alert("Geolocation is not supported by your browser.");
    }
  };

  useEffect(() => {
      try {
        const user = JSON.parse(localStorage.getItem("user"));
        console.log("Fetched user's username:", user.username);
        if (user?.username && user?.token && user?.role === 0) {setAuthenticated(true); loadSubscriptionDetails();}
        else navigate("/user/login");
      } catch (err) {
        console.error("Error parsing user from localStorage:", err);
        navigate("/user/login");
      }
    }, [navigate]);

  const handleImageChange = (index, file) => {
    const newImages = [...images];
    newImages[index] = file;
    setImages(newImages);
  };

  const handleSubmit = async() => {
    setProcessing(true);
    try{
      const user=JSON.parse(localStorage.getItem("user"));
      const formData=new FormData();
      const post={
        "postTitle":title,
        "postDescription":description,
        "latitude":latitude,
        "longitude":longitude,
        "street":address,
        "city":city,
        "state":state,
        "postalCode":zip
      };
      formData.append("post", JSON.stringify(post));
      images.forEach((image)=>{
        formData.append("images", image);
      });
      formData.append("uname",user.username);
      const response = await axios.post("http://localhost:8080/post/create",formData,{
        headers:{
            "Authorization": "Bearer "+user.token,
            "Content-Type": "multipart/form-data"
          }
      });
      if(response.status===201){
        setProcessing(false);
        updateMsg("Post created.");
      }else if(response.status===200){
        setProcessing(false);
        updateMsg(response.data);
      }
    }catch(exception){
      setProcessing(false);
      console.log(exception);
      updateMsg("Token expired login again!");
      if(exception.response && (exception.response.status===401 || exception.response.status===403))navigate("/user/login");
    }
  };

  const postDetailsCreationByAI = async ()=>{
    setProcessing(true);
    try{
      const user=JSON.parse(localStorage.getItem("user"));
      const formData=new FormData();
      formData.append("image", images[0]);
      const response = await axios.post("http://localhost:8080/ai/generate",formData,{
        headers:{
            "Authorization": "Bearer "+user.token,
            "Content-Type": "multipart/form-data"
          }
      });
      if(response.status===201){
        setTitle(response.data.object.title);
        setDescription(response.data.object.description);
        setProcessing(false);
      }else if(response.status===200){
        setProcessing(false);
        updateMsg(response.data.msg);
      }
    }catch(exception){
      setProcessing(false);
      console.log(exception);
      updateMsg("Token expired login again!");
    }
  };

  if (!authenticated) return null;
  return (
    <div style={styles.container}>
        {processing?<LoadingOverlay/>:""}
        <h2 style={styles.heading}>Upload Your Post</h2>
        {msg!=null?(<div style={styles.alertDiv}>
              <h3 style={styles.alertText}>{msg}</h3>
            </div>):""}
      <form style={styles.form} onSubmit={(e) => { e.preventDefault(); handleSubmit(); }}>
        <input
          type="text"
          placeholder="Enter Post Title"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          style={styles.input}
          required
        />

        <textarea
          placeholder="Enter Post Description"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          style={styles.textarea}
          required
        />

        <div style={styles.imageSectionContainer}>
          {images.map((img, index) => (
            <div key={index} style={styles.imageSection}>
              <label htmlFor={`image-${index}`} style={styles.uploadLabel}>
                {img ? (
                  <img
                    src={URL.createObjectURL(img)}
                    alt={`Uploaded ${index + 1}`}
                    style={styles.previewImage}
                  />
                ) : (
                  <span style={styles.placeholderText}>Upload Image {index + 1}</span>
                )}
              </label>
              <input
                type="file"
                id={`image-${index}`}
                accept="image/*"
                onChange={(e) => handleImageChange(index, e.target.files[0])}
                style={styles.hiddenInput}
              />
            </div>
          ))}
        </div>

        {aiAvailable?(
          <>
          <p style={styles.aiNote}>
          To generate title and description from AI, please select 1st image above.
        </p>
        <button
          type="button"
          style={styles.aiGenerateButton}
          onClick={()=>postDetailsCreationByAI()}
        >
          Generate Title & Description using AI
        </button>
        </>
        ):null}

        <div style={styles.locationSection}>
          <input
            type="text"
            value={location}
            onChange={(e) => setLocation(e.target.value)}
            placeholder="Current Location"
            style={styles.input}
            readOnly
          />
          <button type="button" onClick={fetchLocation} style={styles.locationButton}>
            Add Current Location
          </button>
        </div>

        <div style={styles.addressSection}>
          <input
            type="text"
            placeholder="Address"
            value={address}
            onChange={(e) => setAddress(e.target.value)}
            style={styles.input}
            
          />
          <input
            type="text"
            placeholder="City"
            value={city}
            onChange={(e) => setCity(e.target.value)}
            style={styles.input}
            
          />
          <input
            type="text"
            placeholder="State"
            value={state}
            onChange={(e) => setState(e.target.value)}
            style={styles.input}
            
          />
          <input
            type="text"
            placeholder="ZIP Code"
            value={zip}
            onChange={(e) => setZip(e.target.value)}
            style={styles.input}
            
          />
        </div>

        <button type="submit" style={styles.submitButton}>
          Upload Post
        </button>
      </form>
    </div>
  );
}

const styles = {
  aiNote: {
  fontSize: "0.95rem",
  color: "#333",
  textAlign: "center",
  marginTop: "-10px",
  marginBottom: "-10px",
  fontStyle: "italic",
},
aiGenerateButton: {
  backgroundColor: "#007bff",
  color: "#ffffff",
  padding: "0.7rem 1.2rem",
  border: "none",
  borderRadius: "8px",
  fontSize: "1rem",
  cursor: "pointer",
  fontWeight: "bold",
  transition: "background-color 0.3s",
  width: "fit-content",
  alignSelf: "center",
},
  alertDiv:{
        textAlign: "center",
    },
    alertText: {
        backgroundColor: "rgb(255, 64, 57)",
        padding: "2px",
        color: "white",
        borderRadius: "8px",
        width: "100%",
        marginBottom: "15px",
        margin: "auto",
        marginTop:"-10px",
    },
  container: {
    backgroundColor: "#f0f8ff",
    minHeight: "100vh",
    padding: "2rem",
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
  },
  heading: {
    color: "#0056b3",
    marginBottom: "1.5rem",
  },
  form: {
    backgroundColor: "#ffffff",
    padding: "2rem",
    borderRadius: "12px",
    boxShadow: "0 4px 8px rgba(0,0,0,0.1)",
    width: "100%",
    maxWidth: "700px",
    display: "flex",
    flexDirection: "column",
    gap: "1.5rem",
  },
  input: {
    padding: "0.8rem",
    borderRadius: "8px",
    border: "1px solid #007bff",
    outline: "none",
    fontSize: "1rem",
  },
  textarea: {
    padding: "0.8rem",
    borderRadius: "8px",
    border: "1px solid #007bff",
    outline: "none",
    fontSize: "1rem",
    minHeight: "100px",
    resize: "vertical",
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
    borderRadius: "8px",
  },
  locationSection: {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    gap: "0.5rem",
  },
  locationButton: {
    backgroundColor: "#007bff",
    color: "#ffffff",
    padding: "0.7rem 1.5rem",
    border: "none",
    borderRadius: "8px",
    cursor: "pointer",
    fontWeight: "bold",
    transition: "background-color 0.3s",
  },
  addressSection: {
    display: "flex",
    flexDirection: "column",
    gap: "1rem",
  },
  submitButton: {
    marginTop: "1rem",
    backgroundColor: "#0056b3",
    color: "#ffffff",
    padding: "0.8rem",
    border: "none",
    borderRadius: "10px",
    fontSize: "1rem",
    cursor: "pointer",
    fontWeight: "bold",
    transition: "background-color 0.3s",
  },
};

export default UploadPost;
