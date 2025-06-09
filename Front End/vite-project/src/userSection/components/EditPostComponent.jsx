import React, { useState } from "react";

function UpdatePostForm({onBack}) {
  const [title, setTitle] = useState("Dummy Post Title");
  const [description, setDescription] = useState("This is a dummy description.");
  const [images, setImages] = useState([null, null, null, null, null]); // assuming 3 image slots
  const [location, setLocation] = useState("22.5726, 88.3639"); // Dummy lat,long
  const [address, setAddress] = useState("123, Dummy Street");
  const [city, setCity] = useState("Kolkata");
  const [state, setState] = useState("West Bengal");
  const [zip, setZip] = useState("700001");
  const [msg, setMsg] = useState(null);
  const [processing, setProcessing] = useState(false);

  const handleImageChange = (index, file) => {
    const newImages = [...images];
    newImages[index] = file;
    setImages(newImages);
  };

  const handleGetLocation = () => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition((position) => {
        setLocation(`${position.coords.latitude}, ${position.coords.longitude}`);
      });
    } else {
      alert("Geolocation is not supported by this browser.");
    }
  };

  const handleUpdate = () => {
    setProcessing(true);
    // simulate API call
    setTimeout(() => {
      setProcessing(false);
      setMsg("Post updated successfully!");
    }, 1500);
  };

  return (
    <div style={styles.container}>
      {processing ? <div>Loading...</div> : ""}
      <h2 style={styles.heading}>Update Your Post</h2>
      {msg && (
        <div style={styles.alertDiv}>
          <h3 style={styles.alertText}>{msg}</h3>
        </div>
      )}
      <form style={styles.form} onSubmit={(e) => { e.preventDefault(); handleUpdate(); }}>
        <input
          type="text"
          placeholder="Enter Post Title"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          style={styles.input}
          required
        />
        <textarea
          placeholder="Enter Post Description"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          style={styles.textarea}
          required
        />
        <div style={styles.imageSectionContainer}>
          {images.map((img, index) => (
            <div key={index} style={styles.imageSection}>
              <label htmlFor={`update-image-${index}`} style={styles.uploadLabel}>
                {img ? (
                  <img
                    src={URL.createObjectURL(img)}
                    alt={`Uploaded ${index + 1}`}
                    style={styles.previewImage}
                  />
                ) : (
                  <span style={styles.placeholderText}>Update Image {index + 1}</span>
                )}
              </label>
              <input
                type="file"
                id={`update-image-${index}`}
                accept="image/*"
                onChange={(e) => handleImageChange(index, e.target.files[0])}
                style={styles.hiddenInput}
              />
            </div>
          ))}
        </div>
        <div style={styles.locationSection}>
          <button type="button" onClick={handleGetLocation} style={styles.locationButton}>
            Update Location
          </button>
          <input
            type="text"
            value={location}
            onChange={(e) => setLocation(e.target.value)}
            placeholder="Current Location"
            style={styles.input}
            readOnly
          />
        </div>
        <div style={styles.addressSection}>
          <input
            type="text"
            placeholder="Address"
            value={address}
            onChange={(e) => setAddress(e.target.value)}
            style={styles.input}
            required
          />
          <input
            type="text"
            placeholder="City"
            value={city}
            onChange={(e) => setCity(e.target.value)}
            style={styles.input}
            required
          />
          <input
            type="text"
            placeholder="State"
            value={state}
            onChange={(e) => setState(e.target.value)}
            style={styles.input}
            required
          />
          <input
            type="text"
            placeholder="ZIP Code"
            value={zip}
            onChange={(e) => setZip(e.target.value)}
            style={styles.input}
            required
          />
        </div>
        <button type="submit" style={styles.submitButton}>
          Update Post
        </button>
      </form>
    </div>
  );
}

const styles = {
  alertDiv: { textAlign: "center" },
  alertText: {
    backgroundColor: "rgb(255, 64, 57)",
    padding: "2px",
    color: "white",
    borderRadius: "8px",
    width: "100%",
    marginBottom: "15px",
    margin: "auto",
    marginTop: "-10px",
  },
  container: {
    backgroundColor: "#f0f8ff",
    minHeight: "100vh",
    padding: "2rem",
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
  },
  heading: {
    color: "#0056b3",
    marginBottom: "1.5rem",
  },
  form: {
    backgroundColor: "#ffffff",
    padding: "2rem",
    borderRadius: "12px",
    boxShadow: "0 4px 8px rgba(0,0,0,0.1)",
    width: "100%",
    maxWidth: "700px",
    display: "flex",
    flexDirection: "column",
    gap: "1.5rem",
  },
  input: {
    padding: "0.8rem",
    borderRadius: "8px",
    border: "1px solid #007bff",
    outline: "none",
    fontSize: "1rem",
  },
  textarea: {
    padding: "0.8rem",
    borderRadius: "8px",
    border: "1px solid #007bff",
    outline: "none",
    fontSize: "1rem",
    minHeight: "100px",
    resize: "vertical",
  },
  imageSectionContainer: {
    display: "grid",
    gridTemplateColumns: "repeat(auto-fit, minmax(100px, 1fr))",
    gap: "1rem",
  },
  imageSection: {
    backgroundColor: "#e6f0ff",
    border: "2px dashed #007bff",
    borderRadius: "10px",
    height: "120px",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    position: "relative",
    overflow: "hidden",
  },
  uploadLabel: {
    width: "100%",
    height: "100%",
    cursor: "pointer",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    flexDirection: "column",
    color: "#007bff",
    fontWeight: "bold",
  },
  placeholderText: {
    textAlign: "center",
  },
  hiddenInput: {
    display: "none",
  },
  previewImage: {
    width: "100%",
    height: "100%",
    objectFit: "cover",
    borderRadius: "8px",
  },
  locationSection: {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    gap: "0.5rem",
  },
  locationButton: {
    backgroundColor: "#007bff",
    color: "#ffffff",
    padding: "0.7rem 1.5rem",
    border: "none",
    borderRadius: "8px",
    cursor: "pointer",
    fontWeight: "bold",
    transition: "background-color 0.3s",
  },
  addressSection: {
    display: "flex",
    flexDirection: "column",
    gap: "1rem",
  },
  submitButton: {
    marginTop: "1rem",
    backgroundColor: "#0056b3",
    color: "#ffffff",
    padding: "0.8rem",
    border: "none",
    borderRadius: "10px",
    fontSize: "1rem",
    cursor: "pointer",
    fontWeight: "bold",
    transition: "background-color 0.3s",
  },
};

export default UpdatePostForm;
