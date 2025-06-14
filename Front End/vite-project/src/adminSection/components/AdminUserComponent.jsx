import React, { useState , useEffect} from 'react';
import { useNavigate } from "react-router-dom";
import axios from "axios";

const UserDirectory = () => {
    const navigate = useNavigate();
    const [msg, updateMsg] = useState(null);
  const [search, setSearch] = useState('');
  const [users, setUsers] = useState([]);

  useEffect(() => {
    try {
      const admin = JSON.parse(localStorage.getItem("admin"));
      if (admin?.username && admin?.token && admin?.role === 1) {
        fetchUsers();
      } else navigate("/admin/login");
    } catch (err) {
      console.error("Error parsing user from localStorage:", err);
      updateMsg("Failed to fetch your data, please login again.");
      navigate("/admin/login");
    }
    }, []);


  const fetchUsers= async()=>{
    try {
      const admin = JSON.parse(localStorage.getItem("admin"));
      const response = await axios.get("http://localhost:8080/admin/all-users", {
        headers: {
          "Authorization": "Bearer " + admin.token,
          "Content-Type": "application/json"
        }
      });
      if (response.status === 200) {
        setUsers(response.data.objects);
      } else if (response.status === 202) {
        updateMsg(response.data.msg);
      }
    } catch (exception) {
      console.log(exception);
      navigate("/admin/login");
    }
  };  

  const handleSearch= async() => {
    try {
      const admin = JSON.parse(localStorage.getItem("admin"));
      const response = await axios.get(`http://localhost:8080/admin/search/all-users/${search}`, {
        headers: {
          "Authorization": "Bearer " + admin.token,
          "Content-Type": "application/json"
        }
      });
      if (response.status === 200) {
        setUsers(response.data.objects);
      } else if (response.status === 202) {
        updateMsg(response.data.msg);
      }
    } catch (exception) {
      console.log(exception);
      navigate("/admin/login");
    }
  };

  const handleTimeout= async(userId) => {
        try {
      const admin = JSON.parse(localStorage.getItem("admin"));
      const response = await axios.post("http://localhost:8080/admin/time-out/user",userId, {
        headers: {
          "Authorization": "Bearer " + admin.token,
          "Content-Type": "application/json"
        }
      });
      if (response.status === 200) {
        fetchUsers();
      } else if (response.status === 202) {
        updateMsg(response.data.msg);
      }
    } catch (exception) {
      console.log(exception);
      navigate("/admin/login");
    }
};

  const handleRemoveTimeout= async(userId)=>{
    try {
      const admin = JSON.parse(localStorage.getItem("admin"));
      const response = await axios.post("http://localhost:8080/admin/remove/time-out/user",userId, {
        headers: {
          "Authorization": "Bearer " + admin.token,
          "Content-Type": "application/json"
        }
      });
      if (response.status === 200) {
        fetchUsers();
      } else if (response.status === 202) {
        updateMsg(response.data.msg);
      }
    } catch (exception) {
      console.log(exception);
      navigate("/admin/login");
    }
  };

  const handleDelete= async(userId) => {
    try {
      const admin = JSON.parse(localStorage.getItem("admin"));
      const response = await axios.delete(`http://localhost:8080/admin/delete/user/${userId}`,{
        headers: {
          "Authorization": "Bearer " + admin.token,
          "Content-Type": "application/json"
        }
      });
      if (response.status === 200) {
        fetchUsers();
      } else if (response.status === 202) {
        updateMsg(response.data.msg);
      }
    } catch (exception) {
      console.log(exception);
      navigate("/admin/login");
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
      width: '90%',
      height: '80vh',
      margin: '30px auto',
      padding: '20px',
      backgroundColor: '#ffffff',
      border: '2px solid #cce5ff',
      borderRadius: '10px',
      overflowY: 'auto',
      boxShadow: '0 4px 12px rgba(0, 0, 255, 0.1)',
      fontFamily: 'Arial, sans-serif',
    },
    searchBox: {
      display: 'flex',
      gap: '10px',
      marginBottom: '20px',
    },
    searchBar: {
      flex: 1,
      padding: '10px',
      borderRadius: '6px',
      border: '1px solid #0077cc',
      fontSize: '16px',
      color: '#003366',
    },
    searchButton: {
      padding: '10px 20px',
      backgroundColor: '#0077cc',
      color: 'white',
      border: 'none',
      borderRadius: '6px',
      cursor: 'pointer',
    },
    userCard: {
      backgroundColor: '#e6f2ff',
      border: '1px solid #0077cc',
      borderRadius: '8px',
      padding: '16px',
      marginBottom: '15px',
      display: 'flex',
      alignItems: 'flex-start',
      gap: '20px',
      boxShadow: '0 2px 6px rgba(0, 0, 100, 0.1)',
    },
    profileImg: {
      width: '80px',
      height: '80px',
      borderRadius: '50%',
      objectFit: 'cover',
      border: '2px solid #0077cc',
    },
    info: {
      flex: 1,
      color: '#003366',
      marginBottom:"10px",
    },
    label: {
      fontWeight: 'bold',
      color: '#0055aa',
      marginBottom:"10px",
    },
    actions: {
      display: 'flex',
      flexDirection: 'column',
      gap: '10px',
    },
    actionBtn: {
      padding: '6px 12px',
      border: 'none',
      borderRadius: '5px',
      cursor: 'pointer',
      fontSize: '14px',
      color: 'white',
    },
    timeoutBtn: {
      backgroundColor: '#ffa500',
    },
    deleteBtn: {
      backgroundColor: '#cc0000',
    },
    heading: {
    fontSize: "28px",
    marginBottom: "20px",
    textAlign: "center",
    color: "#1b3c74",
  },
  };

  return (
    <div style={styles.container}>
        <h2 style={styles.heading}>Manage User Access Control</h2>
        {msg!=null?(<div style={styles.alertDiv}>
              <h3 style={styles.alertText}>{msg}</h3>
            </div>):""}
      <div style={styles.searchBox}>
        <input
          type="text"
          placeholder="Search by name, email, or phone"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          style={styles.searchBar}
        />
        <button onClick={()=>{handleSearch();}} style={styles.searchButton}>Search</button>
      </div>

      {users.map((user) => (
        <div key={user.userId} style={styles.userCard}>
          <img
            src={(user.profileImagePath!==null && user.profileImagePath !==undefined)?
                `http://localhost:8080/media${user.profileImagePath.replace("\\","/")}`:
            "Profile image"}
            alt="Profile"
            style={styles.profileImg}
          />
          <div style={styles.info}>
            <div><span style={styles.label}>Name: </span>{user.userFirstName} {user.userLastName}</div>
            <div><span style={styles.label}>Email: </span>{user.userEmailId}</div>
            <div><span style={styles.label}>Phone: </span>{user.userPhoneNumber}</div>
            <div><span style={styles.label}>Status: </span>
              {{
                0: 'Inactive',
                1: 'Active',
              }[user.userStatus] || 'Unknown'}
            </div>
            <div><span style={styles.label}>Civic Trust Score: </span>{user.civicTrustScore}</div>
            <div><span style={styles.label}>Reports: </span>{user.userReports}</div>
            <div><span style={styles.label}>Location: </span>{user.street}, {user.city}, {user.state} - {user.zipCode}, {user.country}</div>
            <div><span style={styles.label}>Signup Date: </span>{new Date(user.signupDateTime).toLocaleString()}</div>
            {(user.timeOutEndTime!==null && (new Date(user.timeOutEndTime) > new Date()))? (
                <div><span style={styles.label}>TimeOut End Date: </span>{new Date(user.timeOutEndTime).toLocaleString()}</div>
            ):null}
          </div>
          <div style={styles.actions}>
            {user.timeOutEndTime===null || (new Date(user.timeOutEndTime) < new Date())?
            (<button
              style={{ ...styles.actionBtn, ...styles.timeoutBtn }}
              onClick={() => handleTimeout(user.userId)}
            >
              Timeout (1 Week)
            </button>):(
                <button
              style={{ ...styles.actionBtn, ...styles.timeoutBtn }}
              onClick={() => handleRemoveTimeout(user.userId)}
            >
              Remove Timeout
            </button>
            )}
            <button
              style={{ ...styles.actionBtn, ...styles.deleteBtn }}
              onClick={() => handleDelete(user.userId)}
            >
              Delete User
            </button>
          </div>
        </div>
      ))}
    </div>
  );
};

export default UserDirectory;
