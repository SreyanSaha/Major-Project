import React, { useState , useEffect} from "react";
import { useParams } from 'react-router-dom';
import { useNavigate } from "react-router-dom";
import axios from "axios";
import LoadingOverlay from "../../loadingComponents/Loading";

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
  if (score >= 600 && score <= 849) return { color: "#ffc107" };   // Yellow
  if (score >= 850 && score <= 1000) return { color: "#4caf50" };  // Green
  return {};
};

const PostView = () => {
const navigate = useNavigate();
const [authenticated, setAuthenticated] = useState(false);
const { postId } = useParams();
const [post, setPost] = useState({});
const [userId, setUserId] = useState(-1);
const [postComments, setComments] = useState([]);
const [newComment, setNewComment] = useState("");
const [msg,updateMsg] = useState(null);
const [processing, setProcessing] = useState(false);
const [postImages, setImages] = useState([]);
const [imagesAfterWork, setAfterWorkImages] = useState([]);
const [showComments, setShowComments] = useState(false);
const [lastComment, setLastComment] = useState(false);
const [commentPage, setCommentPage] = useState(0);
const commentPageSize = 25;

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
    if (checkTokenHealth() && localStorage.getItem("user")!==null){
      setAuthenticated(true);
      loadPost();
    } else{
      setAuthenticated(false);
      navigate("/user/login");
    }
  }, [navigate]);
// post related functions
//
  const loadPost = async () =>{
    try{
      const user = JSON.parse(localStorage.getItem("user"));
      setProcessing(true);
      const response=await axios.get(`http://localhost:8080/posts/${postId}`,
        {
          headers:{
            "Authorization": "Bearer "+user.token,
            "Content-Type": "application/json"
          }
        }
      );
      if(response.status===200){
        setPost(response.data.object);
        setUserId(response.data.object.userId);
        setImages([
          response.data.object.imagePath1,
          response.data.object.imagePath2,
          response.data.object.imagePath3,
          response.data.object.imagePath4,
          response.data.object.imagePath5
        ]);
        setAfterWorkImages([
          response.data.object.afterWorkImagePath1,
          response.data.object.afterWorkImagePath2,
          response.data.object.afterWorkImagePath3,
          response.data.object.afterWorkImagePath4,
          response.data.object.afterWorkImagePath5
        ]);
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
  const upVotePost = async (id) =>{
    try{
      setProcessing(true);
      const user = JSON.parse(localStorage.getItem("user"));
      const response=await axios.post("http://localhost:8080/post/upVote",id,
        {
          headers:{
            "Authorization": "Bearer "+user.token,
            "Content-Type": "application/json"
          }
        }
      );
      if(response.status===200){
        setPost(response.data.object);
        setUserId(response.data.object.userId);
        setImages([
          response.data.object.imagePath1,
          response.data.object.imagePath2,
          response.data.object.imagePath3,
          response.data.object.imagePath4,
          response.data.object.imagePath5
        ]);
        setAfterWorkImages([
          response.data.object.afterWorkImagePath1,
          response.data.object.afterWorkImagePath2,
          response.data.object.afterWorkImagePath3,
          response.data.object.afterWorkImagePath4,
          response.data.object.afterWorkImagePath5
        ]);
        updateMsg(response.data.msg);
        setProcessing(false);
      }else if(response.status===202){
        setProcessing(false);
        updateMsg(response.data.msg);
      }
    }catch(exception){
      console.log(exception);
      setProcessing(false);
      updateMsg(exception?.response?.data?.msg || exception.message );
    }
  };
  const downVotePost = async (id) =>{
    try{
      setProcessing(true);
      const user = JSON.parse(localStorage.getItem("user"));
      const response=await axios.post("http://localhost:8080/post/downVote",id,
        {
          headers:{
            "Authorization": "Bearer "+user.token,
            "Content-Type": "application/json"
          }
        }
      );
      if(response.status===200){
        setPost(response.data.object);
        setUserId(response.data.object.userId);
        setImages([
          response.data.object.imagePath1,
          response.data.object.imagePath2,
          response.data.object.imagePath3,
          response.data.object.imagePath4,
          response.data.object.imagePath5
        ]);
        setAfterWorkImages([
          response.data.object.afterWorkImagePath1,
          response.data.object.afterWorkImagePath2,
          response.data.object.afterWorkImagePath3,
          response.data.object.afterWorkImagePath4,
          response.data.object.afterWorkImagePath5
        ]);
        updateMsg(response.data.msg);
        setProcessing(false);
      }else if(response.status===202){
        setProcessing(false);
        updateMsg(response.data.msg);
      }
    }catch(exception){
      console.log(exception);
      setProcessing(false);
      updateMsg(exception?.response?.data?.msg || exception.message );
    }
  };
  const reportPost = async (id) =>{
    try{
      setProcessing(true);
      const user = JSON.parse(localStorage.getItem("user"));
      const response=await axios.post("http://localhost:8080/post/report",id,
        {
          headers:{
            "Authorization": "Bearer "+user.token,
            "Content-Type": "application/json"
          }
        }
      );
      if(response.status===200){
        setPost(response.data.object);
        updateMsg(response.data.msg);
        setProcessing(false);
      }else if(response.status===202){
        setProcessing(false);
        updateMsg(response.data.msg);
      }
    }catch(exception){
      console.log(exception);
      setProcessing(false);
      updateMsg(exception?.response?.data?.msg || exception.message );
    }
  };
//
// comment related functions
  const loadComments = async (id)=>{
    try{
      if(postComments.length===0 && !lastComment){
        const user = JSON.parse(localStorage.getItem("user"));
        setProcessing(true);
        const response=await axios.get(`http://localhost:8080/post/all/comments/${id}/page/${commentPage}/size/${commentPageSize}`,
          {
            headers:{
              "Authorization": "Bearer "+user.token,
              "Content-Type": "application/json"
            }
          }
        );
        if(response.status===200){
          if(postComments.length===0 && response.data.object.content.length!==0)
            setComments(response.data.object.content);
          else setComments(prevComments=>[...prevComments, ...response.data.object.content]);
          updateMsg(response.data.msg);
          setLastComment(response.data.object.last);
          if(!response.data.object.last) setCommentPage(commentPage+1);
          else updateMsg("No more comments are available.");
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
  };
  const loadMoreComments = async (id)=>{
      try{
      if(!lastComment){
        const user = JSON.parse(localStorage.getItem("user"));
        setProcessing(true);
        const response=await axios.get(`http://localhost:8080/post/all/comments/${id}/page/${commentPage}/size/${commentPageSize}`,
          {
            headers:{
              "Authorization": "Bearer "+user.token,
              "Content-Type": "application/json"
            }
          }
        );
        if(response.status===200){
          if(postComments.length===0)setComments(response.data.object.content);
          else setComments(prevComments=>[...prevComments, ...response.data.object.content]);
          updateMsg(response.data.msg);
          setLastComment(response.data.object.last);
          if(!response.data.object.last) setCommentPage(commentPage+1);
          else updateMsg("No more comments are available.");
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
  };
  const addComment = async () => {
    try{
      setProcessing(true);
      const user = JSON.parse(localStorage.getItem("user"));
      const response=await axios.post("http://localhost:8080/post/add/comment",
        {
          "postId":postId,
          "commentDescription":newComment,
          "uname":user.username,
        },
        {
          headers:{
            "Authorization": "Bearer "+user.token,
            "Content-Type": "application/json"
          }
        }
      );
      if(response.status===200){
        updateMsg(response.data.msg);
        setNewComment("");
        setComments(prevComments=>[...prevComments, response.data.object]);
        setProcessing(false);
      }else if(response.status===202){
        setProcessing(false);
        updateMsg(response.data.msg);
      }
    }catch(exception){
      console.log(exception);
      setProcessing(false);
      updateMsg(exception?.response?.data?.msg || exception.message );
    }
  };
  const deleteComment = async (id) => {
      try{
      setProcessing(true);
      const user = JSON.parse(localStorage.getItem("user"));
      const response=await axios.delete(`http://localhost:8080/post/delete/comment/${id}`,
        {
          headers:{
            "Authorization": "Bearer "+user.token,
            "Content-Type": "application/json"
          }
        }
      );
      if(response.status===200 && response.data.object){
        updateMsg(response.data.msg);
        setComments(prevComments =>
          prevComments.filter(comment => comment.postCommentId !== id)
        );
        setProcessing(false);
      }else if(response.status===202){
        setProcessing(false);
        updateMsg(response.data.msg);
      }
    }catch(exception){
      console.log(exception);
      setProcessing(false);
      updateMsg(exception?.response?.data?.msg || exception.message );
    }
  };
  const upVoteComment = async (id) =>{
    try{
      setProcessing(true);
      const user = JSON.parse(localStorage.getItem("user"));
      const response=await axios.post("http://localhost:8080/post/comment/upVote",id,
        {
          headers:{
            "Authorization": "Bearer "+user.token,
            "Content-Type": "application/json"
          }
        }
      );
      if(response.status===200){
        updateMsg(response.data.msg);
        if(postComments.length===0)setComments(response.data.object);
        else setComments(prevComments =>
          prevComments.map(comment =>
          comment.postCommentId === response.data.object.postCommentId?
          response.data.object:comment
        ));
        setProcessing(false);
      }else if(response.status===202){
        setProcessing(false);
        updateMsg(response.data.msg);
      }
    }catch(exception){
      console.log(exception);
      setProcessing(false);
      updateMsg(exception?.response?.data?.msg || exception.message );
    }
  };
  const downVoteComment = async (id) =>{
    try{
      setProcessing(true);
      const user = JSON.parse(localStorage.getItem("user"));
      const response=await axios.post("http://localhost:8080/post/comment/downVote",id,
        {
          headers:{
            "Authorization": "Bearer "+user.token,
            "Content-Type": "application/json"
          }
        }
      );
      if(response.status===200){
        updateMsg(response.data.msg);
        if(postComments.length===0)setComments(response.data.object);
        else setComments(prevComments =>
          prevComments.map(comment =>
          comment.postCommentId === response.data.object.postCommentId?
          response.data.object:comment
        ));
        setProcessing(false);
      }else if(response.status===202){
        setProcessing(false);
        updateMsg(response.data.msg);
      }
    }catch(exception){
      console.log(exception);
      setProcessing(false);
      updateMsg(exception?.response?.data?.msg || exception.message );
    }
  };
//

  const styles = {
  addressSection: {
  backgroundColor: "#f1f5f9",
  borderRadius: "8px",
  padding: "1rem",
  marginBottom: "1rem",
  fontSize: "0.95rem",
  color: "#444",
  boxShadow: "inset 0 0 5px rgba(0,0,0,0.05)",
},

addressLabel: {
  fontWeight: "600",
  marginBottom: "0.4rem",
  color: "#11398f",
},

addressLine: {
  marginBottom: "0.3rem",
},
  card: {
    backgroundColor: "#ffffff",
    border: "1px solid #d0e3ff",
    borderRadius: "12px",
    padding: "1.5rem",
    margin: "2rem auto",
    maxWidth: "700px",
    boxShadow: "0 6px 20px rgba(0, 123, 255, 0.12)",
    fontFamily: "'Segoe UI', sans-serif",
  },

  header1: {
    backgroundColor: "#11398f",
    color: "#ffffff",
    padding: "1.2rem 2rem",
    fontSize: "1.5rem",
    fontWeight: "bold",
    textAlign: "center",
    position: "relative",
    marginTop: "-8px",
  },

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
  },

  header: {
    display: "flex",
    alignItems: "center",
    justifyContent: "space-between",
    marginBottom: "1rem",
  },

  profile: {
    display: "flex",
    alignItems: "center",
  },

  profileImage: {
    width: "50px",
    height: "50px",
    borderRadius: "50%",
    objectFit: "cover",
    marginRight: "1rem",
    backgroundColor: "#dbeafe",
  },

  title: {
    fontSize: "1.4rem",
    fontWeight: "bold",
    color: "#004080",
    marginBottom: "0.5rem",
  },

  trustLabel: {
    fontSize: "0.9rem",
    fontWeight: "500",
  },

  description: {
    color: "#333",
    margin: "0.8rem 0 1rem 0",
    lineHeight: "1.6",
  },

  imagesGrid: {
    display: "grid",
    gridTemplateColumns: "1fr 1fr",
    gap: "1rem",
    marginBottom: "1rem",
  },

  postImage: {
    width: "100%",
    borderRadius: "10px",
    border: "1px solid #d0e3ff",
    objectFit: "cover",
  },

  fullWidthImage: {
    gridColumn: "span 2",
  },

  dividerSlide: {
    textAlign: "center",
    fontWeight: "bold",
    fontSize: "1rem",
    margin: "1rem 0",
    color: "#2e7d32",
    backgroundColor: "#f0f4c3",
    padding: "0.75rem",
    borderRadius: "8px",
  },

  actionRow: {
    display: "flex",
    justifyContent: "space-around",
    borderTop: "1px solid #e6f0ff",
    borderBottom: "1px solid #e6f0ff",
    padding: "0.75rem 0",
    margin: "1rem 0",
    fontSize: "0.95rem",
    fontWeight: "500",
  },

  upvote: { color: "green", cursor: "pointer" , fontSize:"20px"},
  downvote: { color: "red", cursor: "pointer" , fontSize:"20px"},
  comments: { color: "#007bff", cursor: "pointer" , fontSize:"20px"},
  reports: { color: "#ff9800", cursor: "pointer" , fontSize:"20px"},

  commentSection: {
    marginTop: "1rem",
    display: "flex",
    flexDirection: "column",
    height: "250px",
    border: "1px solid #ccdfff",
    borderRadius: "6px",
    backgroundColor: "#f8faff",
  },

  commentList: {
    flexGrow: 1,
    overflowY: "auto",
    padding: "0.75rem",
  },

  commentInputContainer: {
    padding: "0.5rem",
    borderTop: "1px solid #ccdfff",
    backgroundColor: "#e6f0ff",
    display: "flex",
    gap: "0.5rem",
  },

  commentInput: {
    flexGrow: 1,
    padding: "0.6rem",
    borderRadius: "6px",
    border: "1px solid #ccdfff",
  },

  addCommentBtn: {
    backgroundColor: "#007bff",
    color: "#fff",
    border: "none",
    borderRadius: "6px",
    padding: "0.4rem 0.9rem",
    cursor: "pointer",
    marginTop: "0.3rem",
  },

  addCommentBtn1: {
    backgroundColor: "#007bff",
    color: "#fff",
    width: "100%",
    border: "none",
    borderRadius: "6px",
    padding: "0.4rem 0.9rem",
    cursor: "pointer",
    marginTop: "0.3rem",
  },

  commentItem: {
    padding: "0.5rem 0",
    borderBottom: "1px solid #e0e0e0",
    display: "flex",
    flexDirection: "column",
    alignItems: "flex-start",
    gap: "0.5rem",
    wordBreak: "break-word",
    width: "100%",
  },

  commentHeader: {
    display: "flex",
    alignItems: "center",
    marginBottom: "0.5rem",
    cursor:"pointer"
  },

  commentAuthorImage: {
    width: "30px",
    height: "30px",
    borderRadius: "50%",
    objectFit: "cover",
    marginRight: "0.75rem",
  },

  commentAuthorName: {
    fontWeight: "bold",
    fontSize: "0.75rem",
  },

  commentDate: {
    fontSize: "0.8rem",
    color: "#777",
  },

  commentContent: {
    fontSize: ".99rem",
    marginBottom: "0.5rem",
    wordWrap: "break-word",
    width: "100%",
    whiteSpace: "pre-wrap",
  },

  commentActions: {
    display: "flex",
    justifyContent: "flex-end",
    gap: "0.4rem",
    width: "100%",
  },

  deleteBtn: {
    backgroundColor: "#e63946",
    color: "#fff",
    border: "none",
    borderRadius: "5px",
    padding: "0.2rem 0.6rem",
    cursor: "pointer",
    fontSize: "0.8rem",
  },

  alertDiv: {
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
  };  
  if (!authenticated) return null;
  return (
    <>
    <div style={styles.header1}>
        User Dashboard
    </div>
    <div onClick={() => navigate(-1)} style={styles.backArrow}>
      ← Back
      </div>
    <div style={styles.card}>
      {processing?<LoadingOverlay/>:""}
      {msg!=null?(<div style={styles.alertDiv}>
            <h3 style={styles.alertText}>{msg}</h3>
          </div>):""}
      <div style={styles.header}>
        <div style={styles.profile}>
          <img
            src={post.profileImagePath!==undefined && post.profileImagePath!==null?`http://localhost:8080/media${post.profileImagePath.replace(/\\/g, "/")}`:"Profile Image"}
            alt="profile"
            style={styles.profileImage}
          />
          <div>
            <div style={{ fontWeight: "bold", cursor:"pointer" }} onClick={()=>navigate(`/profile/${userId}`)}>{post.authorProfileName}</div>
            <div style={{ fontSize: "0.85rem", color: "#666" }}>
              {new Date(post.postUploadDateTime).toLocaleString()}
            </div>
          </div>
        </div>
        <div style={{ ...styles.trustLabel, ...getTrustScoreStyle(post.civicTrustScore) }}>
          {post.civicTrustScore + "\t" + getTrustScoreLabel(post.civicTrustScore)}
        </div>
      </div>

      <div style={styles.title}>{post.postTitle}</div>
      <div style={styles.description}>{post.postDescription}</div>
      <div style={styles.title}>{post.postStatus===-1?"Under Review":post.postStatus===0?"Work In Progress":"Completed"}</div>
      <div style={styles.imagesGrid}>

      {postImages.map((path, index) => {
        const isLastOdd = postImages.length % 2 !== 0 && index === postImages.length - 1;
        return (
          <img
          key={`before-${index}`}
          src={path!==undefined && path!==null?`http://localhost:8080/media${path.replace("\\", "/")}`:"No Images found"}
          alt={`Before work ${index + 1}`}
          style={isLastOdd ? { ...styles.postImage, ...styles.fullWidthImage } : styles.postImage}
        />
        );
      })}

      {imagesAfterWork[0] == null?(
      <div style={styles.dividerSlide}>
        🔧After work images are not available yet.🔧
      </div>
      ):(<div style={styles.dividerSlide}>
        🔧Work After Completion.🔧
      </div>)}

      {imagesAfterWork.map((path, index) => {
        const isLastOdd = imagesAfterWork.length % 2 !== 0 && index === imagesAfterWork.length - 1;
        return (<img
          key={`after-${index}`}
          src={path!==undefined && path!==null?`http://localhost:8080/media${path.replace("\\", "/")}`:"After work Images are not available yet."}
          alt={`After work Images are not available yet. ${index + 1}`}
          style={isLastOdd ? { ...styles.postImage, ...styles.fullWidthImage } : styles.postImage}
        />)
      })}
      </div>

      <div style={styles.addressSection}>
  <div style={styles.addressLabel}>Address</div>
  <div style={styles.addressLine}>
    <strong>Street:</strong> {post.street+",\t" || "N/A"+",\t"}
    <strong>Pin Code:</strong> {post.postalCode || "N/A"}
  </div>
  <div style={styles.addressLine}>
    <strong>City:</strong> {post.city+",\t" || "N/A"}
    <strong>State:</strong> {post.state || "N/A"}
  </div>
  <div style={styles.addressLine}>
    <strong>Country:</strong> {post.country || "N/A"}
  </div>
</div>

      <div style={styles.actionRow}>
        <span style={styles.upvote} onClick={()=>{upVotePost(post.postId)}}>⬆ {post.upVoteCount}</span>
        <span style={styles.downvote} onClick={()=>{downVotePost(post.postId)}}>⬇ {post.downVoteCount}</span>
        <span style={styles.comments} onClick={()=>{setShowComments(showComments?false:true);loadComments(post.postId);}}>💬 {post.commentCount}</span>
        <span style={styles.reports} onClick={()=>{reportPost(post.postId)}}>🚩 {post.postReports}</span>
      </div>

      {showComments?(
        <div style={styles.commentSection}>
        <div style={styles.commentList}>
  {postComments.length>0?postComments.map((comment) => (
  <div key={comment.postCommentId} style={styles.commentItem}>
    <div style={styles.commentHeader} onClick={()=>navigate(`/profile/${comment.userId}`)} >
      <img
        src={comment.authorProfileImagePath!==undefined && comment.authorProfileImagePath!==null ?`http://localhost:8080/media${comment.authorProfileImagePath.replace("\\", "/")}`:"Profile image"}
        alt="Profile"
        style={styles.commentAuthorImage}
      />
      <div>
        <div style={styles.commentAuthorName}>{comment.authorProfileName}</div>
        <div style={styles.commentDate}>
          {new Date(comment.commentDateTime).toLocaleString()}
        </div>
      </div>
    </div>
    
    <div style={styles.commentContent}>
      {comment.commentDescription}
    </div>

    <div style={styles.commentActions}>
      <button onClick={() => upVoteComment(comment.postCommentId)} style={styles.upvote}>
        ⬆ {comment.likeCount}
      </button>
      <button onClick={() => downVoteComment(comment.postCommentId)} style={styles.downvote}>
        ⬇ {comment.disLikeCount}
      </button>
      {comment.deletable?(
        <button onClick={() => deleteComment(comment.postCommentId)} style={styles.deleteBtn}>
        Delete
      </button>
      ):""}
    </div>
  </div>
)):""}
      <button style={styles.addCommentBtn1} onClick={()=>loadMoreComments(post.postId)}>
          Load more 
      </button>
        </div>
      </div>
      ):""}

      <div style={styles.commentInputContainer}>
        <input
          type="text"
          value={newComment}
          onChange={(e) => setNewComment(e.target.value)}
          placeholder="Write a comment..."
          style={styles.commentInput}
        />
        <button onClick={addComment} style={styles.addCommentBtn}>
          Add Comment
        </button>
      </div>
    </div>
    </>
  );
};

export default PostView;
