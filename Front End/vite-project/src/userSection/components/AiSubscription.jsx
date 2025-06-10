import React, { useState , useEffect} from "react";
import { useNavigate } from "react-router-dom";
import LoadingOverlay from "../../loadingComponents/Loading";
import axios from "axios";

const AISubscriptionCard = ({ subscription }) => {
    const navigate = useNavigate();
    const [msg,updateMsg] = useState(null);
    const [processing, setProcessing] = useState(false);
    const [userSubscriptionDetails, setUserSubscription] = useState({});

  const cardStyle = {
    backgroundColor: '#ffffff',
    border: '2px solid #007bff',
    borderRadius: '12px',
    padding: '24px',
    maxWidth: '400px',
    color: '#007bff',
    boxShadow: '0 4px 12px rgba(0, 123, 255, 0.2)',
    margin: '20px auto',
    fontFamily: 'Arial, sans-serif',
  };
    const alertDiv={
      textAlign: "center",
    };
    const alertText= {
        backgroundColor: "rgb(255, 64, 57)",
        padding: "2px",
        color: "white",
        borderRadius: "8px",
        width: "50%",
        maxWidth: "80%",
        margin: "auto",
        marginBottom: "15px",
    };
  const headingStyle = {
    fontSize: '22px',
    fontWeight: 'bold',
    marginBottom: '16px',
    color: '#004080',
  };
  const detailStyle = {
    fontSize: '16px',
    marginBottom: '12px',
  };
  const buttonStyle = {
    backgroundColor: '#007bff',
    color: '#fff',
    border: 'none',
    borderRadius: '8px',
    padding: '10px 20px',
    fontSize: '16px',
    cursor: 'pointer',
  };

  const checkTokenHealth = async() =>{
    try{
      let user = localStorage.getItem("user");
      if(user===null)return false;
      else user = JSON.parse(user);
      const response = await axios.post("http://localhost:8080/token/health",{},
        {
          headers:{
            "Authorization": "Bearer "+user.token,
            "Content-Type": "application/json"
          }
        }
      );
      if(response.status===200 && response.data==="validated.")return true;
    }catch(exception){
      if(exception.response && (exception.response.status===401 || exception.response.status===403)){
        updateMsg("Please log in to access updates — your session may have expired or you're not signed in.");
        localStorage.removeItem("user");
      }
    }
    return false;
  } 

  useEffect(() => {
  const loadData = async()=>{
    const response = await checkTokenHealth();
    if(response){
        const script = document.createElement('script');
        script.src = 'https://checkout.razorpay.com/v1/checkout.js';
        script.async = true;
        document.body.appendChild(script);
        loadUserSubscriptionDetails();
    }else navigate("/user/login");
    }
    loadData();
}, []);

const loadUserSubscriptionDetails= async()=>{
    try{
        setProcessing(true);
        const response=await axios.get("http://localhost:8080/user/subscription/details",{},
          {
            headers:{
              "Authorization": "Bearer "+user.token,
              "Content-Type": "application/json"
            }
          }
        );
        if(response.status===200){
            
          setProcessing(false);
        }
    }catch(exception){
      console.log(exception);
      setProcessing(false);
      updateMsg("Connection with the server failed.");
    }
}

  const handleSubscription= async()=>{
    
    const options = {
      key: "YOUR_KEY_ID", // Replace with your Razorpay Key ID
      amount: order.amount,
      currency: "INR",
      name: "Your App",
      description: "AI Subscription",
      order_id: order.id,
      handler: function (response) {
        axios.post('/api/payment/verify', {
          razorpay_payment_id: response.razorpay_payment_id,
          razorpay_order_id: response.razorpay_order_id,
          razorpay_signature: response.razorpay_signature,
        })
        .then(res => alert("Payment Verified!"))
        .catch(err => alert("Verification Failed"));
      },
      theme: { color: "#007bff" }
    };

    const rzp = new window.Razorpay(options);
    rzp.open();
 
  };

const dummyInactiveSubscription = {
  isActive: true,
};
  return (
  <>
  {processing?<LoadingOverlay/>:""}
    <div style={cardStyle}>
        {msg!=null?(<div style={styles.alertDiv}>
            <h3 style={styles.alertText}>{msg}</h3>
          </div>):""}
      <h2 style={headingStyle}>AI Subscription</h2>
      {subscription && dummyInactiveSubscription.isActive ? (
        <>
          <p style={detailStyle}>
            <strong>Subscription Status:</strong> Active ✅
          </p>
          <p style={detailStyle}>
            <strong>Start Date:</strong> {subscription.startDate}
          </p>
          <p style={detailStyle}>
            <strong>End Date:</strong> {subscription.endDate}
          </p>
        </>
      ) : (
        <>
          <p style={detailStyle}>You don't have an active subscription.</p>
          <p style={detailStyle}>
            <strong>Fee:</strong> ₹299<br />
            <strong>Duration:</strong> 1 month
          </p>
          <button style={buttonStyle} onClick={handleSubscription}>Subscribe Now</button>
        </>
      )}
    <script src="https://checkout.razorpay.com/v1/checkout.js"></script>
    </div>
  </>
  );
};

export default AISubscriptionCard;
