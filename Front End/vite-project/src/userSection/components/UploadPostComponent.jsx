import { useState } from "react";

function UploadPost() {
  const [images, setImages] = useState([null, null, null, null, null]);
  const [location, setLocation] = useState("");
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");

  const handleImageChange = (index, file) => {
    const newImages = [...images];
    newImages[index] = file;
    setImages(newImages);
  };

  const handleGetLocation = () => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          const { latitude, longitude } = position.coords;
          setLocation(`Lat: ${latitude.toFixed(4)}, Lon: ${longitude.toFixed(4)}`);
        },
        (error) => {
          alert("Error fetching location: " + error.message);
        }
      );
    } else {
      alert("Geolocation is not supported by your browser.");
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Uploading post:", { title, description, images, location });
    alert("Post uploaded successfully!");
  };

  return (
    <div style={styles.container}>
        <h2 style={styles.heading}>Upload Your Post</h2>

      <form onSubmit={handleSubmit} style={styles.form}>
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
              <label htmlFor={`image-${index}`} style={styles.uploadLabel}>
                {img ? (
                  <img
                    src={URL.createObjectURL(img)}
                    alt={`Uploaded ${index + 1}`}
                    style={styles.previewImage}
                  />
                ) : (
                  <span style={styles.placeholderText}>Upload Image {index + 1}</span>
                )}
              </label>
              <input
                type="file"
                id={`image-${index}`}
                accept="image/*"
                onChange={(e) => handleImageChange(index, e.target.files[0])}
                style={styles.hiddenInput}
              />
            </div>
          ))}
        </div>

        <div style={styles.locationSection}>
          <button type="button" onClick={handleGetLocation} style={styles.locationButton}>
            Add Current Location
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

        <button type="submit" style={styles.submitButton}>
          Upload Post
        </button>
      </form>
    </div>
  );
}

const styles = {
  container: {
    backgroundColor: "#f0f8ff", // Light blue
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
    maxWidth: "600px",
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

export default UploadPost;
