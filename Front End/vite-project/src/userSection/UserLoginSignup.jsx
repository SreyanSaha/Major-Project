import { useState, useEffect } from "react";
import axios from "axios";

const UserSignup = (props) => {
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [aadhaar, setAadhaar] = useState("");
  const [phoneno, setNumber] = useState("");
  const [useLiveLocation, setUseLiveLocation] = useState(false);
  const [latitude, setLatitude] = useState("");
  const [longitude, setLongtitude] = useState("");
  const [address, setAddress] = useState("");// { street: "", city: "", state: "", zip: "" }
  const [city, setCity] = useState("");
  const [state, setState] = useState("");
  const [zip, setZip] = useState("");
  const [login, setLogin] = useState(props.status);

  const [otpSent, setOtpSent] = useState(false);
  const [otp, setOtp] = useState("");
  const [otpVerified, setOtpVerified] = useState(false);
  const [msg,updateMsg] = useState(null);
  const [timer, setTimer] = useState(0);
  const [timerActive, setTimerActive] = useState(false);

  const handleLiveLocation = () => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          setLatitude(position.coords.latitude);
          setLongtitude(position.coords.longitude);
          //setLocation(`Lat: ${position.coords.latitude}, Lng: ${position.coords.longitude}`);
          setUseLiveLocation(true);
          updateMsg("Location fetched.");
        },
        (error) => updateMsg("Error fetching location")
      );
    } else {
      updateMsg("Geolocation is not supported by this browser.");
    }
  };

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
    updateMsg("Please wait");
    try{
        const response = await axios.post("http://localhost:8080/auth/user/email/otp",email,{
            headers:{
            "Content-Type": "text/plain"
            }
        });
        if(response.data===true){
            updateMsg("Otp Email sent");
            setTimer(60);
            setOtpSent(true);
            setTimerActive(true);
        }else {
            updateMsg("Failed to send Otp Email");
            setOtpSent(false);
            setTimerActive(false);
        }
    }catch(exception){
        console.log(exception);
        updateMsg("An error occurred while sending OTP email.");
        setOtpSent(false);
        setTimerActive(false);
    }
  };

  const validatePassword = ()=>{
    return password===confirmPassword?false:true;
  };

  const proceedForSignup = async ()=>{
    if(validatePassword()===false){updateMsg("Passwords are not matching!");return;}
    try{
      const response = await axios.post("http://localhost:8080/auth/user/register",
        {
          registerRequest:{
            "username":username,
            "password":password,
            "userTypeRole":0
          },
          user:{
            "userFirstName":firstName,
            "userLastName":lastName,
            "aadharCardNumber":aadhaar,
            "userEmailId":email,
            "userPhoneNumber":phoneno,
            "latitude":latitude,
            "longitude":longitude,
            "street":address,
            "city":city,
            "state":state,
            "zipCode":zip
          }
        },
        {
          headers:{
              "Content-Type": "application/json"
          }
        }
      );
      if(response.status===201)updateMsg("Signup successfull.");
      else if(response.status===401)updateMsg("Signup failed.");
    }catch(exception){
      console.log(exception);
      updateMsg("An error occurred while during signup process.");
    }
  };

  const handleVerifyOtp = async () => {
    updateMsg("Verifying OTP...");
    try{
        const response = await axios.post("http://localhost:8080/auth/user/otp/verify",
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
      background: "linear-gradient(135deg, #e6f0ff, #cce0ff)",
      color: "#001f3f",
    },
    card: {
      width: "700px",
      padding: "30px",
      background: "rgba(255, 255, 255, 0.8)",
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
      color: "#003366",
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
      padding: "12px",
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
    return (
    <div style={styles.container}>
        <div style={styles.wrapper}>
          <div style={styles.sidebar}>
            <h2 style={styles.title}>User Responsibilities</h2>
            <ul style={styles.list}>
              <li style={styles.listItem}>Report road issues with location & images</li>
              <li style={styles.listItem}>Participate in donation and help campaigns</li>
              <li style={styles.listItem}>Assist during emergencies nearby</li>
              <li style={styles.listItem}>Stay updated on campaigns and posts</li>
              <li style={styles.listItem}>Help improve your local community</li>
            </ul>
          </div>
        </div>
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
          <p style={styles.Text} onClick={() => setLogin(true)}>
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
            <h2 style={styles.title}>User Responsibilities</h2>
            <ul style={styles.list}>
              <li style={styles.listItem}>Report road issues with location & images</li>
              <li style={styles.listItem}>Participate in donation and help campaigns</li>
              <li style={styles.listItem}>Assist during emergencies nearby</li>
              <li style={styles.listItem}>Stay updated on campaigns and posts</li>
              <li style={styles.listItem}>Help improve your local community</li>
            </ul>
          </div>
        </div>

        <div style={styles.card}>
          <h2 style={styles.title}>User Signup</h2>
          {msg!=null?(<div style={styles.alertDiv}>
            <h3 style={styles.alertText}>{msg}</h3>
          </div>):""}
          <form style={styles.form}>
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
              //onChange={(e) => setEmail(e.target.value)}
              style={styles.input}
              readOnly
            />

            <input
              type="text"
              placeholder="Aadhaar Card Number"
              value={aadhaar}
              onChange={(e) => setAadhaar(e.target.value)}
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

            <button
              type="button"
              style={styles.button}
              onClick={handleLiveLocation}
              onMouseOver={(e) => Object.assign(e.target.style, styles.buttonHover)}
              onMouseOut={(e) => Object.assign(e.target.style, styles.button)}
            >
              {useLiveLocation ? "Location Added ✅" : "Add Live Location"}
            </button>

            {!useLiveLocation && (
              <>
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
              </>
            )}
            <button
              type="submit"
              style={styles.button}
              onClick={proceedForSignup}
            >
              Signup
            </button>
            <p
              style={styles.Text}
              onClick={() => setLogin(true)}
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
            <h2 style={styles.title}>User Responsibilities</h2>
            <ul style={styles.list}>
              <li style={styles.listItem}>Log in to access road help services</li>
              <li style={styles.listItem}>Post issues or emergency reports</li>
              <li style={styles.listItem}>Join community campaigns and events</li>
              <li style={styles.listItem}>Track post updates and responses</li>
            </ul>
          </div>
        </div>

        <div style={styles.card}>
          <h2 style={styles.title}>User Login</h2>
          <form style={styles.form}>
            <input
              type="email"
              placeholder="Email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
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
              style={styles.button}
            >
              Login
            </button>
          </form>
          <p
            style={styles.Text}
            onClick={() => setLogin(false)}
          >
            Don't have an account?... click here.
          </p>
        </div>
      </div>
    )
  );
};

export default UserSignup;
