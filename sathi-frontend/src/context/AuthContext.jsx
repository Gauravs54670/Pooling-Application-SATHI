import { createContext, useContext, useState, useEffect } from 'react';
import userService from '../services/userService';

const AuthContext = createContext(null);

export const useAuth = () => {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [roles, setRoles] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const storedCredentials = localStorage.getItem('sathi_credentials');
    const storedUser = localStorage.getItem('sathi_user');
    if (storedCredentials && storedUser) {
      try {
        setUser(JSON.parse(storedUser));
        fetchRoles();
      } catch {
        localStorage.removeItem('sathi_credentials');
        localStorage.removeItem('sathi_user');
      }
    }
    setLoading(false);
  }, []);

  const fetchRoles = async () => {
    try {
      const data = await userService.getRoles();
      setRoles(data.response || []);
    } catch {
      setRoles([]);
    }
  };

  const login = async (email, password) => {
    const credentials = btoa(`${email}:${password}`);
    localStorage.setItem('sathi_credentials', credentials);
    try {
      const data = await userService.getProfile();
      const userProfile = data.response;
      localStorage.setItem('sathi_user', JSON.stringify(userProfile));
      setUser(userProfile);
      await fetchRoles();
      return { success: true, user: userProfile };
    } catch (error) {
      localStorage.removeItem('sathi_credentials');
      localStorage.removeItem('sathi_user');
      const message = error.response?.data?.message || 'Login failed. Please check your credentials.';
      return { success: false, error: message };
    }
  };

  const logout = () => {
    localStorage.removeItem('sathi_credentials');
    localStorage.removeItem('sathi_user');
    setUser(null);
    setRoles([]);
  };

  const isAuthenticated = !!user;
  const hasRole = (role) => roles.includes(role);
  const isDriver = hasRole('DRIVER_ROLE');

  const refreshProfile = async () => {
    try {
      const data = await userService.getProfile();
      const userProfile = data.response;
      localStorage.setItem('sathi_user', JSON.stringify(userProfile));
      setUser(userProfile);
      await fetchRoles();
    } catch {
      // ignore
    }
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        roles,
        loading,
        isAuthenticated,
        isDriver,
        hasRole,
        login,
        logout,
        refreshProfile,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export default AuthContext;
