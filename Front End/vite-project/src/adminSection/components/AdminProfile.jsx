import React from 'react';
import { useState , useEffect} from 'react';
import { useNavigate } from "react-router-dom";
import axios from "axios";

const AdminProfileCard = () => {
    const [admin, setAdmin] = useState({});
    const navigate = useNavigate();
  const [msg, updateMsg] = useState(null);

    useEffect(() => {
            try {
              const admin = JSON.parse(localStorage.getItem("admin"));
              if (admin?.username && admin?.token && admin?.role === 1) {
                fetchAdminProfile();
              } else navigate("/admin/login");
            } catch (err) {
              console.error("Error parsing user from localStorage:", err);
              updateMsg("Failed to fetch your data, please login again.");
              navigate("/admin/login");
            }
    }, []);

    const fetchAdminProfile = async()=>{
    try {
      const admin = JSON.parse(localStorage.getItem("admin"));
      const response = await axios.get("http://localhost:8080/admin/profile", {
        headers: {
          "Authorization": "Bearer " + admin.token,
          "Content-Type": "application/json"
        }
      });
      if (response.status === 200) {
        setAdmin(response.data.object);
      } else if (response.status === 202) {
        updateMsg(response.data.msg);
      }
    } catch (exception) {
      console.log(exception);
      navigate("/admin/login");
    }
    };

    const styles = {
  container: {
    width: '800px',
    margin: '40px auto',
    padding: '30px',
    borderRadius: '10px',
    backgroundColor: '#ffffff',
    boxShadow: '0 4px 12px rgba(0,0,0,0.1)',
    fontFamily: 'Arial, sans-serif',
    color: '#003366',
    overflowY: 'auto',
  },
  header: {
    textAlign: 'center',
    fontSize: '24px',
    marginBottom: '20px',
    color: '#003366',
    fontWeight: 'bold',
  },
  profileRow: {
    display: 'flex',
    flexWrap: 'wrap',
    marginBottom: '15px',
  },
  label: {
    flex: '1 1 30%',
    fontWeight: 'bold',
    marginBottom: '5px',
  },
  value: {
    flex: '1 1 70%',
    marginBottom: '5px',
  },
  profileImage: {
    display: 'block',
    margin: '0 auto 20px auto',
    width: '120px',
    height: '120px',
    borderRadius: '50%',
    objectFit: 'cover',
    border: '3px solid #0077cc',
  },
    };
  
    return (
    <div style={styles.container}>
      <img
        src={admin.profileImagePath!==null && admin.profileImagePath!==undefined?
            `http://localhost:8080/media${admin.profileImagePath.replace("\\","/")}`:
        "profile image"}
        alt="Admin Profile"
        style={styles.profileImage}
      />
      <div style={styles.header}>
        {admin.adminFirstName} {admin.adminLastName}
      </div>

      <div style={styles.profileRow}>
        <div style={styles.label}>Email:</div>
        <div style={styles.value}>{admin.adminEmailId}</div>
      </div>

      <div style={styles.profileRow}>
        <div style={styles.label}>Phone Number:</div>
        <div style={styles.value}>{admin.adminPhoneNumber}</div>
      </div>

      <div style={styles.profileRow}>
        <div style={styles.label}>Role:</div>
        <div style={styles.value}>{admin.adminRole === 1 ? 'Admin' : 'Super Admin'}</div>
      </div>

      <div style={styles.profileRow}>
        <div style={styles.label}>Status:</div>
        <div style={styles.value}>
          {{
            0: 'Inactive / Timeout',
            1: 'Active',
            2: 'Deleted',
            3: 'Blacklisted',
          }[admin.adminStatus]}
        </div>
      </div>

      <div style={styles.profileRow}>
        <div style={styles.label}>Signup Date:</div>
        <div style={styles.value}>{new Date(admin.signupDateTime).toLocaleString()}</div>
      </div>

      <div style={styles.profileRow}>
        <div style={styles.label}>Timeout End:</div>
        <div style={styles.value}>{new Date(admin.timeOutEndTime).toLocaleString()}</div>
      </div>

      <div style={styles.profileRow}>
        <div style={styles.label}>Latitude:</div>
        <div style={styles.value}>{admin.latitude}</div>
      </div>

      <div style={styles.profileRow}>
        <div style={styles.label}>Longitude:</div>
        <div style={styles.value}>{admin.longitude}</div>
      </div>

      <div style={styles.profileRow}>
        <div style={styles.label}>Street:</div>
        <div style={styles.value}>{admin.street}</div>
      </div>

      <div style={styles.profileRow}>
        <div style={styles.label}>City:</div>
        <div style={styles.value}>{admin.city}</div>
      </div>

      <div style={styles.profileRow}>
        <div style={styles.label}>State:</div>
        <div style={styles.value}>{admin.state}</div>
      </div>

      <div style={styles.profileRow}>
        <div style={styles.label}>Country:</div>
        <div style={styles.value}>{admin.country}</div>
      </div>

      <div style={styles.profileRow}>
        <div style={styles.label}>ZIP Code:</div>
        <div style={styles.value}>{admin.zipCode}</div>
      </div>

      <div style={styles.profileRow}>
        <div style={styles.label}>Department:</div>
        <div style={styles.value}>{admin.adminDepartment}</div>
      </div>

      <div style={styles.profileRow}>
        <div style={styles.label}>Employee ID:</div>
        <div style={styles.value}>{admin.adminEmployeeId}</div>
      </div>

      <div style={styles.profileRow}>
        <div style={styles.label}>Company Name:</div>
        <div style={styles.value}>{admin.adminCompanyName}</div>
      </div>
    </div>
  );
};

export default AdminProfileCard;
