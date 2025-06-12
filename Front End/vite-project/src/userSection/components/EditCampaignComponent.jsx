import { useState , useEffect} from "react";
import LoadingOverlay from "../../loadingComponents/Loading";
import axios from "axios";
import { useNavigate } from "react-router-dom";

export default function EditCampaign({onBack, campaignId}) {
  const navigate = useNavigate();
  const [authenticated, setAuthenticated] = useState(false);
  const [msg,updateMsg] = useState(null);
  const [address, setAddress] = useState("");
  const [city, setCity] = useState("");
  const [state, setState] = useState("");
  const [zipCode, setZipCode] = useState("");
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [campaignType, setCampaignType] = useState(-1);
  const [processing, setProcessing] = useState(false);
  const [id, setId] = useState(-1);

  useEffect(() => {
        if (localStorage.getItem("user")!==null){
          setAuthenticated(true);
          fetchFullCampaignData();
        } else{
          setAuthenticated(false);
          navigate("/user/login");
        }
      }, [navigate]);

  const fetchFullCampaignData = async()=>{
    try{
      const user = JSON.parse(localStorage.getItem("user"));
      setProcessing(true);
      const response=await axios.get(`http://localhost:8080/campaign/data-to-edit/${campaignId}`,
        {
          headers:{
            "Authorization": "Bearer "+user.token,
            "Content-Type": "application/json"
          }
        }
      );
      if(response.status===200){
        setTitle(response.data.object.campaignTitle);
        setDescription(response.data.object.campaignDescription);
        setCampaignType(response.data.object.campaignType);
        setAddress(response.data.object.street);
        setState(response.data.object.state);
        setZipCode(response.data.object.postalCode);
        setCity(response.data.object.city);
        setId(response.data.object.campaignId);
        setProcessing(false);
      }else if(response.status===202){
        setProcessing(false);
        updateMsg(response.data.msg);
      }
    }catch(exception){
      console.log(exception);
      setProcessing(false);
      updateMsg(exception?.response?.data?.msg || exception.message );
      navigate("/user/login");
    }
  };

  const handleSubmit = async() => {
    setProcessing(true);
    try{
      const user=JSON.parse(localStorage.getItem("user"));
      const editCampaignData={
        "campaignId":id,
        "campaignTitle":title,
        "campaignDescription":description,
        "street":address,
        "city":city,
        "state":state,
        "postalCode":zipCode,
        "campaignType":campaignType
      };
      const response = await axios.post("http://localhost:8080/campaign/edit",editCampaignData,{
        headers:{
            "Authorization": "Bearer "+user.token,
            "Content-Type": "application/json"
          }
      });
      if(response.status===200){
        setProcessing(false);
        updateMsg("Campaing updated.");
      }
      else if(response.status===202){
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
      <div onClick={() => {onBack();}} style={styles.backArrow}>
      ← Back
      </div>
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

        <textarea
          placeholder="Enter Campaign Description (Include Payment Details)"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          style={styles.textArea}
          required
        />

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
        <button type="submit" style={styles.submitButton}>
          Update Campaign
        </button>
      </form>
    </div>
  );
}

const styles = {
    backArrow: {
    cursor: "pointer",
    fontSize: "1.5rem",
    color: "#004080",
    fontWeight: "bold",
    marginBottom: "-90px",
    display: "inline-block",
    backgroundColor: "#e3f2fd",
    padding: "0.4rem 0.8rem",
    borderRadius: "6px",
    boxShadow: "0 2px 5px rgba(0, 0, 0, 0.1)",
    transition: "background-color 0.2s ease-in-out",
    width: "fit-content",
    marginBottom:"1px",
    marginRight:"650px"
  },
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

