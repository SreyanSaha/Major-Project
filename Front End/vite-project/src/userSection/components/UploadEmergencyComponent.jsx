import { useState, useRef } from "react";

function UploadEmergencyPost() {
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [images, setImages] = useState([null, null, null, null, null]);
  const [audioURL, setAudioURL] = useState(null);
  const [isRecording, setIsRecording] = useState(false);
  const [location, setLocation] = useState("");
  const mediaRecorderRef = useRef(null);
  const audioChunksRef = useRef([]);

  const handleStartRecording = async () => {
    try {
      const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
      const mediaRecorder = new MediaRecorder(stream);
      mediaRecorderRef.current = mediaRecorder;
      audioChunksRef.current = [];

      mediaRecorder.start();
      setIsRecording(true);

      mediaRecorder.addEventListener("dataavailable", (event) => {
        audioChunksRef.current.push(event.data);
      });

      mediaRecorder.addEventListener("stop", () => {
        const audioBlob = new Blob(audioChunksRef.current, { type: "audio/webm" });
        const url = URL.createObjectURL(audioBlob);
        setAudioURL(url);
      });
    } catch (error) {
      console.error("Error accessing microphone:", error);
      alert("Could not access microphone. Please allow microphone permission.");
    }
  };

  const handleStopRecording = () => {
    if (mediaRecorderRef.current) {
      mediaRecorderRef.current.stop();
      setIsRecording(false);
    }
  };

  const fetchLocation = () => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          setLocation(`Latitude: ${position.coords.latitude}, Longitude: ${position.coords.longitude}`);
        },
        (error) => {
          console.error("Error fetching location:", error);
          alert("Unable to fetch location. Please allow location access.");
        }
      );
    } else {
      alert("Geolocation is not supported by your browser.");
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!location) {
      alert("Location is mandatory for emergency posts!");
      return;
    }
    if (!title && !description && !audioURL) {
      alert("Please provide at least Title or Description or Audio for emergency post.");
      return;
    }

    console.log("Uploading Emergency Post:", { title, description, images, audioURL, location });
    alert("Emergency Post uploaded successfully!");
  };

  const handleImageChange = (index, file) => {
    const newImages = [...images];
    newImages[index] = file;
    setImages(newImages);
  };

  return (
    <div style={styles.container}>
      <h2 style={styles.heading}>Upload Emergency Post</h2>

      <form onSubmit={handleSubmit} style={styles.form}>
        <input
          type="text"
          placeholder="Enter Title (Optional)"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          style={styles.inputField}
        />

        <textarea
          placeholder="Enter Description (Optional)"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          style={styles.textArea}
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

        <div style={styles.audioSection}>
          <button
            type="button"
            onMouseDown={handleStartRecording}
            onMouseUp={handleStopRecording}
            onTouchStart={handleStartRecording}
            onTouchEnd={handleStopRecording}
            style={{
              ...styles.recordButton,
              backgroundColor: isRecording ? "#ff4d4d" : "#007bff",
            }}
          >
            {isRecording ? "Recording..." : "Hold to Record"}
          </button>

          {audioURL && (
            <audio controls src={audioURL} style={styles.audioPlayer}></audio>
          )}
        </div>

        <div style={styles.locationSection}>
          <button type="button" onClick={fetchLocation} style={styles.locationButton}>
            Fetch Current Location
          </button>
          <textarea
            placeholder="Location will appear here..."
            value={location}
            readOnly
            style={styles.locationBox}
          />
        </div>

        <button type="submit" style={styles.submitButton}>
          Upload Emergency Post
        </button>
      </form>
    </div>
  );
}

const styles = {
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
  inputField: {
    width: "95%",
    padding: "0.8rem",
    borderRadius: "8px",
    border: "1px solid #007bff",
  },
  textArea: {
    width: "95%",
    padding: "0.8rem",
    borderRadius: "8px",
    border: "1px solid #007bff",
    minHeight: "100px",
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
  audioSection: {
    backgroundColor: "#e0f7ff",
    padding: "1rem",
    borderRadius: "8px",
    textAlign: "center",
  },
  recordButton: {
    padding: "0.8rem 2rem",
    border: "none",
    borderRadius: "8px",
    color: "#fff",
    fontWeight: "bold",
    fontSize: "1rem",
    cursor: "pointer",
    transition: "background-color 0.3s",
    marginBottom: "1rem",
  },
  audioPlayer: {
    width: "100%",
    marginTop: "1rem",
  },
  locationSection: {
    display: "flex",
    flexDirection: "column",
    gap: "1rem",
  },
  locationButton: {
    backgroundColor: "#007bff",
    color: "#fff",
    padding: "0.8rem",
    borderRadius: "8px",
    border: "none",
    cursor: "pointer",
    fontWeight: "bold",
  },
  locationBox: {
    width: "95%",
    padding: "0.8rem",
    borderRadius: "8px",
    border: "1px solid #007bff",
    backgroundColor: "#f9f9f9",
    minHeight: "80px",
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

export default UploadEmergencyPost;
