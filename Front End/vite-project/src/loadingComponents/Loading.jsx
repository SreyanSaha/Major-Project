import './loading.css';

const LoadingOverlay = () => {
  return (
    <div style={overlayStyle}>
      <div style={contentStyle}>
        <div className="loading-spinner"></div>
        <p style={textStyle}>Please wait...</p>
      </div>
    </div>
  );
};

const overlayStyle = {
  position: 'absolute',
  top: 0, left: 0, right: 0, bottom: 0,
  backgroundColor: 'rgba(0, 0, 0, 0.6)', // darker semi-transparent background
  display: 'flex',
  justifyContent: 'center',
  alignItems: 'center',
  zIndex: 1000,
  backdropFilter: 'blur(2px)', // little blur effect
};

const contentStyle = {
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
};

const spinnerStyle = {
  border: '6px solid #f3f3f3',
  borderTop: '6px solid #3498db',
  borderRadius: '50%',
  width: '50px',
  height: '50px',
  animation: 'spin 1s linear infinite',
  marginBottom: '15px', // space between spinner and text
};

const textStyle = {
  color: '#ffffff',
  fontSize: '18px',
  fontWeight: 'bold',
};

export default LoadingOverlay;
