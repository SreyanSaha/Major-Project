import { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import LoadingOverlay from "../loadingComponents/Loading";

const AdminSignup=(props)=>{
  const navigate = useNavigate();
  const [username, setUsername] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [phoneno, setNumber] = useState("");
  const [useLiveLocation, setUseLiveLocation] = useState(false);
  const [latitude, setLatitude] = useState("");
  const [longitude, setLongtitude] = useState("");
  const [address, setAddress] = useState("");// { street: "", city: "", state: "", zip: "" }
  const [city, setCity] = useState("");
  const [state, setState] = useState("");
  const [zip, setZip] = useState("");
  const [organizationName, setOrganization] = useState("");
  const [department, setDepartment] = useState("");
  const [empId, setEmpId] = useState("");
  const [profileImage, setProfileImage] = useState(null);
  const [processing, setProcessing] = useState(false);

  const [otpSent, setOtpSent] = useState(false);
  const [otp, setOtp] = useState("");
  const [otpVerified, setOtpVerified] = useState(false);
  const [msg,updateMsg] = useState(null);
  const [timer, setTimer] = useState(0);
  const [timerActive, setTimerActive] = useState(false);
  const [login, setlogin] = useState(props.status);


  useEffect(() => {
    let interval = null;
    if (timerActive && timer > 0) {
      interval = setInterval(() => {
        setTimer((prevTimer) => prevTimer - 1);
      }, 1000);
    } else if (timer === 0) {
      setTimerActive(false);
    }
    return () => clearInterval(interval);

  }, [timerActive, timer]);

  const sendOtp = async () => {
    setProcessing(true);
    try{
        const response = await axios.post("http://localhost:8080/auth/admin/email/otp",email,{
            headers:{
            "Content-Type": "text/plain"
            }
        });
        if(response.data===true){
            updateMsg("Otp Email sent");
            setProcessing(false);
            setTimer(60);
            setOtpSent(true);
            setTimerActive(true);
        }else {
            setProcessing(false);
            updateMsg("Failed to send Otp Email");
            setOtpSent(false);
            setTimerActive(false);
        }
    }catch(exception){
        console.log(exception);
        setProcessing(false);
        updateMsg("An error occurred while sending OTP email.");
        setOtpSent(false);
        setTimerActive(false);
    }
  };

  const proceedForSignup = async ()=>{
    if(password!==confirmPassword)updateMsg("Passwords are not matching!");
    else{
      setProcessing(true);
      try{
        const formData=new FormData();
        const wrapper={
          registerRequest:{
            "username":username,
            "password":password,
            "userTypeRole":1
          },
          admin:{
            "adminFirstName":firstName,
            "adminLastName":lastName,
            "adminEmailId":email,
            "adminPhoneNumber":phoneno,
            "latitude":latitude,
            "longitude":longitude,
            "street":address,
            "city":city,
            "state":state,
            "zipCode":zip,
            "adminDepartment":department,
            "adminEmployeeId":empId,
            "adminCompanyName":organizationName
          },
          user:{}
        }
        formData.append("wrapper", JSON.stringify(wrapper));
        formData.append("profileImage", profileImage);
        const response = await axios.post("http://localhost:8080/auth/admin/register",formData,
        {
          headers:{
            "Content-Type": "multipart/form-data"
          }
        }
      );
      if(response.status===201 || response.status===200){
        setProcessing(false);
        updateMsg(response.data);
      }
    }catch(exception){
      setProcessing(false);
      console.log(exception);
      updateMsg("An error occurred during signup process.");
    }
    }
  };

useEffect(() => {
      try {
        const admin = JSON.parse(localStorage.getItem("admin"));
        console.log("Fetched amdin's username:", admin?.username);
        if (admin?.username && admin?.token && admin?.role === 1) navigate("/admin/dashboard");
        else localStorage.removeItem("admin");
      } catch (err) {
        localStorage.removeItem("admin");
        console.error("Error parsing user from localStorage:", err);
        updateMsg("Failed to fetch your data, please login again.");
      }
    }, [navigate]);

  const handleLogin = async () => {
    setProcessing(true);
    try{
      const response = await axios.post("http://localhost:8080/auth/admin/login",{
        "username":username,
        "password":password,
        "userTypeRole":1
      },
      {
        withCredentials: true 
      });
      if(response.status===200){
        const {token, username, userTypeRole} = response.data;
        localStorage.setItem(
          "admin",
          JSON.stringify(
          {
            username: username,
            token:token,
            role: userTypeRole
          })
        );
        setProcessing(false);
        navigate("/admin/dashboard");
      }else if(response.status===202){
        updateMsg(response.data);
        setProcessing(false);
      }
    }catch(exception){
      console.log(exception);
      updateMsg("Invalid user details!");
      setProcessing(false);
    }
  };

  const handleVerifyOtp = async () => {
    updateMsg("Verifying OTP...");
    try{
        const response = await axios.post("http://localhost:8080/auth/admin/otp/verify",
            {
                "email": email,
                "otp": otp
            },
            {
                headers:{
                    "Content-Type": "application/json"
                }
            }
        );
        if(response.data===-1)
            updateMsg("Expired Otp");
        else if(response.data===-2)
            updateMsg("Invalid Otp");
        else {
            updateMsg("Otp verified, please fill up the requied details");
            setOtpVerified(true);
            setTimerActive(false);
        }
    }catch(exception){
        console.log(exception);
        updateMsg("An error occurred while verifying the OTP.");
    }
  };

  const styles = {
    timerDiv:{
        textAlign: "center",
    },
    timer: {
        backgroundColor: "rgb(26, 144, 255)",
        padding: "2px",
        color: "white",
        borderRadius: "8px",
        width: "40%",
        margin: "auto",
        marginBottom: "15px",
    },
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
      display: "flex",
      justifyContent: "center",
      alignItems: "center",
      height: "100vh",
      background: "linear-gradient(135deg, #e6f0ff, #cce0ff)", // Light blue gradient
      color: "#001f3f", // Deep navy blue text
      overflowY: 'auto',
    },
    card: {
      width: "700px",
      padding: "30px",
      background: "rgba(255, 255, 255, 0.8)", // Soft white glassy effect
      backdropFilter: "blur(10px)",
      borderRadius: "15px",
      boxShadow: "0 10px 25px rgba(0, 0, 128, 0.1)",
      textAlign: "center",
    },
    title: {
      fontSize: "26px",
      fontWeight: "600",
      marginBottom: "20px",
      letterSpacing: "1px",
      color: "#003366", // Medium-dark blue
    },
    form: {
      display: "flex",
      flexDirection: "column",
      gap: "15px",
    },
    inputRow: {
      display: "flex",
      gap: "10px",
    },
    input: {
      flex: 1,
      padding: "12px",
      border: "1px solid rgba(0, 0, 128, 0.2)",
      borderRadius: "8px",
      fontSize: "15px",
      backgroundColor: "rgba(255, 255, 255, 0.5)",
      color: "#003366",
      outline: "none",
      transition: "0.5s",
    },
    inputFocus: {
      border: "1px solid #3399ff",
      boxShadow: "0 0 8px rgba(51, 153, 255, 0.5)",
    },
    button: {
      marginLeft:"5px",
      padding: "12px",
      background: "linear-gradient(45deg, #3399ff, #0077cc)",
      color: "#fff",
      border: "none",
      borderRadius: "8px",
      cursor: "pointer",
      fontSize: "16px",
      fontWeight: "bold",
      transition: "0.3s",
    },
    buttonHover: {
      background: "linear-gradient(45deg, #0077cc, #005fa3)",
    },
    Text: {
      marginTop: "20px",
      color: "#003366",
      cursor: "pointer",
      fontSize: "20px",
      textDecoration: "none",
      transition: "0.3s",
    },
    loginTextHover: {
      color: "#3399ff",
    },
    Button: {
      padding: "5px",
      background: "linear-gradient(45deg, #3399ff, #0077cc)",
      color: "#fff",
      border: "none",
      borderRadius: "8px",
      cursor: "pointer",
      fontSize: "16px",
      fontWeight: "bold",
      transition: "0.3s",
      marginLeft: "10px",
    },
    wrapper: {
      display: "flex",
      width: "500px",
      borderRadius: "15px",
      boxShadow: "0 10px 25px rgba(0, 0, 128, 0.1)",
      overflow: "hidden",
      background: "rgba(255, 255, 255, 0.8)",
      height: "35vh",
      marginRight: "15px",
    },
    sidebar: {
      flex: 1,
      padding: "30px",
      display: "flex",
      flexDirection: "column",
      justifyContent: "center",
      background: "rgba(255, 255, 255, 0.7)",
      backdropFilter: "blur(10px)",
      borderRight: "1px solid rgba(0, 0, 128, 0.2)",
    },
    list: {
      listStyle: "none",
      padding: "0",
    },
    listItem: {
      marginBottom: "10px",
      fontSize: "16px",
      color: "#005fa3",
    },
  };

  if (!otpVerified && !login) {
    return(
      <div style={styles.container}>
      <div style={styles.wrapper}>
      <div style={styles.sidebar}>
        <h2 style={styles.title}>Admin Responsibilities</h2>
        <ul style={styles.list}>
          <li style={styles.listItem}>Manage user accounts and permissions</li>
          <li style={styles.listItem}>Approve or reject vendor products</li>
          <li style={styles.listItem}>Monitor and resolve reported issues</li>
          <li style={styles.listItem}>Ensure platform security and data integrity</li>
          <li style={styles.listItem}>Oversee content moderation and compliance</li>
        </ul>
      </div>
      </div>
        {processing?<LoadingOverlay/>:""}
        <div style={styles.card}>
            <h2 style={styles.title}>Verify Email</h2>
            {msg!=null?(<div style={styles.alertDiv}>
              <h3 style={styles.alertText}>{msg}</h3>
            </div>):""}
            {!otpSent ? (
              <>
                <input
                  type="email"
                  placeholder="Enter your email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  style={styles.input}
                />
                <button
                  type="button"
                  style={styles.button}
                  onClick={sendOtp}
                >
                  Send OTP
                </button>
              </>
            ) : (
              <>
              {timerActive?(<div style={styles.timerDiv}>
                  <h3 style={styles.timer}>Time remaining: {timer}</h3>
              </div>):""}
                <input
                  type="text"
                  placeholder="Enter OTP"
                  value={otp}
                  onChange={(e) => setOtp(e.target.value)}
                  style={styles.input}
                />
                <button
                  type="button"
                  style={styles.button}
                  onClick={handleVerifyOtp}
                >
                  Verify OTP
                </button>
                {timer===0?(<button
                  type="button"
                  style={styles.button}
                  onClick={sendOtp}
                >
                  resend OTP
                </button>):""}
              </>
            )}
            <p style={styles.Text} onClick={() => setlogin(true)}>
              Already have an account?... click here.
            </p>
          </div>
      </div>
    );
  }
  return (
    !login?(
      <div style={styles.container}>
        <div style={styles.wrapper}>
          <div style={styles.sidebar}>
            <h2 style={styles.title}>Admin Responsibilities</h2>
            <ul style={styles.list}>
               <li style={styles.listItem}>Manage user accounts and permissions</li>
              <li style={styles.listItem}>Approve or reject vendor products</li>
              <li style={styles.listItem}>Monitor and resolve reported issues</li>
              <li style={styles.listItem}>Ensure platform security and data integrity</li>
              <li style={styles.listItem}>Oversee content moderation and compliance</li>
            </ul>
          </div>
        </div>
        {processing?<LoadingOverlay/>:""}
        <div style={styles.card}>
          <h2 style={styles.title}>Admin Signup</h2>
          {msg!=null?(<div style={styles.alertDiv}>
            <h3 style={styles.alertText}>{msg}</h3>
          </div>):""}
          <form style={styles.form} onSubmit={(e) => { e.preventDefault(); proceedForSignup(); }}>
            <div style={styles.inputRow}>
              <input
                type="text"
                placeholder="First Name"
                value={firstName}
                onChange={(e) => setFirstName(e.target.value)}
                style={styles.input}
                onFocus={(e) => Object.assign(e.target.style, styles.inputFocus)}
                onBlur={(e) => Object.assign(e.target.style, styles.input)}
              />
              <input
                type="text"
                placeholder="Last Name"
                value={lastName}
                onChange={(e) => setLastName(e.target.value)}
                style={styles.input}
                onFocus={(e) => Object.assign(e.target.style, styles.inputFocus)}
                onBlur={(e) => Object.assign(e.target.style, styles.input)}
              />
            </div>

            <input
              type="email"
              placeholder="Email"
              value={email}
              style={styles.input}
              readOnly
            />

            <input
              type="text"
              placeholder="Username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              style={styles.input}
            />

            <input
              type="text"
              placeholder="Phone Number"
              value={phoneno}
              onChange={(e) => setNumber(e.target.value)}
              style={styles.input}
            />

            <div style={styles.inputRow}>
              <input
                type="password"
                placeholder="Password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                style={styles.input}
              />
              <input
                type="password"
                placeholder="Confirm Password"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                style={styles.input}
              />
            </div>
            
            <input
              type="file"
              accept="image/*"
              onChange={(e) => setProfileImage(e.target.files[0])}
              style={styles.input}
            />
            
        <select
          style={styles.input}
          name="department"
          value={department}
          onChange={(e)=> setDepartment(e.target.value)}
          required
        >
          <option value="" disabled>Select Department</option>
          <option value="Public Works">Public Works</option>
          <option value="Electricity">Electricity</option>
          <option value="Water Treatment">Water Treatment</option>
          <option value="Road Maintenance">Road Maintenance</option>
          <option value="Waste Management">Waste Management</option>
        </select>
    

      <input
        style={styles.input}
        type="text"
        name="officeName"
        value={organizationName}
        onChange={(e)=> setOrganization(e.target.value)}
        placeholder="Enter organization name"
        required
      />
   
      <input
        style={styles.input}
        type="text"
        name="empId"
        value={empId}
        onChange={(e)=> setEmpId(e.target.value)}
        placeholder="Enter employee ID"
        required
      />
             
                <input
                  type="text"
                  placeholder="Street Address"
                  value={address.street}
                  onChange={(e) => setAddress(e.target.value )}
                  style={styles.input}
                />
                <div style={styles.inputRow}>
                  <input
                    type="text"
                    placeholder="City"
                    value={address.city}
                    onChange={(e) => setCity(e.target.value )}
                    style={styles.input}
                  />
                  <input
                    type="text"
                    placeholder="State"
                    value={address.state}
                    onChange={(e) => setState(e.target.value )}
                    style={styles.input}
                  />
                </div>
                <input
                  type="text"
                  placeholder="Zip Code"
                  value={address.zip}
                  onChange={(e) => setZip(e.target.value )}
                  style={styles.input}
                />
 
          
            <button
              type="submit"
              style={styles.button}
            >
              Signup
            </button>
            <p
              style={styles.Text}
              onClick={() => setlogin(true)}
            >
              Already have an account?... click here.
            </p>
          </form>
        </div>
      </div>
    ) : (
      <div style={styles.container}>
    <div style={styles.wrapper}>
    <div style={styles.sidebar}>
      <h2 style={styles.title}>Admin Responsibilities</h2>
      <ul style={styles.list}>
          <li style={styles.listItem}>Log in securely with verified credentials</li>
          <li style={styles.listItem}>Access the admin dashboard and tools</li>
          <li style={styles.listItem}>Track system alerts and notifications</li>
          <li style={styles.listItem}>Review pending approvals and reports</li>
          <li style={styles.listItem}>Maintain platform performance and quality</li>
      </ul>
    </div>
    </div>
      {processing?<LoadingOverlay/>:""}
      <div style={styles.card}>
        <h2 style={styles.title}>Admin Login</h2>
        {msg!=null?(<div style={styles.alertDiv}>
              <h3 style={styles.alertText}>{msg}</h3>
            </div>):""}
        <form style={styles.form} onSubmit={(e) => { e.preventDefault(); handleLogin(); }}>
          <input
            type="text"
            placeholder="Username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            style={styles.input}
          />
          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            style={styles.input}
          />
          <button
            type="submit"
            style={styles.button}>
            Login
          </button>
        </form>
        <p
          style={styles.Text}
          onClick={() => setlogin(false)}
        >
        Don't have an account?...click here.
        </p>
      </div>
    </div>
    )
  );
};

export default AdminSignup;
