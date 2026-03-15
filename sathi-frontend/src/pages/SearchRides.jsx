import { useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import { motion } from 'framer-motion';
import passengerService from '../services/passengerService';
import RideCard from '../components/RideCard';
import './SearchRides.css';

const SearchRides = () => {
  const [searchParams] = useSearchParams();
  const [form, setForm] = useState({
    passengerSourceAddress: searchParams.get('from') || '',
    passengerDestinationString: searchParams.get('to') || '',
    passengerSourceLat: '',
    passengerSourceLong: '',
    passengerDestinationLat: '',
    passengerDestinationLong: '',
  });
  const [rides, setRides] = useState([]);
  const [loading, setLoading] = useState(false);
  const [searched, setSearched] = useState(false);
  const [error, setError] = useState('');
  const [requestModal, setRequestModal] = useState(null);
  const [requestSeats, setRequestSeats] = useState(1);
  const [requestLoading, setRequestLoading] = useState(false);
  const [toast, setToast] = useState(null);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSearch = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    setSearched(true);
    try {
      const searchData = {
        passengerSourceLat: parseFloat(form.passengerSourceLat) || 0,
        passengerSourceLong: parseFloat(form.passengerSourceLong) || 0,
        passengerSourceAddress: form.passengerSourceAddress,
        passengerDestinationLat: parseFloat(form.passengerDestinationLat) || 0,
        passengerDestinationLong: parseFloat(form.passengerDestinationLong) || 0,
        passengerDestinationString: form.passengerDestinationString,
      };
      const data = await passengerService.searchRides(searchData);
      setRides(data.response || []);
    } catch (err) {
      const msg = err.response?.data?.message || 'Search failed. Please try again.';
      setError(msg);
      setRides([]);
    }
    setLoading(false);
  };

  const handleRequestRide = async () => {
    if (!requestModal) return;
    setRequestLoading(true);
    try {
      await passengerService.requestRide({
        rideCode: requestModal.rideCode,
        sourceLat: parseFloat(form.passengerSourceLat) || 0,
        sourceLong: parseFloat(form.passengerSourceLong) || 0,
        sourceAddress: form.passengerSourceAddress,
        destinationLat: parseFloat(form.passengerDestinationLat) || 0,
        destinationLong: parseFloat(form.passengerDestinationLong) || 0,
        destinationAddress: form.passengerDestinationString,
        requiredSeats: requestSeats,
      });
      setToast({ type: 'success', message: 'Ride request sent successfully!' });
      setRequestModal(null);
      setTimeout(() => setToast(null), 3000);
    } catch (err) {
      setToast({ type: 'error', message: err.response?.data?.message || 'Failed to send request.' });
      setTimeout(() => setToast(null), 3000);
    }
    setRequestLoading(false);
  };

  return (
    <div className="search-page">
      {toast && (
        <div className={`toast toast-${toast.type}`}>{toast.message}</div>
      )}

      <div className="search-hero">
        <div className="container">
          <motion.h1
            initial={{ opacity: 0, y: -20 }}
            animate={{ opacity: 1, y: 0 }}
          >
            Find Your Perfect Ride
          </motion.h1>
          <motion.form
            className="search-form-card"
            onSubmit={handleSearch}
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.2 }}
          >
            <div className="search-form-grid">
              <div className="form-group">
                <label className="form-label">From</label>
                <input
                  type="text"
                  name="passengerSourceAddress"
                  className="form-input"
                  placeholder="Enter pickup location"
                  value={form.passengerSourceAddress}
                  onChange={handleChange}
                />
              </div>
              <div className="form-group">
                <label className="form-label">To</label>
                <input
                  type="text"
                  name="passengerDestinationString"
                  className="form-input"
                  placeholder="Enter destination"
                  value={form.passengerDestinationString}
                  onChange={handleChange}
                />
              </div>
              <div className="form-group">
                <label className="form-label">Source Lat</label>
                <input
                  type="number"
                  step="any"
                  name="passengerSourceLat"
                  className="form-input"
                  placeholder="e.g. 28.6139"
                  value={form.passengerSourceLat}
                  onChange={handleChange}
                />
              </div>
              <div className="form-group">
                <label className="form-label">Source Long</label>
                <input
                  type="number"
                  step="any"
                  name="passengerSourceLong"
                  className="form-input"
                  placeholder="e.g. 77.2090"
                  value={form.passengerSourceLong}
                  onChange={handleChange}
                />
              </div>
              <div className="form-group">
                <label className="form-label">Dest Lat</label>
                <input
                  type="number"
                  step="any"
                  name="passengerDestinationLat"
                  className="form-input"
                  placeholder="e.g. 26.9124"
                  value={form.passengerDestinationLat}
                  onChange={handleChange}
                />
              </div>
              <div className="form-group">
                <label className="form-label">Dest Long</label>
                <input
                  type="number"
                  step="any"
                  name="passengerDestinationLong"
                  className="form-input"
                  placeholder="e.g. 75.7873"
                  value={form.passengerDestinationLong}
                  onChange={handleChange}
                />
              </div>
            </div>
            <button type="submit" className="btn btn-primary btn-lg search-submit-btn" disabled={loading}>
              {loading ? 'Searching...' : '🔍 Search Rides'}
            </button>
          </motion.form>
        </div>
      </div>

      <div className="search-results container">
        {error && <div className="auth-error" style={{ marginBottom: '1rem' }}>{error}</div>}

        {loading ? (
          <div className="loading-screen">
            <div className="loading-spinner" />
            <p>Finding rides for you...</p>
          </div>
        ) : searched && rides.length === 0 ? (
          <div className="empty-state">
            <div className="empty-state-icon">🔍</div>
            <div className="empty-state-title">No rides found</div>
            <p>Try adjusting your search criteria or check back later.</p>
          </div>
        ) : (
          <div className="rides-list">
            {rides.map((ride, i) => (
              <RideCard
                key={ride.rideCode || i}
                ride={ride}
                onAction={(r) => setRequestModal(r)}
                actionLabel="Request Ride"
                actionVariant="accent"
              />
            ))}
          </div>
        )}
      </div>

      {/* Request Modal */}
      {requestModal && (
        <div className="modal-overlay" onClick={() => setRequestModal(null)}>
          <motion.div
            className="modal-content"
            onClick={(e) => e.stopPropagation()}
            initial={{ opacity: 0, scale: 0.9 }}
            animate={{ opacity: 1, scale: 1 }}
          >
            <h3>Request Ride</h3>
            <p className="modal-route">
              {requestModal.driverSourceLocation} → {requestModal.driverDestinationLocation}
            </p>
            <p className="modal-driver">Driver: {requestModal.driverName}</p>
            <div className="form-group">
              <label className="form-label">Number of Seats</label>
              <input
                type="number"
                className="form-input"
                min="1"
                max={requestModal.totalAvailableSeats || 4}
                value={requestSeats}
                onChange={(e) => setRequestSeats(parseInt(e.target.value) || 1)}
              />
            </div>
            <div className="modal-actions">
              <button className="btn btn-ghost" onClick={() => setRequestModal(null)}>Cancel</button>
              <button className="btn btn-accent" onClick={handleRequestRide} disabled={requestLoading}>
                {requestLoading ? 'Sending...' : 'Send Request'}
              </button>
            </div>
          </motion.div>
        </div>
      )}
    </div>
  );
};

export default SearchRides;
