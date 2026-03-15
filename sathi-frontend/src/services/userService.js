import api from './api';

const userService = {
  getProfile: async () => {
    const response = await api.get('/user/myProfile');
    return response.data;
  },

  updateProfile: async (data) => {
    const response = await api.put('/user/update-myProfile', data);
    return response.data;
  },

  changePassword: async (data) => {
    const response = await api.put('/user/change-password', data);
    return response.data;
  },

  getRoles: async () => {
    const response = await api.get('/user/get-roles');
    return response.data;
  },

  deactivateAccount: async () => {
    const response = await api.put('/user/deactivate-account');
    return response.data;
  },

  activateAccount: async () => {
    const response = await api.put('/user/activate-account');
    return response.data;
  },

  registerDriver: async (licenseFile, driverProfileRequest) => {
    const formData = new FormData();
    formData.append('media', licenseFile);
    formData.append('driverProfileRequest', JSON.stringify(driverProfileRequest));
    const response = await api.post('/user/register-driver', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
    return response.data;
  },
};

export default userService;
