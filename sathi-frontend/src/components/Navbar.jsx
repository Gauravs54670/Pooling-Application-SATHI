import { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { motion, AnimatePresence } from 'framer-motion';
import './Navbar.css';

const Navbar = () => {
  const { isAuthenticated, isDriver, user, logout } = useAuth();
  const [scrolled, setScrolled] = useState(false);
  const [menuOpen, setMenuOpen] = useState(false);
  const location = useLocation();
  const navigate = useNavigate();

  useEffect(() => {
    const onScroll = () => setScrolled(window.scrollY > 20);
    window.addEventListener('scroll', onScroll);
    return () => window.removeEventListener('scroll', onScroll);
  }, []);

  useEffect(() => {
    setMenuOpen(false);
  }, [location]);

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  const isLanding = location.pathname === '/';

  return (
    <nav className={`navbar ${scrolled ? 'scrolled' : ''} ${isLanding && !scrolled ? 'transparent' : ''}`}>
      <div className="navbar-container">
        <Link to="/" className="navbar-logo">
          <span className="logo-icon">🚗</span>
          <span className="logo-text">SATHI</span>
        </Link>

        <div className="navbar-links-desktop">
          <Link to="/search" className="nav-link">Search Rides</Link>
          {isDriver && <Link to="/publish" className="nav-link">Offer a Ride</Link>}
          {isAuthenticated ? (
            <>
              <Link to="/dashboard" className="nav-link">Dashboard</Link>
              <div className="nav-user-section">
                <Link to="/dashboard" className="nav-avatar">
                  {user?.userFullName?.charAt(0) || 'U'}
                </Link>
                <button onClick={handleLogout} className="btn btn-ghost btn-sm">Logout</button>
              </div>
            </>
          ) : (
            <div className="nav-auth-buttons">
              <Link to="/login" className="btn btn-ghost">Login</Link>
              <Link to="/register" className="btn btn-primary">Sign Up</Link>
            </div>
          )}
        </div>

        <button className={`hamburger ${menuOpen ? 'active' : ''}`} onClick={() => setMenuOpen(!menuOpen)} aria-label="Toggle menu">
          <span /><span /><span />
        </button>
      </div>

      <AnimatePresence>
        {menuOpen && (
          <motion.div
            className="mobile-menu"
            initial={{ opacity: 0, height: 0 }}
            animate={{ opacity: 1, height: 'auto' }}
            exit={{ opacity: 0, height: 0 }}
            transition={{ duration: 0.3 }}
          >
            <Link to="/search" className="mobile-link">Search Rides</Link>
            {isDriver && <Link to="/publish" className="mobile-link">Offer a Ride</Link>}
            {isAuthenticated ? (
              <>
                <Link to="/dashboard" className="mobile-link">Dashboard</Link>
                <button onClick={handleLogout} className="mobile-link mobile-link-btn">Logout</button>
              </>
            ) : (
              <>
                <Link to="/login" className="mobile-link">Login</Link>
                <Link to="/register" className="mobile-link highlight">Sign Up</Link>
              </>
            )}
          </motion.div>
        )}
      </AnimatePresence>
    </nav>
  );
};

export default Navbar;
