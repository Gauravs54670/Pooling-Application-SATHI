import { motion } from 'framer-motion';
import './RideCard.css';

const RideCard = ({ ride, onAction, actionLabel, actionVariant = 'primary' }) => {
  const formatDate = (dateStr) => {
    if (!dateStr) return '';
    const d = new Date(dateStr);
    return d.toLocaleDateString('en-IN', {
      day: 'numeric',
      month: 'short',
      year: 'numeric',
    });
  };

  const formatTime = (dateStr) => {
    if (!dateStr) return '';
    const d = new Date(dateStr);
    return d.toLocaleTimeString('en-IN', {
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  return (
    <motion.div
      className="ride-card"
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.4 }}
      whileHover={{ y: -4 }}
    >
      <div className="ride-card-header">
        <div className="ride-driver-info">
          <div className="driver-avatar">
            {ride.driverName?.charAt(0) || 'D'}
          </div>
          <div>
            <h4 className="driver-name">{ride.driverName || 'Driver'}</h4>
            {ride.driverRating && (
              <div className="driver-rating">
                <span className="star">⭐</span>
                <span>{ride.driverRating.toFixed(1)}</span>
              </div>
            )}
          </div>
        </div>
        {ride.rideStatus && (
          <span className={`badge badge-${getStatusBadge(ride.rideStatus)}`}>
            {ride.rideStatus}
          </span>
        )}
      </div>

      <div className="ride-route">
        <div className="route-point">
          <div className="route-dot start" />
          <div className="route-text">
            <span className="route-label">From</span>
            <span className="route-address">{ride.driverSourceLocation || ride.sourceAddress || 'Origin'}</span>
          </div>
        </div>
        <div className="route-line" />
        <div className="route-point">
          <div className="route-dot end" />
          <div className="route-text">
            <span className="route-label">To</span>
            <span className="route-address">{ride.driverDestinationLocation || ride.destinationAddress || 'Destination'}</span>
          </div>
        </div>
      </div>

      <div className="ride-details">
        <div className="ride-detail-item">
          <span className="detail-icon">📅</span>
          <span>{formatDate(ride.rideDepartureTime || ride.departureTime)}</span>
        </div>
        <div className="ride-detail-item">
          <span className="detail-icon">⏰</span>
          <span>{formatTime(ride.rideDepartureTime || ride.departureTime)}</span>
        </div>
        <div className="ride-detail-item">
          <span className="detail-icon">💺</span>
          <span>{ride.totalAvailableSeats ?? ride.availableSeats ?? '—'} seats</span>
        </div>
        {(ride.estimatedFare || ride.estimatedTotalFare) && (
          <div className="ride-detail-item fare">
            <span className="detail-icon">💰</span>
            <span>₹{parseFloat(ride.estimatedFare || ride.estimatedTotalFare).toFixed(0)}</span>
          </div>
        )}
      </div>

      {(ride.vehicleModel || ride.vehicleCategory) && (
        <div className="ride-vehicle">
          <span>🚗 {ride.vehicleModel || ''}</span>
          {ride.vehicleCategory && <span className="vehicle-badge">{ride.vehicleCategory}</span>}
          {ride.vehicleClass && <span className="vehicle-badge">{ride.vehicleClass}</span>}
        </div>
      )}

      {onAction && (
        <div className="ride-card-footer">
          <button className={`btn btn-${actionVariant} btn-sm`} onClick={() => onAction(ride)}>
            {actionLabel || 'View Details'}
          </button>
        </div>
      )}
    </motion.div>
  );
};

const getStatusBadge = (status) => {
  const map = {
    POSTED: 'primary',
    ACTIVE: 'success',
    COMPLETED: 'success',
    CANCELLED: 'danger',
    STARTED: 'warning',
    PENDING: 'warning',
    ACCEPTED: 'success',
    REJECTED: 'danger',
  };
  return map[status] || 'gray';
};

export default RideCard;
