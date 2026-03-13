import api from './api';

const passengerService = {
  /**
   * Search for available rides.
   * Backend uses GET with @RequestBody — browsers don't send body with GET.
   * We use axios.request with explicit method to force the body through.
   * If this still fails (some proxies strip GET body), the backend
   * endpoint should be changed to POST.
   */
  searchRides: async (searchData) => {
    const response = await api.request({
      method: 'GET',
      url: '/passenger/get-availableRides',
      data: searchData,
      headers: { 'Content-Type': 'application/json' },
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
