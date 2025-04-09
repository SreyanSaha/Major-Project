import React from 'react';

function AssignVolunteers(){
  const styles = {
    container: {
      padding: '2rem',
      backgroundColor: '#ffff',
      minHeight: '100vh',
      width: '100%',
    },
    heading: {
      fontSize: '2rem',
      fontWeight: 'bold',
      marginBottom: '1.5rem',
      color: '#333',
      textAlign: "center",
    },
    searchSection: {
      display: 'flex',
      alignItems: 'center',
      marginBottom: '2rem',
      gap: '1rem',
    },
    input: {
      padding: '0.5rem 1rem',
      borderRadius: '5px',
      border: '1px solid #ccc',
      flexGrow: 1,
      fontSize: '1rem',
    },
    button: {
      backgroundColor: '#11398f',
      color: '#fff',
      padding: '0.5rem 1rem',
      border: 'none',
      borderRadius: '6px',
      fontWeight: 'bold',
      cursor: 'pointer',
    },
    eventsContainer: {
      display: 'flex',
      flexWrap: 'wrap',
      gap: '1.5rem',
    },
    card: {
      backgroundColor: '#fff',
      borderRadius: '15px',
      boxShadow: '0 2px 6px rgba(0, 0, 0, 0.1)',
      padding: '1rem',
      minWidth: '250px',
      flex: '1',
    },
    eventTitle: {
      fontWeight: 'bold',
      fontSize: '1.1rem',
      color: '#f18b01',
      marginBottom: '0.5rem',
    },
    label: {
      marginBottom: '0.25rem',
      display: 'block',
      fontWeight: '500',
      color: '#333',
    },
    select: {
      width: '100%',
      padding: '0.4rem',
      borderRadius: '5px',
      border: '1px solid #ccc',
      fontSize: '1rem',
    },
  };

  return (
    <div style={styles.container}>
      <div style={styles.heading}>Assign Volunteers</div>

      <div style={styles.searchSection}>
        <input type="text" placeholder="Enter volunteer name" style={styles.input} />
        <button style={styles.button}>Add Volunteer</button>
      </div>

      <div style={styles.eventsContainer}>
        {[1, 2, 3, 4].map((eventId) => (
          <div key={eventId} style={styles.card}>
            <div style={styles.eventTitle}>Events #{eventId}</div>
            <label style={styles.label}>Assign Volunteer:</label>
            <select style={styles.select}>
              <option>Select</option>
              <option>Volunteer A</option>
              <option>Volunteer B</option>
              <option>Volunteer C</option>
            </select>
          </div>
        ))}
      </div>
    </div>
  );
};

export default AssignVolunteers;
