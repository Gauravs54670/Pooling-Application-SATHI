import axios from 'axios';

const API_BASE_URL = '/api/car-pooling';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to attach auth credentials
api.interceptors.request.use(
  (config) => {
    const credentials = localStorage.getItem('sathi_credentials');
    if (credentials) {
      config.headers.Authorization = `Basic ${credentials}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response interceptor for error handling
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response) {
      if (error.response.status === 401) {
        localStorage.removeItem('sathi_credentials');
        localStorage.removeItem('sathi_user');
        if (window.location.pathname !== '/login') {
          window.location.href = '/login';
        }
      }
    }
    return Promise.reject(error);
  }
);

export default api;
