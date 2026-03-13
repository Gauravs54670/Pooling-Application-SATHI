import api from './api';

const authService = {
  register: async (email, phoneNumber, userFullName, password) => {
    const response = await api.post('/public/register-user', {
      email,
      phoneNumber,
      userFullName,
      password,
    });
    return response.data;
  },

  requestOTP: async (email) => {
    const response = await api.post(`/public/request-otp?email=${encodeURIComponent(email)}`);
    return response.data;
  },

  forgotPassword: async (email, otp, newPassword) => {
    const params = new URLSearchParams();
    params.append('email', email);
    params.append('OTP', otp);
    params.append('new password', newPassword);
    const response = await api.post(`/public/forgot-password?${params.toString()}`);
    return response.data;
  },
};

export default authService;
