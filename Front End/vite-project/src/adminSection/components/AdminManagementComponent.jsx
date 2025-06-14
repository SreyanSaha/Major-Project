import React, { useState , useEffect} from 'react';
import { useNavigate } from "react-router-dom";
import axios from "axios";

const AdminProfileList = () => {
  const navigate = useNavigate();
  const [msg, updateMsg] = useState(null);
  const [admins, setAdmins] = useState([]);
  const [searchText, setSearchText] = useState('');
  const [adminRole, setRole] = useState(1);

  useEffect(() => {
    try {
      const admin = JSON.parse(localStorage.getItem("admin"));
      if (admin?.username && admin?.token && admin?.role === 1) {
        fetchAllAdmins();
      } else navigate("/admin/login");
    } catch (err) {
      console.error("Error parsing user from localStorage:", err);
      updateMsg("Failed to fetch your data, please login again.");
      navigate("/admin/login");
    }
    }, []);

  const fetchAllAdmins= async()=>{
    try {
      const admin = JSON.parse(localStorage.getItem("admin"));
      const response = await axios.get("http://localhost:8080/admin/get/all-admins", {
        headers: {
          "Authorization": "Bearer " + admin.token,
          "Content-Type": "application/json"
        }
      });
      if (response.status === 200) {
        setAdmins(response.data.objects);
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
      const response = await axios.get(`http://localhost:8080/admin/search/${searchText}`, {
        headers: {
          "Authorization": "Bearer " + admin.token,
          "Content-Type": "application/json"
        }
      });
      if (response.status === 200) {
        setAdmins(response.data.objects);
        updateMsg(null);
      } else if (response.status === 202) {
        updateMsg(response.data.msg);
      }
    } catch (exception) {
      console.log(exception);
      navigate("/admin/login");
    }
  };

  const handleApprove= async(adminId) => {
    try {
      const admin = JSON.parse(localStorage.getItem("admin"));
      const response = await axios.post("http://localhost:8080/admin/approve",
      {
        adminId, 
        adminRole
      }, 
      {
        headers: {
          "Authorization": "Bearer " + admin.token,
          "Content-Type": "application/json"
        }
      });
      if (response.status === 200) {
        fetchAllAdmins();
      } else if (response.status === 202) {
        updateMsg(response.data.msg);
      }
    } catch (exception) {
      console.log(exception);
      navigate("/admin/login");
    }
  };
  const handleReject= async(adminId) => {
    try {
      const admin = JSON.parse(localStorage.getItem("admin"));
      const response = await axios.post("http://localhost:8080/admin/reject",adminId, 
      {
        headers: {
          "Authorization": "Bearer " + admin.token,
          "Content-Type": "application/json"
        }
      });
      if (response.status === 200) {
        fetchAllAdmins();
      } else if (response.status === 202) {
        updateMsg(response.data.msg);
      }
    } catch (exception) {
      console.log(exception);
      navigate("/admin/login");
    }
  };
  const handleDelete= async(adminId) => {
    try {
      const admin = JSON.parse(localStorage.getItem("admin"));
      const response = await axios.post("http://localhost:8080/admin/delete",adminId, 
      {
        headers: {
          "Authorization": "Bearer " + admin.token,
          "Content-Type": "application/json"
        }
      });
      if (response.status === 200) {
        fetchAllAdmins();
        setAdmins(
            admins.filter(admin=>admin.adminId!==adminId)
        );
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
    width:"100%",
    backgroundColor: '#f4faff',
    padding: '20px',
    height: '95vh',
    overflowY: 'auto',
    border: '1px solid #ccc',
    borderRadius: '10px',
  },
  searchBar: {
    marginBottom: '20px',
    display: 'flex',
    gap: '10px',
  },
  searchInput: {
    padding: '8px',
    borderRadius: '5px',
    border: '1px solid #ccc',
    flexGrow: 1,
  },
  searchButton: {
    padding: '8px 12px',
    backgroundColor: '#007bff',
    color: '#fff',
    border: 'none',
    borderRadius: '5px',
    cursor: 'pointer',
  },
  card: {
    backgroundColor: '#e6f0ff',
    padding: '15px',
    marginBottom: '20px',
    borderRadius: '8px',
    boxShadow: '0 2px 5px rgba(0,0,0,0.1)',
    display: 'flex',
    alignItems: 'flex-start',
    gap: '15px',
  },
  profilePic: {
    width: '100px',
    height: '100px',
    borderRadius: '8px',
    objectFit: 'cover',
    border: '2px solid #0056b3',
  },
  content: {
    flex: 1,
  },
  title: {
    color: '#0056b3',
    fontSize: '18px',
    fontWeight: 'bold',
    marginBottom: '8px',
  },
  field: {
    marginBottom: '6px',
    color: '#003366',
    fontSize: '14px',
  },
  label: {
    fontWeight: 'bold',
    marginRight: '5px',
  },
  button: {
    padding: '6px 10px',
    backgroundColor: '#007bff',
    color: '#fff',
    border: 'none',
    borderRadius: '5px',
    cursor: 'pointer',
    marginRight: '10px',
    marginTop: '10px',
  },
  deleteBtn: {
    backgroundColor: 'red',
  },
  select: {
    padding: '6px 8px',
    marginTop: '10px',
    marginBottom: '10px',
    borderRadius: '4px',
    border: '1px solid #ccc',
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
        <h2 style={styles.heading}>Manage Admin Access Control</h2>
        {msg!=null?(<div style={styles.alertDiv}>
              <h3 style={styles.alertText}>{msg}</h3>
            </div>):""}
      <div style={styles.searchBar}>
        <input
          style={styles.searchInput}
          type="text"
          placeholder="Search by name, email, company, or department..."
          value={searchText}
          onChange={(e) => setSearchText(e.target.value)}
        />
        <button style={styles.searchButton} onClick={handleSearch}>Search</button>
      </div>

      {admins.map((admin) => (
        <div key={admin.adminId} style={styles.card}>
          <img
            src={admin.profileImagePath!==null && admin.profileImagePath!==undefined? 
                `http://localhost:8080/media${admin.profileImagePath.replace("\\","/")}`:
                "Admin profile image"}
            alt="Profile"
            style={styles.profilePic}
          />
          <div style={styles.content}>
            <div style={styles.title}>
              {admin.adminFirstName} {admin.adminLastName} ({admin.adminCompanyName})
            </div>
            <div style={styles.field}><span style={styles.label}>Email:</span> {admin.adminEmailId}</div>
            <div style={styles.field}><span style={styles.label}>Phone:</span> {admin.adminPhoneNumber}</div>
            <div style={styles.field}><span style={styles.label}>Department:</span> {admin.adminDepartment}</div>
            <div style={styles.field}><span style={styles.label}>Location:</span> {admin.street}, {admin.city}, {admin.state} - {admin.zipCode}, {admin.country}</div>
            <div style={styles.field}><span style={styles.label}>Status:</span> {{
              0: 'Inactive',
              1: 'Active',
            }[admin.adminStatus]}</div>
            <div style={styles.field}><span style={styles.label}>Role:</span>{admin.adminRole===1?"Admin":admin.adminRole===2?"Super Admin":"Inactive"}</div>

            <div>
              <label style={{ marginRight: '10px' }}>Role:</label>
              <select
                style={styles.select}
                value={adminRole}
                onChange={(e) => setRole(e.target.value)}
              >
                <option value={1} defaultChecked>Admin</option>
                <option value={2}>Super Admin</option>
              </select>
            </div>

            <button style={styles.button} onClick={() => handleApprove(admin.adminId)}>Approve</button>
            <button style={styles.button} onClick={() => handleReject(admin.adminId)}>Reject</button>
            <button style={{ ...styles.button, ...styles.deleteBtn }} onClick={() => handleDelete(admin.adminId)}>Delete</button>
          </div>
        </div>
      ))}
    </div>
  );
};

export default AdminProfileList;
