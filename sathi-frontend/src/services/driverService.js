import api from './api';

const driverService = {
  getProfile: async () => {
    const response = await api.get('/driver/myProfile');
    return response.data;
  },

  updateProfile: async (data) => {
    const response = await api.put('/driver/update-myProfile', data);
    return response.data;
  },

  updatePhoto: async (file) => {
    const formData = new FormData();
    formData.append('profile photo', file);
    const response = await api.put('/driver/update-profile', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
    return response.data;
  },

  postRide: async (data) => {
    const response = await api.post('/driver/post-ride', data);
    return response.data;
  },

  getMyRides: async () => {
    const response = await api.get('/driver/get-myRides');
    return response.data;
  },

  cancelRide: async (rideCode) => {
    const response = await api.put(`/driver/cancel-ride?rideCode=${encodeURIComponent(rideCode)}`);
    return response.data;
  },

  getRequestedRides: async (rideCode) => {
    const response = await api.get(`/driver/requested-rides?rideCode=${encodeURIComponent(rideCode)}`);
    return response.data;
  },

  rideRequestDecision: async (decision, bookingData) => {
    const response = await api.put(`/driver/ride-request-decision?decision=${encodeURIComponent(decision)}`, bookingData);
    return response.data;
  },

  startRide: async (rideCode, otp) => {
    const params = new URLSearchParams();
    params.append('rideCode', rideCode);
    params.append('OTP', otp);
    const response = await api.post(`/driver/start-ride?${params.toString()}`);
    return response.data;
  },

  completeRide: async (rideCode) => {
    const response = await api.post(`/driver/ride-completed?rideCode=${encodeURIComponent(rideCode)}`);
    return response.data;
  },

  changeAvailability: async (status) => {
    const formData = new FormData();
    formData.append('status', status);
    const response = await api.put('/driver/change-availability', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
    return response.data;
  },
};

export default driverService;
