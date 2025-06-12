import React, { useState , useEffect} from "react";
import { useNavigate } from "react-router-dom";
import LoadingOverlay from "../../loadingComponents/Loading";
import axios from "axios";
import UpdatePostForm from "./EditPostComponent";

const AISubscriptionCard = ({ subscription }) => {
    const navigate = useNavigate();
    const [msg,updateMsg] = useState(null);
    const [processing, setProcessing] = useState(false);
    const [userSubscriptionDetails, setUserSubscription] = useState({});
    const amount=299;

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
        if(response.status===200){
          setUserSubscription(response.data.object);
          updateMsg(response.data.msg);
          setProcessing(false);
        }
        else if(response.status===202){
          updateMsg(response.data.msg);
          setUserSubscription(response.data.object);
          setProcessing(false);
        }
    }catch(exception){
      console.log(exception);
      setProcessing(false);
      updateMsg("Connection with the server failed.");
    }
}

const getOrderId= async()=>{
  try{
      const user = JSON.parse(localStorage.getItem("user"));
      setProcessing(true);
      const response = await axios.post("http://localhost:8080/payment/create-order",{},
        {
          headers:{
            "Authorization": "Bearer "+user.token,
            "Content-Type": "application/json"
          }
        }
      );
      if(response.status===200){setProcessing(false);return response.data;}
      else {updateMsg("payment system error."); setProcessing(false); return null;}
    }catch(exception){
      console.log(exception);
    }
};

  const handleSubscription= async()=>{
    const oId = await getOrderId();
    if(oId!==null){
      const options = {
      key: "rzp_test_Xq2JJkU6biRFF2", 
      amount: amount,
      currency: "INR",
      name: "Road Help",
      description: "AI Subscription",
      order_id: oId,
      handler: async function (response) {
        const PaymentVerificationData={
          razorpay_payment_id: response.razorpay_payment_id,
          razorpay_order_id: response.razorpay_order_id,
          razorpay_signature: response.razorpay_signature,
        };
        try{
          const user = JSON.parse(localStorage.getItem("user"));
          const response = await axios.post('http://localhost:8080/payment/verify-start-subscription', PaymentVerificationData, 
            {
              headers:{
              "Authorization": "Bearer "+user.token,
              "Content-Type": "application/json"
              }
            }
          );
          if(response.status===200 && response.data)loadUserSubscriptionDetails();
          else updateMsg("We couldn’t complete your subscription. A refund of ₹299 has been initiated.");
        }catch(exception){
          console.log(exception);
        }
      },
      theme: { color: "#007bff" }
    };

    const rzp = new window.Razorpay(options);
    rzp.open();
    }
    
  };

  return (
  <>
  {processing?<LoadingOverlay/>:""}
    {msg!=null?(<div style={alertDiv}>
            <h3 style={alertText}>{msg}</h3>
          </div>):""}
    <div style={cardStyle}>
      <h2 style={headingStyle}>AI Subscription</h2>
      {userSubscriptionDetails!==null? (
        <>
          <p style={detailStyle}>
            <strong>Subscription Status:</strong> Active ✅
          </p>
          <p style={detailStyle}>
            <strong>Start Date:</strong> {new Date(userSubscriptionDetails.startDate).toLocaleString()}
          </p>
          <p style={detailStyle}>
            <strong>End Date:</strong> {new Date(userSubscriptionDetails.endDate).toLocaleString()}
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
