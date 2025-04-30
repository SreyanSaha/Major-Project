import { useState } from "react";

function UploadCampaign() {
  const [images, setImages] = useState([null, null, null, null, null]);
  const [paymentImage, setPaymentImage] = useState(null);
  const [address, setAddress] = useState("");
  const [city, setCity] = useState("");
  const [state, setState] = useState("");
  const [zipCode, setZipCode] = useState("");
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");

  const handleImageChange = (index, file) => {
    const newImages = [...images];
    newImages[index] = file;
    setImages(newImages);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // Logic to upload campaign data (images, address, payment image etc.)
    console.log("Uploading Campaign:", { title, description, address, city, state, zipCode, images, paymentImage });
    alert("Campaign uploaded successfully!");
  };

  return (
    <div style={styles.container}>
      <h2 style={styles.heading}>Upload Campaign</h2>

      <form onSubmit={handleSubmit} style={styles.form}>
        <input
          type="text"
          placeholder="Enter Campaign Title"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          style={styles.inputField}
          required
        />

        <div style={styles.noteSection}>
          <p style={styles.noteText}>
            ⚡ <strong>Note:</strong> Please mention your online payment details (like UPI ID, bank details, etc.) inside the description.
          </p>
        </div>

        <textarea
          placeholder="Enter Campaign Description (Include Payment Details)"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          style={styles.textArea}
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

        <div style={styles.addressSection}>
          <input
            type="text"
            placeholder="Address"
            value={address}
            onChange={(e) => setAddress(e.target.value)}
            style={styles.inputField}
            required
          />
          <input
            type="text"
            placeholder="City"
            value={city}
            onChange={(e) => setCity(e.target.value)}
            style={styles.inputField}
            required
          />
          <input
            type="text"
            placeholder="State"
            value={state}
            onChange={(e) => setState(e.target.value)}
            style={styles.inputField}
            required
          />
          <input
            type="text"
            placeholder="Zip Code"
            value={zipCode}
            onChange={(e) => setZipCode(e.target.value)}
            style={styles.inputField}
            required
          />
        </div>
        
        <div style={styles.noteSection}>
          <p style={styles.noteText}>
            ⚡ <strong>Note:</strong> Please upload your online payment UPI QR Code.
          </p>
        </div>

        <div style={styles.paymentSection}>
          <label htmlFor="paymentImage" style={styles.uploadLabel}>
            {paymentImage ? (
              <img
                src={URL.createObjectURL(paymentImage)}
                alt="Payment QR"
                style={styles.previewImage}
              />
            ) : (
              <span style={styles.placeholderText}>Upload UPI QR or Receipt Image</span>
            )}
          </label>
          <input
            type="file"
            id="paymentImage"
            accept="image/*"
            onChange={(e) => setPaymentImage(e.target.files[0])}
            style={styles.hiddenInput}
          />
        </div>

        <button type="submit" style={styles.submitButton}>
          Upload Campaign
        </button>
      </form>
    </div>
  );
}

const styles = {
  container: {
    backgroundColor: "#f0f8ff",
    minHeight: "100vh",
    padding: "1rem",
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
    marginRight: "100px",
    border: "1px solid #007bff",
  },
  textArea: {
    width: "95%",
    padding: "0.8rem",
    borderRadius: "8px",
    border: "1px solid #007bff",
    minHeight: "120px",
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
    padding: "10px",
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
  noteSection: {
    backgroundColor: "#e0f0ff",
    padding: "1rem",
    borderRadius: "8px",
  },
  noteText: {
    color: "#007bff",
    fontSize: "0.95rem",
  },
  addressSection: {
    display: "flex",
    flexDirection: "column",
    gap: "1rem",
  },
  paymentSection: {
    backgroundColor: "#e6f0ff",
    border: "2px dashed #28a745",
    borderRadius: "10px",
    height: "140px",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    position: "relative",
    overflow: "hidden",
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

export default UploadCampaign;
