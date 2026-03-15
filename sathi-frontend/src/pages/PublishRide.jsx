import { useState } from 'react';
import { motion } from 'framer-motion';
import driverService from '../services/driverService';
import './PublishRide.css';

const PublishRide = () => {
  const [form, setForm] = useState({
    sourceAddress: '',
    sourceLat: '',
    sourceLong: '',
    destinationAddress: '',
    destinationLat: '',
    destinationLong: '',
    departureTime: '',
    availableSeats: 1,
  });
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(null);
  const [error, setError] = useState('');

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    if (!form.sourceAddress || !form.destinationAddress || !form.departureTime) {
      setError('Please fill in all required fields');
      return;
    }
    setLoading(true);
    try {
      const rideData = {
        sourceLat: parseFloat(form.sourceLat) || 0,
        sourceLong: parseFloat(form.sourceLong) || 0,
        sourceAddress: form.sourceAddress,
        destinationLat: parseFloat(form.destinationLat) || 0,
        destinationLong: parseFloat(form.destinationLong) || 0,
        destinationAddress: form.destinationAddress,
        departureTime: form.departureTime,
        availableSeats: parseInt(form.availableSeats) || 1,
      };
      const data = await driverService.postRide(rideData);
      setSuccess(data.response);
      setForm({
        sourceAddress: '', sourceLat: '', sourceLong: '',
        destinationAddress: '', destinationLat: '', destinationLong: '',
        departureTime: '', availableSeats: 1,
      });
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to post ride. Please try again.');
    }
    setLoading(false);
  };

  return (
    <div className="publish-page">
      <div className="publish-hero">
        <div className="container">
          <motion.h1
            initial={{ opacity: 0, y: -20 }}
            animate={{ opacity: 1, y: 0 }}
          >
            Offer a Ride
          </motion.h1>
          <motion.p
            className="publish-subtitle"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ delay: 0.2 }}
          >
            Share your journey, save costs, and help the environment
          </motion.p>
        </div>
      </div>

      <div className="container">
        <motion.div
          className="publish-form-card"
          initial={{ opacity: 0, y: 30 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.3 }}
        >
          {success ? (
            <div className="publish-success">
              <div className="success-icon">🎉</div>
              <h2>Ride Posted!</h2>
              <p>Your ride <strong>{success.rideCode}</strong> has been published.</p>
              <p className="success-route">{success.sourceAddress} → {success.destinationAddress}</p>
              <button className="btn btn-primary btn-lg" onClick={() => setSuccess(null)}>
                Post Another Ride
              </button>
            </div>
          ) : (
            <form onSubmit={handleSubmit}>
              {error && <div className="auth-error">{error}</div>}

              <h2 className="form-section-title">Route Details</h2>
              <div className="publish-form-grid">
                <div className="form-group full-width">
                  <label className="form-label">Pickup Location *</label>
                  <input
                    type="text"
                    name="sourceAddress"
                    className="form-input"
                    placeholder="e.g. Connaught Place, Delhi"
                    value={form.sourceAddress}
                    onChange={handleChange}
                  />
                </div>
                <div className="form-group">
                  <label className="form-label">Source Latitude</label>
                  <input type="number" step="any" name="sourceLat" className="form-input" placeholder="28.6139" value={form.sourceLat} onChange={handleChange} />
                </div>
                <div className="form-group">
                  <label className="form-label">Source Longitude</label>
                  <input type="number" step="any" name="sourceLong" className="form-input" placeholder="77.2090" value={form.sourceLong} onChange={handleChange} />
                </div>
                <div className="form-group full-width">
                  <label className="form-label">Destination *</label>
                  <input
                    type="text"
                    name="destinationAddress"
                    className="form-input"
                    placeholder="e.g. Hawa Mahal, Jaipur"
                    value={form.destinationAddress}
                    onChange={handleChange}
                  />
                </div>
                <div className="form-group">
                  <label className="form-label">Destination Latitude</label>
                  <input type="number" step="any" name="destinationLat" className="form-input" placeholder="26.9124" value={form.destinationLat} onChange={handleChange} />
                </div>
                <div className="form-group">
                  <label className="form-label">Destination Longitude</label>
                  <input type="number" step="any" name="destinationLong" className="form-input" placeholder="75.7873" value={form.destinationLong} onChange={handleChange} />
                </div>
              </div>

              <h2 className="form-section-title">Trip Details</h2>
              <div className="publish-form-grid">
                <div className="form-group">
                  <label className="form-label">Departure Date & Time *</label>
                  <input
                    type="datetime-local"
                    name="departureTime"
                    className="form-input"
                    value={form.departureTime}
                    onChange={handleChange}
                  />
                </div>
                <div className="form-group">
                  <label className="form-label">Available Seats *</label>
                  <input
                    type="number"
                    name="availableSeats"
                    className="form-input"
                    min="1"
                    max="10"
                    value={form.availableSeats}
                    onChange={handleChange}
                  />
                </div>
              </div>

              <button type="submit" className="btn btn-primary btn-lg publish-submit" disabled={loading}>
                {loading ? 'Posting...' : '🚗 Publish Ride'}
              </button>
            </form>
          )}
        </motion.div>
      </div>
    </div>
  );
};

export default PublishRide;
