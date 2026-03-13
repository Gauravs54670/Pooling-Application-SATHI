import api from './api';

const passengerService = {
  getAvailableRides: async (searchData) => {
    const response = await api.get('/passenger/get-availableRides', {
      data: searchData,
    });
    return response.data;
  },

  // Alternative: use POST with query params for search (GET with body may not work in all browsers)
  searchRides: async (searchData) => {
    const response = await api.get('/passenger/get-availableRides', {
      params: searchData,
      data: searchData,
    });
    return response.data;
  },

  requestRide: async (data) => {
    const response = await api.post('/passenger/request-ride', data);
    return response.data;
  },

  getDecisionResponse: async (requestId, rideCode) => {
    const response = await api.get(`/passenger/get-decision-response/${requestId}?rideCode=${encodeURIComponent(rideCode)}`);
    return response.data;
  },

  getMyRideRequests: async (date) => {
    const params = date ? `?date=${date}` : '';
    const response = await api.get(`/passenger/my-rideRequests${params}`);
    return response.data;
  },

  rateDriver: async (rideCode, rating, comment) => {
    const params = new URLSearchParams();
    params.append('rideCode', rideCode);
    params.append('rating', rating);
    params.append('comment', comment);
    const response = await api.post(`/passenger/rate?${params.toString()}`);
    return response.data;
  },
};

export default passengerService;
