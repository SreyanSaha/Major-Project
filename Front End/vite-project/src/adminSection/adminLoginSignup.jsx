import { useState } from "react";

const AdminSignup=()=>{
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [aadhaar, setAadhaar] = useState("");
  const [useLiveLocation, setUseLiveLocation] = useState(false);
  const [location, setLocation] = useState("");
  const [address, setAddress] = useState({
    street: "",
    city: "",
    state: "",
    zip: "",
  });
  const [login, setlogin] = useState(false);
  const handleLiveLocation = () => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          setLocation(`Lat: ${position.coords.latitude}, Lng: ${position.coords.longitude}`);
          setUseLiveLocation(true);
        },
        (error) => alert("Error fetching location")
      );
    } else {
      alert("Geolocation is not supported by this browser.");
    }
  };

  const styles = {
    container: {
      display: "flex",
      justifyContent: "center",
      alignItems: "center",
      height: "100vh",
      background: "linear-gradient(135deg, #0b0b0b, #1a1a1a)",
      color: "#fff",
    },
    card: {
      width: "700px",
      padding: "30px",
      background: "rgba(255, 255, 255, 0.1)",
      backdropFilter: "blur(15px)",
      borderRadius: "15px",
      boxShadow: "0 10px 25px rgba(255, 255, 255, 0.08)",
      textAlign: "center",
    },
    title: {
      fontSize: "26px",
      fontWeight: "600",
      marginBottom: "20px",
      letterSpacing: "1px",
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
      border: "1px solid rgba(255, 255, 255, 0.2)",
      borderRadius: "8px",
      fontSize: "15px",
      backgroundColor: "rgba(255, 255, 255, 0.15)",
      color: "#fff",
      outline: "none",
      transition: "0.5s",
    },
    inputFocus: {
      border: "1px solid #ffcc00",
      boxShadow: "0 0 8px rgba(255, 204, 0, 0.5)",
    },
    button: {
      padding: "12px",
      background: "linear-gradient(45deg, #ffcc00, #ff6600)",
      color: "#111",
      border: "none",
      borderRadius: "8px",
      cursor: "pointer",
      fontSize: "16px",
      fontWeight: "bold",
      transition: "0.3s",
    },
    buttonHover: {
      background: "linear-gradient(45deg, #ff9900, #ff3300)",
    },
    loginText: {
        marginTop: "20px",
        color: "#ffff",
        cursor: "pointer",
        fontSize: "14px",
        textDecoration: "underline",
        transition: "0.3s",
    },
    loginTextHover: {
        color: "#ff9900",
    },
    loginButton: {
        padding: "5px",
        background: "linear-gradient(45deg, #ffcc00, #ff6600)",
        color: "#111",
        border: "none",
        borderRadius: "8px",
        cursor: "pointer",
        fontSize: "16px",
        fontWeight: "bold",
        transition: "0.3s",
    },
    wrapper: {
      display: "flex",
      width: "500px", 
      borderRadius: "15px",
      boxShadow: "0 10px 25px rgba(255, 255, 255, 0.08)",
      overflow: "hidden",
      background: "rgba(255, 255, 255, 0.1)",
      height: "35vh",
      marginRight: "15px",
    },
    sidebar: {
      flex: 1,
      padding: "30px",
      display: "flex",
      flexDirection: "column",
      justifyContent: "center",
      background: "rgba(255, 255, 255, 0.1)",
      backdropFilter: "blur(15px)",
      borderRight: "1px solid rgba(255, 255, 255, 0.2)",
    },
    list: {
      listStyle: "none",
      padding: "0",
    },
    listItem: {
      marginBottom: "10px",
      fontSize: "16px",
      color: "#ffcc00",
    },
  };

  return(
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

    
      <div style={styles.card}>
        <h2 style={styles.title}>Admin Signup</h2>
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
            onChange={(e) => setEmail(e.target.value)}
            style={styles.input}
            onFocus={(e) => Object.assign(e.target.style, styles.inputFocus)}
            onBlur={(e) => Object.assign(e.target.style, styles.input)}
          />

          <input
            type="text"
            placeholder="Aadhaar Card Number"
            value={aadhaar}
            onChange={(e) => setAadhaar(e.target.value)}
            style={styles.input}
            onFocus={(e) => Object.assign(e.target.style, styles.inputFocus)}
            onBlur={(e) => Object.assign(e.target.style, styles.input)}
          />

          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            style={styles.input}
            onFocus={(e) => Object.assign(e.target.style, styles.inputFocus)}
            onBlur={(e) => Object.assign(e.target.style, styles.input)}
          />

          <input
            type="password"
            placeholder="Confirm Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            style={styles.input}
            onFocus={(e) => Object.assign(e.target.style, styles.inputFocus)}
            onBlur={(e) => Object.assign(e.target.style, styles.input)}
          />

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
                onChange={(e) => setAddress({ ...address, street: e.target.value })}
                style={styles.input}
              />
              <div style={styles.inputRow}>
                <input
                  type="text"
                  placeholder="City"
                  value={address.city}
                  onChange={(e) => setAddress({ ...address, city: e.target.value })}
                  style={styles.input}
                />
                <input
                  type="text"
                  placeholder="State"
                  value={address.state}
                  onChange={(e) => setAddress({ ...address, state: e.target.value })}
                  style={styles.input}
                />
              </div>
              <input
                type="text"
                placeholder="Zip Code"
                value={address.zip}
                onChange={(e) => setAddress({ ...address, zip: e.target.value })}
                style={styles.input}
              />
            </>
          )}
          <button
            type="submit"
            style={styles.button}
            onMouseOver={(e) => Object.assign(e.target.style, styles.buttonHover)}
            onMouseOut={(e) => Object.assign(e.target.style, styles.button)}
          >Signup</button>
            <p style={styles.loginText}>Already have an account? <button className="login-link" onClick={() => setlogin(true)} style={styles.loginButton}>Login here</button></p>
        </form>
      </div>
    </div>
    ):(
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

      <div style={styles.card}>
        <h2 style={styles.title}>Admin Login</h2>
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
            style={styles.button}>
            Login
          </button>
        </form>
        <p
          style={styles.signupText}
          onMouseOver={(e) => Object.assign(e.target.style, styles.signupTextHover)}
          onMouseOut={(e) => Object.assign(e.target.style, styles.signupText)}
          onClick={() => setlogin(false)}
        >
        Don't have an account?
        <button className="login-link" onClick={() => setlogin(false)} style={styles.loginButton}>Signup here</button>
        </p>
      </div>
    </div>
    )
  );
};

export default AdminSignup;
