import React, { useState } from "react";
import { useParams } from 'react-router-dom';

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
const { postId } = useParams();
const [postData, setPost] = useState({});
const [userId, setUserId] = useState(-1);
const [postPostComments, setPostComments] = useState([]);
  const post = {
    postId: 1,
    civicTrustScore: 720,
    authorProfileName: "Rohit Roy",
    authorProfileImagePath: null,
    postUploadDateTime: "2025-06-05T01:51:46.278164",
    postTitle: "Beach Cleanup Drive",
    postDescription: "Join us this weekend to clean the local beach!",
    upVoteCount: 12,
    downVoteCount: 3,
    commentCount: 2,
    postReports: 1,
    imagePath1:
      "/postImages/fec31b1f-5942-4f73-a777-41c24697a9fa_1749068506251_images (1).jpg",
    postStatus: -1,
  };

  const [comments, setComments] = useState([
    { id: 1, text: "This is great!" },
    { id: 2, text: "Count me in!" },
  ]);
  const [newComment, setNewComment] = useState("");

  const addComment = () => {
    if (newComment.trim() !== "") {
      setComments([...comments, { id: Date.now(), text: newComment }]);
      setNewComment("");
    }
  };

  const deleteComment = (id) => {
    setComments(comments.filter((c) => c.id !== id));
  };

  const styles = {
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
    postImage: {
      width: "100%",
      borderRadius: "10px",
      marginBottom: "1rem",
      border: "1px solid #d0e3ff",
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
    upvote: { color: "green", cursor: "pointer" },
    downvote: { color: "red", cursor: "pointer" },
    comments: { color: "#007bff", cursor: "pointer" },
    reports: { color: "#ff9800", cursor: "pointer" },
    commentSection: {
      marginTop: "1rem",
    },
    commentInput: {
      width: "100%",
      padding: "0.6rem",
      borderRadius: "6px",
      border: "1px solid #ccdfff",
      marginBottom: "0.5rem",
    },
    commentList: {
      marginTop: "0.5rem",
      backgroundColor: "#f8faff",
      padding: "0.75rem",
      borderRadius: "6px",
    },
    commentItem: {
      padding: "0.5rem 0",
      borderBottom: "1px solid #e0e0e0",
      display: "flex",
      justifyContent: "space-between",
      alignItems: "center",
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
    addCommentBtn: {
      backgroundColor: "#007bff",
      color: "#fff",
      border: "none",
      borderRadius: "6px",
      padding: "0.4rem 0.9rem",
      cursor: "pointer",
      marginTop: "0.3rem",
    },
  };

  return (
    <div style={styles.card}>
      <div style={styles.header}>
        <div style={styles.profile}>
          <img
            src={
              post.authorProfileImagePath ||
              "https://via.placeholder.com/50?text=User"
            }
            alt="profile"
            style={styles.profileImage}
          />
          <div>
            <div style={{ fontWeight: "bold" }}>{post.authorProfileName}</div>
            <div style={{ fontSize: "0.85rem", color: "#666" }}>
              {new Date(post.postUploadDateTime).toLocaleString()}
            </div>
          </div>
        </div>
        <div style={{ ...styles.trustLabel, ...getTrustScoreStyle(post.civicTrustScore) }}>
          {getTrustScoreLabel(post.civicTrustScore)}
        </div>
      </div>

      <div style={styles.title}>{post.postTitle}</div>
      <div style={styles.description}>{post.postDescription}</div>
      <img src={post.imagePath1} alt="Post" style={styles.postImage} />

      <div style={styles.actionRow}>
        <span style={styles.upvote}>⬆ {post.upVoteCount}</span>
        <span style={styles.downvote}>⬇ {post.downVoteCount}</span>
        <span style={styles.comments}>💬 {comments.length}</span>
        <span style={styles.reports}>🚩 {post.postReports}</span>
      </div>

      <div style={styles.commentSection}>
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

        <div style={styles.commentList}>
          {comments.map((comment) => (
            <div key={comment.id} style={styles.commentItem}>
              <span>{comment.text}</span>
              <button
                onClick={() => deleteComment(comment.id)}
                style={styles.deleteBtn}
              >
                Delete
              </button>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default PostView;
