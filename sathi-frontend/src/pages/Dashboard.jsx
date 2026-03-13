import { useState, useEffect } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { useAuth } from '../context/AuthContext';
import userService from '../services/userService';
import driverService from '../services/driverService';
import passengerService from '../services/passengerService';
import RideCard from '../components/RideCard';
import './Dashboard.css';

const tabs = [
  { id: 'profile', label: '👤 Profile', icon: '👤' },
  { id: 'myrides', label: '🚗 My Rides', icon: '🚗', driverOnly: true },
  { id: 'requests', label: '📋 Ride Requests', icon: '📋', driverOnly: true },
  { id: 'myrequests', label: '🎫 My Bookings', icon: '🎫' },
  { id: 'driver-reg', label: '🪪 Become Driver', icon: '🪪' },
  { id: 'settings', label: '⚙️ Settings', icon: '⚙️' },
];

const Dashboard = () => {
  const { user, isDriver, refreshProfile, logout } = useAuth();
  const [activeTab, setActiveTab] = useState('profile');
  const [toast, setToast] = useState(null);

  const showToast = (type, message) => {
    setToast({ type, message });
    setTimeout(() => setToast(null), 3000);
  };

  const visibleTabs = tabs.filter(t => !t.driverOnly || isDriver);

  return (
    <div className="dashboard-page">
      {toast && <div className={`toast toast-${toast.type}`}>{toast.message}</div>}
      <div className="dashboard-header">
        <div className="container">
          <h1>Dashboard</h1>
          <p>Welcome back, <strong>{user?.userFullName || 'User'}</strong></p>
        </div>
      </div>
      <div className="container dashboard-layout">
        <aside className="dashboard-sidebar">
          {visibleTabs.map(tab => (
            <button
              key={tab.id}
              className={`sidebar-tab ${activeTab === tab.id ? 'active' : ''}`}
              onClick={() => setActiveTab(tab.id)}
            >
              <span className="tab-icon">{tab.icon}</span>
              <span className="tab-label">{tab.label.split(' ').slice(1).join(' ')}</span>
            </button>
          ))}
        </aside>
        <main className="dashboard-content">
          <AnimatePresence mode="wait">
            <motion.div
              key={activeTab}
              initial={{ opacity: 0, x: 20 }}
              animate={{ opacity: 1, x: 0 }}
              exit={{ opacity: 0, x: -20 }}
              transition={{ duration: 0.3 }}
            >
              {activeTab === 'profile' && <ProfileTab showToast={showToast} user={user} refreshProfile={refreshProfile} />}
              {activeTab === 'myrides' && <MyRidesTab showToast={showToast} />}
              {activeTab === 'requests' && <RideRequestsTab showToast={showToast} />}
              {activeTab === 'myrequests' && <MyBookingsTab showToast={showToast} />}
              {activeTab === 'driver-reg' && <DriverRegTab showToast={showToast} isDriver={isDriver} refreshProfile={refreshProfile} />}
              {activeTab === 'settings' && <SettingsTab showToast={showToast} logout={logout} />}
            </motion.div>
          </AnimatePresence>
        </main>
      </div>
    </div>
  );
};

/* ===== Profile Tab ===== */
const ProfileTab = ({ showToast, user, refreshProfile }) => {
  const [editing, setEditing] = useState(false);
  const [form, setForm] = useState({
    userFullName: user?.userFullName || '',
    email: user?.email || '',
    phoneNumber: user?.phoneNumber || '',
  });
  const [loading, setLoading] = useState(false);

  const handleSave = async () => {
    setLoading(true);
    try {
      await userService.updateProfile(form);
      await refreshProfile();
      showToast('success', 'Profile updated!');
      setEditing(false);
    } catch (err) {
      showToast('error', err.response?.data?.message || 'Update failed');
    }
    setLoading(false);
  };

  return (
    <div className="tab-content">
      <div className="tab-header">
        <h2>My Profile</h2>
        {!editing && <button className="btn btn-outline btn-sm" onClick={() => setEditing(true)}>Edit</button>}
      </div>
      <div className="profile-card">
        <div className="profile-avatar-large">
          {user?.userFullName?.charAt(0) || 'U'}
        </div>
        <div className="profile-fields">
          <div className="profile-field">
            <label>Full Name</label>
            {editing ? (
              <input className="form-input" value={form.userFullName} onChange={(e) => setForm({...form, userFullName: e.target.value})} />
            ) : (
              <p>{user?.userFullName}</p>
            )}
          </div>
          <div className="profile-field">
            <label>Email</label>
            {editing ? (
              <input className="form-input" value={form.email} onChange={(e) => setForm({...form, email: e.target.value})} />
            ) : (
              <p>{user?.email}</p>
            )}
          </div>
          <div className="profile-field">
            <label>Phone</label>
            {editing ? (
              <input className="form-input" value={form.phoneNumber} onChange={(e) => setForm({...form, phoneNumber: e.target.value})} />
            ) : (
              <p>{user?.phoneNumber || '—'}</p>
            )}
          </div>
          <div className="profile-field">
            <label>Account Status</label>
            <p><span className={`badge badge-${user?.userAccountStatus === 'ACTIVE' ? 'success' : 'warning'}`}>{user?.userAccountStatus || 'ACTIVE'}</span></p>
          </div>
          <div className="profile-field">
            <label>Member Since</label>
            <p>{user?.accountCreatedAt ? new Date(user.accountCreatedAt).toLocaleDateString('en-IN') : '—'}</p>
          </div>
        </div>
        {editing && (
          <div className="profile-actions">
            <button className="btn btn-ghost" onClick={() => setEditing(false)}>Cancel</button>
            <button className="btn btn-primary" onClick={handleSave} disabled={loading}>
              {loading ? 'Saving...' : 'Save Changes'}
            </button>
          </div>
        )}
      </div>
    </div>
  );
};

/* ===== My Rides Tab ===== */
const MyRidesTab = ({ showToast }) => {
  const [rides, setRides] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchRides = async () => {
    setLoading(true);
    try {
      const data = await driverService.getMyRides();
      setRides(data.response || []);
    } catch {
      showToast('error', 'Failed to load rides');
    }
    setLoading(false);
  };

  useEffect(() => { fetchRides(); }, []);

  const handleCancel = async (ride) => {
    if (!window.confirm('Cancel this ride?')) return;
    try {
      await driverService.cancelRide(ride.rideCode);
      showToast('success', 'Ride cancelled');
      fetchRides();
    } catch (err) {
      showToast('error', err.response?.data?.message || 'Failed to cancel');
    }
  };

  return (
    <div className="tab-content">
      <div className="tab-header">
        <h2>My Posted Rides</h2>
        <button className="btn btn-outline btn-sm" onClick={fetchRides}>Refresh</button>
      </div>
      {loading ? (
        <div className="loading-screen"><div className="loading-spinner" /></div>
      ) : rides.length === 0 ? (
        <div className="empty-state">
          <div className="empty-state-icon">🚗</div>
          <div className="empty-state-title">No rides posted yet</div>
          <p>Post your first ride and start earning!</p>
        </div>
      ) : (
        <div className="rides-grid">
          {rides.map((ride, i) => (
            <RideCard key={ride.rideCode || i} ride={ride} onAction={handleCancel} actionLabel="Cancel Ride" actionVariant="danger" />
          ))}
        </div>
      )}
    </div>
  );
};

/* ===== Ride Requests Tab ===== */
const RideRequestsTab = ({ showToast }) => {
  const [rides, setRides] = useState([]);
  const [selectedRide, setSelectedRide] = useState(null);
  const [bookings, setBookings] = useState([]);
  const [loadingRides, setLoadingRides] = useState(true);
  const [loadingBookings, setLoadingBookings] = useState(false);

  useEffect(() => {
    const fetchRides = async () => {
      try {
        const data = await driverService.getMyRides();
        setRides(data.response || []);
      } catch {
        showToast('error', 'Failed to load rides');
      }
      setLoadingRides(false);
    };
    fetchRides();
  }, []);

  const handleSelectRide = async (ride) => {
    setSelectedRide(ride);
    setLoadingBookings(true);
    try {
      const data = await driverService.getRequestedRides(ride.rideCode);
      setBookings(data.response || []);
    } catch {
      showToast('error', 'Failed to load bookings');
    }
    setLoadingBookings(false);
  };

  const handleDecision = async (booking, decision) => {
    try {
      await driverService.rideRequestDecision(decision, {
        requestId: booking.requestId,
        rideCode: booking.rideCode,
      });
      showToast('success', `Request ${decision.toLowerCase()}`);
      handleSelectRide(selectedRide);
    } catch (err) {
      showToast('error', err.response?.data?.message || 'Decision failed');
    }
  };

  return (
    <div className="tab-content">
      <div className="tab-header"><h2>Ride Booking Requests</h2></div>
      {loadingRides ? (
        <div className="loading-screen"><div className="loading-spinner" /></div>
      ) : (
        <>
          <div className="ride-selector">
            <label className="form-label">Select a ride to view requests:</label>
            <div className="ride-selector-grid">
              {rides.map((ride) => (
                <button
                  key={ride.rideCode}
                  className={`ride-selector-btn ${selectedRide?.rideCode === ride.rideCode ? 'active' : ''}`}
                  onClick={() => handleSelectRide(ride)}
                >
                  <span>{ride.sourceAddress} → {ride.destinationAddress}</span>
                  <span className="ride-selector-code">{ride.rideCode}</span>
                </button>
              ))}
            </div>
          </div>
          {selectedRide && (
            <div className="bookings-list">
              {loadingBookings ? (
                <div className="loading-screen"><div className="loading-spinner" /></div>
              ) : bookings.length === 0 ? (
                <div className="empty-state">
                  <div className="empty-state-icon">📋</div>
                  <div className="empty-state-title">No booking requests</div>
                </div>
              ) : (
                bookings.map((booking) => (
                  <div key={booking.requestId} className="booking-card">
                    <div className="booking-header">
                      <strong>{booking.passengerName}</strong>
                      <span className={`badge badge-${booking.rideRequestStatus === 'PENDING' ? 'warning' : booking.rideRequestStatus === 'ACCEPTED' ? 'success' : 'danger'}`}>
                        {booking.rideRequestStatus}
                      </span>
                    </div>
                    <div className="booking-details">
                      <p>📍 {booking.passengerSourceAddress} → {booking.passengerDestinationAddress}</p>
                      <p>💺 {booking.requestedSeats} seat(s)</p>
                      {booking.distanceFromDriver && <p>📏 {booking.distanceFromDriver.toFixed(1)} km from you</p>}
                    </div>
                    {booking.rideRequestStatus === 'PENDING' && (
                      <div className="booking-actions">
                        <button className="btn btn-success btn-sm" onClick={() => handleDecision(booking, 'ACCEPTED')}>Accept</button>
                        <button className="btn btn-danger btn-sm" onClick={() => handleDecision(booking, 'REJECTED')}>Reject</button>
                      </div>
                    )}
                  </div>
                ))
              )}
            </div>
          )}
        </>
      )}
    </div>
  );
};

/* ===== My Bookings Tab ===== */
const MyBookingsTab = ({ showToast }) => {
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetch = async () => {
      try {
        const data = await passengerService.getMyRideRequests();
        setRequests(data.response || []);
      } catch {
        showToast('error', 'Failed to load requests');
      }
      setLoading(false);
    };
    fetch();
  }, []);

  return (
    <div className="tab-content">
      <div className="tab-header"><h2>My Ride Requests</h2></div>
      {loading ? (
        <div className="loading-screen"><div className="loading-spinner" /></div>
      ) : requests.length === 0 ? (
        <div className="empty-state">
          <div className="empty-state-icon">🎫</div>
          <div className="empty-state-title">No ride requests yet</div>
          <p>Search for rides and send your first request!</p>
        </div>
      ) : (
        <div className="bookings-list">
          {requests.map((req, i) => (
            <div key={i} className="booking-card">
              <div className="booking-header">
                <strong>Ride: {req.rideCode}</strong>
                <span className={`badge badge-${req.rideRequestDecision === 'PENDING' ? 'warning' : req.rideRequestDecision === 'ACCEPTED' ? 'success' : 'danger'}`}>
                  {req.rideRequestDecision}
                </span>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

/* ===== Driver Registration Tab ===== */
const DriverRegTab = ({ showToast, isDriver, refreshProfile }) => {
  const [form, setForm] = useState({
    driverLicenseNumber: '',
    licenseExpirationDate: '',
    vehicleModel: '',
    vehicleNumber: '',
    vehicleCategory: 'CAR',
    vehicleClass: 'ECONOMY',
    vehicleSeatCapacity: 4,
  });
  const [file, setFile] = useState(null);
  const [loading, setLoading] = useState(false);
  const [driverProfile, setDriverProfile] = useState(null);

  useEffect(() => {
    if (isDriver) {
      driverService.getProfile().then(data => setDriverProfile(data.response)).catch(() => {});
    }
  }, [isDriver]);

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!file) {
      showToast('error', 'Please upload your driving license');
      return;
    }
    setLoading(true);
    try {
      await userService.registerDriver(file, form);
      showToast('success', 'Driver registration submitted!');
      await refreshProfile();
    } catch (err) {
      showToast('error', err.response?.data?.message || 'Registration failed');
    }
    setLoading(false);
  };

  if (isDriver && driverProfile) {
    return (
      <div className="tab-content">
        <div className="tab-header"><h2>Driver Profile</h2></div>
        <div className="driver-profile-card">
          <div className="driver-profile-avatar">
            {driverProfile.driverProfileUrl ? (
              <img src={driverProfile.driverProfileUrl} alt="Profile" />
            ) : (
              <span>{driverProfile.userFullName?.charAt(0) || 'D'}</span>
            )}
          </div>
          <div className="driver-profile-info">
            <h3>{driverProfile.userFullName}</h3>
            <span className={`badge badge-${driverProfile.driverVerificationStatus === 'VERIFIED' ? 'success' : 'warning'}`}>
              {driverProfile.driverVerificationStatus}
            </span>
          </div>
          <div className="profile-fields">
            <div className="profile-field"><label>License</label><p>{driverProfile.driverLicenseNumber}</p></div>
            <div className="profile-field"><label>Vehicle</label><p>{driverProfile.vehicleModel} ({driverProfile.vehicleNumber})</p></div>
            <div className="profile-field"><label>Category</label><p>{driverProfile.vehicleCategory} / {driverProfile.vehicleClass}</p></div>
            <div className="profile-field"><label>Seats</label><p>{driverProfile.vehicleSeatCapacity}</p></div>
            <div className="profile-field"><label>Rating</label><p>⭐ {driverProfile.averageRatingOfDriver?.toFixed(1) || 'N/A'} ({driverProfile.totalReviewCount || 0} reviews)</p></div>
            <div className="profile-field"><label>Rides</label><p>✅ {driverProfile.totalCompletedRides || 0} completed / ❌ {driverProfile.totalCancelledRides || 0} cancelled</p></div>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="tab-content">
      <div className="tab-header"><h2>Become a Driver</h2></div>
      <p className="tab-desc">Register as a driver to start posting rides and earning money.</p>
      <form onSubmit={handleSubmit}>
        <div className="form-grid-2">
          <div className="form-group">
            <label className="form-label">License Number *</label>
            <input className="form-input" name="driverLicenseNumber" value={form.driverLicenseNumber} onChange={handleChange} placeholder="DL-XXXXXXXXX" />
          </div>
          <div className="form-group">
            <label className="form-label">License Expiry *</label>
            <input type="date" className="form-input" name="licenseExpirationDate" value={form.licenseExpirationDate} onChange={handleChange} />
          </div>
          <div className="form-group">
            <label className="form-label">Vehicle Model *</label>
            <input className="form-input" name="vehicleModel" value={form.vehicleModel} onChange={handleChange} placeholder="e.g. Swift Dzire" />
          </div>
          <div className="form-group">
            <label className="form-label">Vehicle Number *</label>
            <input className="form-input" name="vehicleNumber" value={form.vehicleNumber} onChange={handleChange} placeholder="e.g. DL 01 AB 1234" />
          </div>
          <div className="form-group">
            <label className="form-label">Vehicle Category</label>
            <select className="form-select" name="vehicleCategory" value={form.vehicleCategory} onChange={handleChange}>
              <option value="CAR">Car</option>
              <option value="SUV">SUV</option>
              <option value="BIKE">Bike</option>
              <option value="AUTO">Auto</option>
            </select>
          </div>
          <div className="form-group">
            <label className="form-label">Vehicle Class</label>
            <select className="form-select" name="vehicleClass" value={form.vehicleClass} onChange={handleChange}>
              <option value="ECONOMY">Economy</option>
              <option value="PREMIUM">Premium</option>
              <option value="LUXURY">Luxury</option>
            </select>
          </div>
          <div className="form-group">
            <label className="form-label">Seat Capacity</label>
            <input type="number" className="form-input" name="vehicleSeatCapacity" min="1" max="10" value={form.vehicleSeatCapacity} onChange={handleChange} />
          </div>
          <div className="form-group">
            <label className="form-label">Driving License Image *</label>
            <input type="file" className="form-input" accept="image/*" onChange={(e) => setFile(e.target.files[0])} />
          </div>
        </div>
        <button type="submit" className="btn btn-primary btn-lg" style={{ width: '100%', marginTop: '1.5rem' }} disabled={loading}>
          {loading ? 'Submitting...' : '🪪 Register as Driver'}
        </button>
      </form>
    </div>
  );
};

/* ===== Settings Tab ===== */
const SettingsTab = ({ showToast, logout }) => {
  const [passwordForm, setPasswordForm] = useState({ oldPassword: '', newPassword: '', confirmPassword: '' });
  const [loading, setLoading] = useState(false);

  const handleChangePassword = async (e) => {
    e.preventDefault();
    if (passwordForm.newPassword !== passwordForm.confirmPassword) {
      showToast('error', 'Passwords do not match');
      return;
    }
    setLoading(true);
    try {
      await userService.changePassword({
        oldPassword: passwordForm.oldPassword,
        newPassword: passwordForm.newPassword,
      });
      showToast('success', 'Password changed!');
      setPasswordForm({ oldPassword: '', newPassword: '', confirmPassword: '' });
    } catch (err) {
      showToast('error', err.response?.data?.message || 'Failed to change password');
    }
    setLoading(false);
  };

  const handleDeactivate = async () => {
    if (!window.confirm('Are you sure you want to deactivate your account?')) return;
    try {
      await userService.deactivateAccount();
      showToast('success', 'Account deactivated');
      logout();
    } catch (err) {
      showToast('error', err.response?.data?.message || 'Failed to deactivate');
    }
  };

  return (
    <div className="tab-content">
      <div className="tab-header"><h2>Settings</h2></div>

      <div className="settings-section">
        <h3>Change Password</h3>
        <form onSubmit={handleChangePassword}>
          <div className="form-group">
            <label className="form-label">Current Password</label>
            <input type="password" className="form-input" value={passwordForm.oldPassword} onChange={(e) => setPasswordForm({...passwordForm, oldPassword: e.target.value})} />
          </div>
          <div className="form-grid-2">
            <div className="form-group">
              <label className="form-label">New Password</label>
              <input type="password" className="form-input" value={passwordForm.newPassword} onChange={(e) => setPasswordForm({...passwordForm, newPassword: e.target.value})} />
            </div>
            <div className="form-group">
              <label className="form-label">Confirm New Password</label>
              <input type="password" className="form-input" value={passwordForm.confirmPassword} onChange={(e) => setPasswordForm({...passwordForm, confirmPassword: e.target.value})} />
            </div>
          </div>
          <button type="submit" className="btn btn-primary" disabled={loading}>
            {loading ? 'Changing...' : 'Change Password'}
          </button>
        </form>
      </div>

      <div className="settings-section danger-zone">
        <h3>Danger Zone</h3>
        <p>Deactivating your account will prevent you from logging in until you reactivate it.</p>
        <button className="btn btn-danger" onClick={handleDeactivate}>Deactivate Account</button>
      </div>
    </div>
  );
};

export default Dashboard;
