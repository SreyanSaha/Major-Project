import { useState , useEffect} from "react";
import LoadingOverlay from "../../loadingComponents/Loading";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function UploadCampaign() {
  const navigate = useNavigate();
  const [authenticated, setAuthenticated] = useState(false);
  const [msg,updateMsg] = useState(null);
  const [images, setImages] = useState([null, null, null, null, null]);
  const [paymentImage, setPaymentImage] = useState(null);
  const [address, setAddress] = useState("");
  const [city, setCity] = useState("");
  const [state, setState] = useState("");
  const [zipCode, setZipCode] = useState("");
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [campaignType, setCampaignType] = useState(-1);
  const [processing, setProcessing] = useState(false);

  useEffect(() => {
      try {
        const user = JSON.parse(localStorage.getItem("user"));
        console.log("Fetched user's username:", user.username);
        if (user?.username && user?.token && user?.role === 0) setAuthenticated(true);
        else navigate("/user/login");
      } catch (err) {
        console.error("Error parsing user from localStorage:", err);
        updateMsg("Failed to fetch your data, please login again.");
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
      console.log(JSON.parse(localStorage.getItem("user")).token);
      const formData=new FormData();
      const campaign={
        "campaignTitle":title,
        "campaignDescription":description,
        "street":address,
        "city":city,
        "state":state,
        "postalCode":zipCode,
        "campaignType":campaignType
      };
      formData.append("campaign", JSON.stringify(campaign));
      images.forEach((image)=>{
        formData.append("images", image);
      });
      formData.append("upiQRImage", paymentImage);
      formData.append("uname",user.username);
      const response = await axios.post("http://localhost:8080/campaign/create",formData,{
        headers:{
            "Authorization": "Bearer "+user.token,
            "Content-Type": "multipart/form-data"
          }
      });
      if(response.status===201){
        setProcessing(false);
        updateMsg("Campaing post created. Waiting for admin's approval.");
      }
      else if(response.status===200){
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
  if (!authenticated) return null;
  return (
    <div style={styles.container}>
      {processing?<LoadingOverlay/>:""}
      <h2 style={styles.heading}>Upload Campaign</h2>
      {msg!=null?(<div style={styles.alertDiv}>
              <h3 style={styles.alertText}>{msg}</h3>
            </div>):""}
      <form style={styles.form} onSubmit={(e) => { e.preventDefault(); handleSubmit(); }}>
        <input
          type="text"
          placeholder="Enter Campaign Title"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          style={styles.inputField}
          required
        />

        <select
          value={campaignType}
          onChange={(e) => setCampaignType(e.target.value)}
          style={styles.selectField}
          required
        >
        <option value="">Select Campaign Type</option>
        <option value="-1">Donation</option>
        <option value="0">Awareness</option>
        <option value="1">Volunteering</option>
      </select>

        <div style={styles.noteSection}>
          <p style={styles.noteText}>
            ⚡ <strong>Note:</strong> Please mention about the campaign properly and also mention the online payment details (like UPI ID, bank details, etc.) inside the description.
          </p>
        </div>

        <textarea
          placeholder="Enter Campaign Description (Include Payment Details)"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          style={styles.textArea}
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

        <div style={styles.addressSection}>
          <input
            type="text"
            placeholder="Address"
            value={address}
            onChange={(e) => setAddress(e.target.value)}
            style={styles.inputField}
            required
          />
          <input
            type="text"
            placeholder="City"
            value={city}
            onChange={(e) => setCity(e.target.value)}
            style={styles.inputField}
            required
          />
          <input
            type="text"
            placeholder="State"
            value={state}
            onChange={(e) => setState(e.target.value)}
            style={styles.inputField}
            required
          />
          <input
            type="text"
            placeholder="Zip Code"
            value={zipCode}
            onChange={(e) => setZipCode(e.target.value)}
            style={styles.inputField}
            required
          />
        </div>
        
        <div style={styles.noteSection}>
          <p style={styles.noteText}>
            ⚡ <strong>Note:</strong> As our site does not allow any means of cash transaction so, please upload your online payment UPI QR Code.
          </p>
        </div>

        <div style={styles.paymentSection}>
          <label htmlFor="paymentImage" style={styles.uploadLabel}>
            {paymentImage ? (
              <img
                src={URL.createObjectURL(paymentImage)}
                alt="Payment QR"
                style={styles.previewImage}
              />
            ) : (
              <span style={styles.placeholderText}>Upload UPI QR</span>
            )}
          </label>
          <input
            type="file"
            id="paymentImage"
            accept="image/*"
            onChange={(e) => setPaymentImage(e.target.files[0])}
            style={styles.hiddenInput}
          />
        </div>

        <button type="submit" style={styles.submitButton}>
          Upload Campaign
        </button>
      </form>
    </div>
  );
}

const styles = {
  alertDiv:{
        textAlign: "center",
    },
    alertText: {
        backgroundColor: "rgb(255, 64, 57)",
        padding: "2px",
        color: "white",
        borderRadius: "8px",
        width: "80%",
        margin: "auto",
        marginBottom: "10px",
    },
  selectField: {
  width: "100%",
  padding: "0.8rem",
  borderRadius: "8px",
  border: "1px solid #007bff",
  backgroundColor: "#ffffff",
  color: "#333",
  },
  container: {
    backgroundColor: "#f0f8ff",
    height: "100vh",
    padding: "1rem",
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
  inputField: {
    width: "95%",
    padding: "0.8rem",
    borderRadius: "8px",
    marginRight: "100px",
    border: "1px solid #007bff",
  },
  textArea: {
    width: "95%",
    padding: "0.8rem",
    borderRadius: "8px",
    border: "1px solid #007bff",
    minHeight: "120px",
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
    padding: "10px",
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
  noteSection: {
    backgroundColor: "#e0f0ff",
    padding: "1rem",
    borderRadius: "8px",
  },
  noteText: {
    color: "#007bff",
    fontSize: "0.95rem",
  },
  addressSection: {
    display: "flex",
    flexDirection: "column",
    gap: "1rem",
  },
  paymentSection: {
    backgroundColor: "#e6f0ff",
    border: "2px dashed #28a745",
    borderRadius: "10px",
    height: "140px",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    position: "relative",
    overflow: "hidden",
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

export default UploadCampaign;
