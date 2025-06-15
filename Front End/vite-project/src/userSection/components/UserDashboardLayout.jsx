import React, { useState , useEffect} from "react";
import { useNavigate } from "react-router-dom";
import LoadingOverlay from "../../loadingComponents/Loading";
import axios from "axios";
import UploadPost from "./UploadPostComponent";
import UploadCampaign from "./UploadCampaignComponent";
import UploadEmergencyPost from "./UploadEmergencyComponent";

export default function UserDashboardLayout() {
  // system and ui
  const navigate = useNavigate();
  const [msg,updateMsg] = useState(null);
  const [processing, setProcessing] = useState(false);
  const [search, setSearch] = useState("");
  // 
  // page state of all it type of posts 
  const [page, setPage] = useState(0);
  const [campaignPage, setCampaignPage] = useState(0);
  const [emergencyPage, setEmergencyPage] = useState(0);
  //
  // site related state management 
  const [activeTab, setActiveTab] = useState("posts");
  const [showAddPost, setShowAddPost] = useState(false);
  const [isLoggedin, setLogin] = useState(false);
  //
  // all the types of posts states 
  const [posts, setPost] = useState([]);
  const [campaignPosts, setCampaigns] = useState([]);
  const [emergencyPosts, setEmergencyPost] = useState([]);
  //
  // all the types of posts page state management 
  const [lastPostPage, setLastPostPage] = useState(false);
  const [lastCampaignPage, setLastCampaignPage] = useState(false);
  const [lastEmergencyPage, setLastEmergencyPage] = useState(false);
  //
  // page size for each type of posts 
  const pageSize = 20;
  //

  useEffect(() => {
    const loadData = async () =>{
      const response = await checkTokenHealth();
      if(response){
        if (activeTab === "posts") fetchAllPosts();
        else if(activeTab === "campaigns") fetchAllCampaignPosts();
        else fetchAllEmergencyPosts();
      }else{
        if (activeTab === "posts") fetchPosts();
        else if(activeTab === "campaigns") fetchCampaignPosts();
        else fetchEmergencyPosts();
      }
    }
    loadData();
  },[activeTab]);
//
// Search functions for the 3 types of posts
  const handleSearch = async() => {
    if(activeTab==="posts"){
      setPage(0);
      try{
      const user = JSON.parse(localStorage.getItem("user"));
      setProcessing(true);
      const response=await axios.get(`http://localhost:8080/post/search/page/${0}/size/${pageSize}/${search}`,
        {
          headers:{
            "Authorization": "Bearer "+user.token,
            "Content-Type": "application/json"
          }
        }
      );
      if(response.status===200){
        setPost(response.data.object.content);
        if(!response.data.object.last)setPage(page+1);
        else setLastPostPage(true);
        setProcessing(false);
      }else if(response.status===202){
        updateMsg(response.data.msg);
        setProcessing(false);
      }
    }catch(exception){
      console.log(exception);
      setProcessing(false);
      updateMsg(exception?.response?.data?.msg || exception.message );
    }
    }else if(activeTab==="campaigns"){
      setCampaignPage(0);
      try{
      const user = JSON.parse(localStorage.getItem("user"));
      setProcessing(true);
      const response=await axios.get(`http://localhost:8080/campaign/search/page/${0}/size/${pageSize}/${search}`,
        {
          headers:{
            "Authorization": "Bearer "+user.token,
            "Content-Type": "application/json"
          }
        }
      );
      if(response.status===200){
        setCampaigns(response.data.object.content);
        if(!response.data.object.last)setCampaignPage(page+1);
        else setLastCampaignPage(true);
        setProcessing(false);
      }else if(response.status===202){
        updateMsg(response.data.msg);
        setProcessing(false);
      }
    }catch(exception){
      console.log(exception);
      setProcessing(false);
      updateMsg(exception?.response?.data?.msg || exception.message );
    }
    }else if(activeTab==="emergency"){
      setEmergencyPage(0);
      try{
      const user = JSON.parse(localStorage.getItem("user"));
      setProcessing(true);
      const response=await axios.get(`http://localhost:8080/emergency-post/search/page/${0}/size/${pageSize}/${search}`,
        {
          headers:{
            "Authorization": "Bearer "+user.token,
            "Content-Type": "application/json"
          }
        }
      );
      if(response.status===200){
        setEmergencyPost(response.data.object.content);
        if(!response.data.object.last)setEmergencyPage(page+1);
        else setLastEmergencyPage(true);
        setProcessing(false);
      }else if(response.status===202){
        updateMsg(response.data.msg);
        setProcessing(false);
      }
    }catch(exception){
      console.log(exception);
      setProcessing(false);
      updateMsg(exception?.response?.data?.msg || exception.message );
    }
    }
  };
//
// health check func for the token
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
//
// if user is new || token is expired then these funcs will be called to provide
// only lates ten posts of all the types 
  const fetchPosts = async() =>{
    try{
      if(posts.length===0){
        setProcessing(true);
        const response=await axios.get("http://localhost:8080/posts",
          {
            headers:{
              "Content-Type": "application/json"
            }
          }
        );
        if(response.status===200){
          setProcessing(false);
          setPost(response.data.objects);
          updateMsg(response.data.objects.length===0?"Posts not found.":response.data.msg);
        }
      }
    }catch(exception){
      console.log(exception);
      setProcessing(false);
      updateMsg("Connection with the server failed.");
    }
  }
  const fetchCampaignPosts = async() =>{
    try{
      if(campaignPosts.length===0){
        setProcessing(true);
        const response=await axios.get("http://localhost:8080/campaigns",
          {
            headers:{
              "Content-Type": "application/json"
            }
          }
        );
        if(response.status===200){
          setProcessing(false);
          setCampaigns(response.data.objects);
          updateMsg(response.data.objects.length===0?"Campaigs not found.":response.data.msg);
        }
      }
    }catch(exception){
      console.log(exception);
      setProcessing(false);
      updateMsg("Connection with the server failed.");
    }
  }
  const fetchEmergencyPosts = async() =>{
    try{
      if(emergencyPosts.length===0){
        setProcessing(true);
        const response=await axios.get("http://localhost:8080/emergency",
          {
            headers:{
              "Content-Type": "application/json"
            }
          }
        );
        if(response.status===200){
          setProcessing(false);
          setEmergencyPost(response.data.objects);
          updateMsg(response.data.objects.length===0?"Emergency posts not found.":response.data.msg);
        }
      }
    }catch(exception){
      console.log(exception);
      setProcessing(false);
      updateMsg("Connection with the server failed.");
    }
  }
//
// if the user is regular one && token is not expired then these funcs will 
// be called to provide all the posts with in a paged format 20 posts of all types 
// per request  
  const fetchAllPosts = async()=>{
    try{
      if(!lastPostPage){
        const user = JSON.parse(localStorage.getItem("user"));
        setProcessing(true);
        const response=await axios.get(`http://localhost:8080/all-posts/page/${page}/size/${pageSize}`,
          {
            headers:{
              "Authorization": "Bearer "+user.token,
              "Content-Type": "application/json"
            }
          }
        );
        if(response.status===200){
          setProcessing(false);
          if(posts.length===0) setPost(response.data.object.content);
          else setPost(prevPost=>[...prevPost, ...response.data.object.content]);
          
          if(!response.data.object.last)setPage(page+1);
          else setLastPostPage(true);
        }else if(response.status===202){
          setProcessing(false);
          updateMsg(response.data.msg);
        }
      }
    }catch(exception){
      console.log(exception);
      setProcessing(false);
      updateMsg(exception?.response?.data?.msg || exception.message );
    }
  }
  const fetchAllCampaignPosts = async()=>{
    try{
      if(!lastCampaignPage){
        const user = JSON.parse(localStorage.getItem("user"));
        setProcessing(true);
        const response=await axios.get(`http://localhost:8080/all-campaigns/page/${campaignPage}/size/${pageSize}`,
          {
            headers:{
              "Authorization": "Bearer "+user.token,
              "Content-Type": "application/json"
            }
          }
        );
        if(response.status===200){
          setProcessing(false);
          if(campaignPosts.length===0)setCampaigns(response.data.object.content);
          else setCampaigns(prevCampaigns=>[...prevCampaigns, ...response.data.object.content]);
          
          if(!response.data.object.last)setCampaignPage(campaignPage+1);
          else setLastCampaignPage(true);
        }else if(response.status===202){
          setProcessing(false);
          updateMsg(response.data.msg);
        }
      }
    }catch(exception){
      console.log(exception);
      setProcessing(false);
      updateMsg(exception?.response?.data?.msg || exception.message );
    }
  }
  const fetchAllEmergencyPosts = async()=>{
    try{
      if(!lastEmergencyPage){
        const user = JSON.parse(localStorage.getItem("user"));
        setProcessing(true);
        const response=await axios.get(`http://localhost:8080/all-emergency/page/${emergencyPage}/size/${pageSize}`,
          {
            headers:{
              "Authorization": "Bearer "+user.token,
              "Content-Type": "application/json"
            }
          }
        );
        if(response.status===200){
          if(emergencyPosts.length===0) setEmergencyPost(response.data.object.content);
          else setEmergencyPost(prevEmergencyPost=>[...prevEmergencyPost, ...response.data.object.content]);
          
          if(!response.data.object.last)setEmergencyPage(page+1);
          else setLastEmergencyPage(true);
          setProcessing(false);
        }else if(response.status===202){
          setProcessing(false);
          updateMsg(response.data.msg);
        }
      }
    }catch(exception){
      console.log(exception);
      setProcessing(false);
      updateMsg(exception?.response?.data?.msg || exception.message );
    }
  }
//
// load more will evaluate which one func to call for the next set of posts 
  const loadMore = async () =>{
    if(activeTab==="posts"){
      try{
      if(!lastPostPage){
        const user = JSON.parse(localStorage.getItem("user"));
        setProcessing(true);
        const response=await axios.get(`http://localhost:8080/all-posts/page/${page}/size/${pageSize}`,
          {
            headers:{
              "Authorization": "Bearer "+user.token,
              "Content-Type": "application/json"
            }
          }
        );
        if(response.status===200){
          if(posts.length===0) setPost(response.data.object.content);
          else setPost(prevPost=>[...prevPost, ...response.data.object.content]);
          
          if(!response.data.object.last)setPage(page+1);
          else setLastPostPage(true);
          setProcessing(false);
        }else if(response.status===202){
          updateMsg(response.data.msg);
          setProcessing(false);
        }
      }
    }catch(exception){
      console.log(exception);
      setProcessing(false);
      updateMsg(exception?.response?.data?.msg || exception.message );
    }
    }else if(activeTab==="campaigns"){
      try{
      if(!lastCampaignPage){
        const user = JSON.parse(localStorage.getItem("user"));
        setProcessing(true);
        const response=await axios.get(`http://localhost:8080/all-campaigns/page/${campaignPage}/size/${pageSize}`,
          {
            headers:{
              "Authorization": "Bearer "+user.token,
              "Content-Type": "application/json"
            }
          }
        );
        if(response.status===200){
          setProcessing(false);
          if(campaignPosts.length===0)setCampaigns(response.data.object.content);
          else setCampaigns(prevCampaigns=>[...prevCampaigns, ...response.data.object.content]);
          
          if(!response.data.object.last)setCampaignPage(campaignPage+1);
          else setLastCampaignPage(true);
        }else if(response.status===202){
          setProcessing(false);
          updateMsg(response.data.msg);
        }
      }
    }catch(exception){
      console.log(exception);
      setProcessing(false);
      updateMsg(exception?.response?.data?.msg || exception.message );
    }
    }else if(activeTab==="emergency"){
      try{
      if(!lastEmergencyPage){
        const user = JSON.parse(localStorage.getItem("user"));
        setProcessing(true);
        const response=await axios.get(`http://localhost:8080/all-emergency/page/${emergencyPage}/size/${pageSize}`,
          {
            headers:{
              "Authorization": "Bearer "+user.token,
              "Content-Type": "application/json"
            }
          }
        );
        if(response.status===200){
          if(emergencyPosts.length===0) setEmergencyPost(response.data.object.content);
          else setEmergencyPost(prevEmergencyPost=>[...prevEmergencyPost, ...response.data.object.content]);
          
          if(!response.data.object.last)setEmergencyPage(page+1);
          else setLastEmergencyPage(true);
          setProcessing(false);
        }else if(response.status===202){
          setProcessing(false);
          updateMsg(response.data.msg);
        }
      }
    }catch(exception){
      console.log(exception);
      setProcessing(false);
      updateMsg(exception?.response?.data?.msg || exception.message );
    }
    }
  }
//


  const getTrustScoreLabel = (score) => {
  if (score >= 0 && score <= 299) return "Low Trust 🔴";
  if (score >= 300 && score <= 599) return "Moderate Trust 🟠";
  if (score >= 600 && score <= 849) return "High Trust 🟡";
  if (score >= 850 && score <= 1000) return "Trusted Citizen 🟢";
  return "";
};

const getTrustScoreStyle = (score) => {
  if (score >= 0 && score <= 299) return { color: "#f44336" };     // Red
  if (score >= 300 && score <= 599) return { color: "#ff9800" };   // Orange
  if (score >= 600 && score <= 849) return { color: "#ffc107" };   // Yellow/Gold
  if (score >= 850 && score <= 1000) return { color: "#4caf50" };  // Green
  return {};
};

  const renderCards = () => {
    if (activeTab === "posts") {
      return posts.map((post) => (
      <div key={post.postId} style={styles.card} onClick={()=>navigate(`/post/${post.postId}`)}>

      <div style={styles.uploaderInfo}>
      <img
        src={post.authorProfileImagePath!==null?`http://localhost:8080/media${post.authorProfileImagePath.replace("\\", "/")}`:"post image"}
        alt={post.uploaderName}
        style={styles.profileImage}
      />
      <div>
        <span style={styles.uploaderName}>{post.authorProfileName}</span>
        <span style={{ ...styles.trustScoreLabel, ...getTrustScoreStyle(post.civicTrustScore) }}>
          {getTrustScoreLabel(post.civicTrustScore)}
        </span>
      </div>
      <div style={styles.uploadDate}>
      {new Date(post.postUploadDateTime).toLocaleString()}
      </div>
      </div>

        <div style={styles.imagePlaceholder}><img src={`http://localhost:8080/media${post.imagePath1.replace("\\", "/")}`} 
        style={{
          height: "100%",
          objectFit: "cover",
          borderRadius: "8px",
          flex: 1,
        }}/></div>
        <div style={styles.title}>{post.postTitle}</div>
        <div style={styles.desc}>{post.postDescription.length > 120?
                                  post.postDescription.substring(0, 120) + "..."
                                  :post.postDescription}</div>

      <div style={styles.postStats}>
      <span style={styles.upvote}>⮝ {post.upVoteCount}</span>
      <span style={styles.downvote}>⮟ {post.downVoteCount}</span>
      <span style={styles.comments}>✉ {post.commentCount}</span>
      <span style={styles.reports}>⚠️ {post.postReports}</span>
      <div style={styles.title}>{post.postStatus===-1?"Under Review":post.postStatus===0?"Work In Progress":"Completed"}</div>
      </div>

      </div>
    ));
    }
    else if(activeTab === "campaigns") {
      return campaignPosts.map((campaigns)=>(
        <div key={campaigns.campaignId} style={styles.card} onClick={()=>navigate(`/campaign/${campaigns.campaignId}`)}>

        <div style={styles.uploaderInfo}>
      <img
        src={campaigns.campaignOrganizerProfileImagePath!==null && campaigns.campaignOrganizerProfileImagePath!==undefined?
          `http://localhost:8080/media${campaigns.campaignOrganizerProfileImagePath.replace("\\", "/")}`:"profile image"}
        alt={campaigns.campaignOrganizerName}
        style={styles.profileImage}
      />
      <div>
        <span style={styles.uploaderName}>{campaigns.campaignOrganizerName}</span>
        <span style={{ ...styles.trustScoreLabel, ...getTrustScoreStyle(campaigns.civicTrustScore) }}>
          {getTrustScoreLabel(campaigns.civicTrustScore)}
        </span>
      </div>
      <div style={styles.uploadDate}>
      {new Date(campaigns.campaignCreationTime).toLocaleString()}
      </div>
      </div>

        <div style={styles.imagePlaceholder}><img src={`http://localhost:8080/media${campaigns.imagePath1.replace("\\", "/")}`}/></div>
        <div style={styles.title}>{campaigns.campaignTitle}</div>
        <div style={styles.desc}>{campaigns.campaignDescription}</div>

        <div style={styles.postStats}>
      <span style={styles.upvote}>⮝ {campaigns.upVoteCount}</span>
      <span style={styles.downvote}>⮟ {campaigns.downVoteCount}</span>
      <span style={styles.campaignType}>
        🎯 {campaigns.campaignType === -1
          ? "Donation"
          : campaigns.campaignType === 0
          ? "Awareness"
          : "Volunteer"}
      </span>
      <span style={styles.status}>
        🏁 {campaigns.status === 1 ? "Completed" : "Active"}
      </span>
      <span style={styles.reports}>⚠️ {campaigns.campaignReports}</span>
      </div>

      </div>
      ));
    }
    else if(activeTab === "emergency"){
      return emergencyPosts.map((post) => (
  <div
    key={post.emergencyPostId}
    style={styles.card}
    onClick={() => navigate(`/emergency/${post.emergencyPostId}`)}
  >
    {/* Uploader Info */}
    <div style={styles.uploaderInfo}>
      <img
        src={
          post.profileImagePath
            ? `http://localhost:8080/media${post.profileImagePath.replace("\\", "/")}`
            : "profile image"
        }
        alt={post.authorName}
        style={styles.profileImage}
      />
      <div>
        <span style={styles.uploaderName}>{post.authorName}</span>
        <span style={{ ...styles.trustScoreLabel, ...getTrustScoreStyle(post.civicTrustScore) }}>
          {getTrustScoreLabel(post.civicTrustScore)}
        </span>
      </div>
      <div style={styles.uploadDate}>
        {new Date(post.emergencyPostUploadDateTime).toLocaleString()}
      </div>
    </div>

    {/* Emergency Image */}
    <div style={styles.imagePlaceholder}>
      <img
        src={post.imagePath1!==null && post.imagePath1!==undefined?
          `http://localhost:8080/media${post.imagePath1.replace("\\", "/")}`:
        "Image"}
        style={{
          height: "100%",
          objectFit: "cover",
          borderRadius: "8px",
          flex: 1,
        }}
        alt="Emergency"
      />
    </div>

    {/* Title and Description */}
    <div style={styles.title}>{post.emergencyPostTitle}</div>
    <div style={styles.desc}>{post.emergencyPostDescription}</div>

    {/* Address Info */}
    <div style={styles.address}>
      📍 {post.street || ""}, {post.city || ""}, {post.state || ""} {post.zip || ""}
    </div>
  </div>
  ))
    }
  };

  const styles = {
    address: {
  marginTop: "8px",
  fontSize: "14px",
  color: "#666",
  fontStyle: "italic",
},
    campaignType: {
  padding: "2px 8px",
  backgroundColor: "#e3f2fd",
  color: "#0d47a1",
  borderRadius: "6px",
  fontSize: "0.85rem",
  fontWeight: "500",
},

status: {
  padding: "2px 8px",
  backgroundColor: "#f1f8e9",
  color: "#33691e",
  borderRadius: "6px",
  fontSize: "0.85rem",
  fontWeight: "500",
},
    trustScoreLabel: {
    fontWeight: "600",
    fontSize: "0.75rem",
    marginLeft: "8px",
    verticalAlign: "middle",
    userSelect: "none",
  },
    uploaderInfo: {
    display: "flex",
    alignItems: "center",
    gap: "10px",
    marginBottom: "8px",
  },
  uploadDate: {
    fontSize: "0.8rem",
    color: "#777",
  },
  uploaderImage: {
    width: "40px",
    height: "40px",
    borderRadius: "50%",
    objectFit: "cover",
  },
  uploaderDetails: {
    display: "flex",
    flexDirection: "column",
  },

    profileImage: {
      width: "32px",
      height: "32px",
      borderRadius: "50%",
      objectFit: "cover",
      border: "1.5px solid #11398f",
    },

    uploaderName: {
      fontWeight: "600",
      color: "#11398f",
      fontSize: "0.9rem",
    },

    postStats: {
      display: "flex",
      justifyContent: "flex-start",
      gap: "20px",
      fontSize: "0.85rem",
      color: "#555",
      marginTop: "auto", 
    },

    upvote: {
      color: "green",
      cursor: "pointer",
    },

    downvote: {
      color: "red",
      cursor: "pointer",
    },

    comments: {
      color: "#333",
      cursor: "pointer",
    },
    reports: {
      color: "#ff9800",  // orange for reports
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
      flexDirection: "column",
      minHeight: "100vh",
      backgroundColor: "#f5f9ff",
      color: "#11398f",
      fontFamily: "Arial, sans-serif",
      position: "relative",
    },
    main: {
      flex: 1,
      padding: "1.5rem",
      display: "flex",
      flexDirection: "column",
      gap: "1rem",
    },
    tabColumn: {
      display: "flex",
      flexDirection: "column",
      gap: "1rem",
    },
    navBlock: {
      padding: "1rem",
      backgroundColor: "#ffffff",
      border: "1px solid #dce4f1",
      borderRadius: "10px",
      textAlign: "center",
      fontWeight: "600",
      fontSize: "1.1rem",
      cursor: "pointer",
      color: "#ff0000",
      transition: "background-color 0.3s, transform 0.2s",
    },
    navRow: {
      display: "flex",
      gap: "1rem",
    },
    navItem: {
      flex: 1,
      padding: "1rem",
      backgroundColor: "#ffffff",
      border: "1px solid #dce4f1",
      borderRadius: "10px",
      textAlign: "center",
      fontWeight: "600",
      fontSize: "1rem",
      cursor: "pointer",
      color: "#11398f",
      transition: "background-color 0.3s, transform 0.2s",
    },
    navItemActive: {
      backgroundColor: "#11398f",
      color: "#ffffff",
      transform: "scale(1.02)",
    },
    searchBoxWrapper: {
      marginTop: "1rem",
      display: "flex",
      justifyContent: "center",
      alignItems: "center",
      gap: "0.5rem",
    },
    searchBox: {
      width: "100%",
      maxWidth: "500px",
      padding: "0.75rem 1rem",
      borderRadius: "8px",
      border: "1px solid #ccc",
      fontSize: "1rem",
      backgroundColor: "#ffffff",
      color: "#11398f",
      outline: "none",
    },
    searchButton: {
      padding: "0.75rem 1.5rem",
      borderRadius: "8px",
      backgroundColor: "#11398f",
      color: "#ffffff",
      fontSize: "1rem",
      border: "none",
      cursor: "pointer",
    },
    sectionTitle: {
      marginTop: "1rem",
      fontSize: "1.5rem",
      fontWeight: "600",
      textAlign: "center",
      color: "#11398f",
    },
    cardGrid: {
      marginTop: "1rem",
      display: "grid",
      gridTemplateColumns: "repeat(auto-fit, minmax(350px, 1fr))",
      gap: "1.5rem",
    },
    card: {
      height: "350px",
      width:"420px",
      backgroundColor: "#ffffff",
      padding: "1rem",
      borderRadius: "10px",
      border: "1px solid #dce4f1",
      display: "flex",
      flexDirection: "column",
      gap: "0.75rem",
      boxShadow: "0 2px 5px rgba(0,0,0,0.05)",
      transition: "transform 0.2s",
      cursor: "pointer",
    },
    imagePlaceholder: {
    backgroundColor: "#cce0ff",
    height: "190px",
    borderRadius: "8px",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    gap: "0.5rem",
    },
    title: {
      fontSize: "1rem",
      fontWeight: "600",
      color: "#11398f",
    },
    desc: {
      fontSize: "0.9rem",
      color: "#555",
    },
    fab: {
      position: "fixed",
      bottom: "30px",
      right: "30px",
      width: "60px",
      height: "60px",
      borderRadius: "50%",
      backgroundColor: "#11398f",
      color: "#ffffff",
      fontSize: "2rem",
      display: "flex",
      alignItems: "center",
      justifyContent: "center",
      cursor: "pointer",
      boxShadow: "0 4px 10px rgba(0,0,0,0.3)",
      zIndex: 1000,
    },
  };

  return (
    <div style={styles.container}>
      {processing?<LoadingOverlay/>:""}
      <div style={styles.main}>
        <div style={styles.tabColumn}>
          <div
            style={{
              ...styles.navBlock,
              backgroundColor: activeTab === "emergency" ? "#ffe5e5" : "#ffffff",
            }}
            onClick={() => {
              setActiveTab("emergency");
              setShowAddPost(false);
            }}
          >
            🚨 Emergency
          </div>

          <div style={styles.navRow}>
            <div
              style={{
                ...styles.navItem,
                ...(activeTab === "posts" ? styles.navItemActive : {}),
              }}
              onClick={() => {
                setActiveTab("posts");
                setShowAddPost(false);
              }}
            >
              📝 Posts
            </div>
            <div
              style={{
                ...styles.navItem,
                ...(activeTab === "campaigns" ? styles.navItemActive : {}),
              }}
              onClick={() => {
                setActiveTab("campaigns");
                setShowAddPost(false);
              }}
            >
              📢 Campaigns
            </div>
          </div>
        </div>

        {!showAddPost && (
          <div style={styles.searchBoxWrapper}>
            <input
              type="text"
              placeholder={
                activeTab === "posts"
                  ? "Search for posts..."
                  : activeTab === "campaigns"
                  ? "Search for campaigns..."
                  : "Search for Emergency Posts..."
              }
              onChange={(e)=>setSearch(e.target.value)}
              style={styles.searchBox}
            />
            <button style={styles.searchButton} onClick={()=>handleSearch()}>
              Search
            </button>
          </div>
        )}

        {msg!=null?(<div style={styles.alertDiv}>
            <h3 style={styles.alertText}>{msg}</h3>
          </div>):""}
        
        <div style={styles.sectionTitle}>
          {activeTab === "posts"
            ? "📝 Posts"
            : activeTab === "campaigns"
            ? "📢 Campaigns"
            : "🚨 Emergency"}
        </div>
      
        {!showAddPost ? (
          <div style={styles.cardGrid}>
            {renderCards()}
          </div>
        ) : activeTab === "posts" ? (
          <UploadPost />
        ) : activeTab === "campaigns" ? (
          <UploadCampaign />
        ) : (
          <UploadEmergencyPost />
        )}
      </div>

      {!isLoggedin && (
        <div style={styles.fab} onClick={() => setShowAddPost(true)}>
          +
        </div>
      )}
      <button
        style={{
          marginTop: "85%",
          padding: "0.75rem 3rem",
          fontSize: "1rem",
          backgroundColor: "#11398f",
          color: "white",
          border: "none",
          borderRadius: "8px",
          cursor: "pointer",
          position: "fixed",
          bottom: "30px",
          boxShadow: "0 4px 10px rgba(0,0,0,0.3)",
          zIndex: 1000,
          left: "30px",
        }}
        onClick={()=>loadMore()}
      >
        Load More
      </button>
    </div>
  );
}
