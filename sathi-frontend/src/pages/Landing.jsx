import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import { useAuth } from '../context/AuthContext';
import './Landing.css';

const fadeInUp = {
  hidden: { opacity: 0, y: 40 },
  visible: (i = 0) => ({ opacity: 1, y: 0, transition: { delay: i * 0.15, duration: 0.6, ease: 'easeOut' } }),
};

const staggerContainer = {
  hidden: {},
  visible: { transition: { staggerChildren: 0.15 } },
};

const Landing = () => {
  const navigate = useNavigate();
  const { isAuthenticated } = useAuth();
  const [from, setFrom] = useState('');
  const [to, setTo] = useState('');

  const handleSearch = (e) => {
    e.preventDefault();
    navigate(`/search?from=${encodeURIComponent(from)}&to=${encodeURIComponent(to)}`);
  };

  const features = [
    { icon: '🛡️', title: 'Verified Drivers', desc: 'Every driver is verified with license and vehicle documents for your safety.' },
    { icon: '💰', title: 'Save Money', desc: 'Split fuel costs equally. Save up to 75% compared to solo driving or cabs.' },
    { icon: '🌱', title: 'Eco-Friendly', desc: 'Reduce carbon emissions by sharing rides. One car, multiple happy passengers.' },
    { icon: '⭐', title: 'Rated Community', desc: 'Rate and review after every ride. Build trust in the SATHI community.' },
    { icon: '📱', title: 'Real-time Tracking', desc: 'Track your ride in real-time. Know exactly when your driver arrives.' },
    { icon: '🔒', title: 'Secure Payments', desc: 'Transparent pricing. Know your fare before you book.' },
  ];

  const howItWorks = [
    { step: '01', icon: '🔍', title: 'Search or Post', desc: 'Looking for a ride? Search by route. Got a car? Post your ride in seconds.' },
    { step: '02', icon: '🤝', title: 'Match & Book', desc: 'Find the perfect ride match. Send a request and get instant confirmation.' },
    { step: '03', icon: '🚗', title: 'Ride Together', desc: 'Share the journey, share the costs, and make new connections on the way.' },
  ];

  const stats = [
    { number: '50K+', label: 'Happy Riders' },
    { number: '10K+', label: 'Verified Drivers' },
    { number: '200+', label: 'Cities Covered' },
    { number: '₹2Cr+', label: 'Money Saved' },
  ];

  const popularRoutes = [
    { from: 'Delhi', to: 'Jaipur', price: '₹450', time: '5h' },
    { from: 'Mumbai', to: 'Pune', price: '₹350', time: '3h' },
    { from: 'Bangalore', to: 'Mysore', price: '₹300', time: '3h' },
    { from: 'Chennai', to: 'Pondicherry', price: '₹280', time: '2.5h' },
    { from: 'Hyderabad', to: 'Vijayawada', price: '₹400', time: '4h' },
    { from: 'Ahmedabad', to: 'Surat', price: '₹350', time: '4h' },
  ];

  const testimonials = [
    { name: 'Priya Sharma', city: 'Delhi', text: 'SATHI saved me thousands on my weekly Delhi-Jaipur commute. The drivers are always verified and friendly!', rating: 5 },
    { name: 'Rahul Verma', city: 'Mumbai', text: 'As a driver, I love earning extra while helping people commute. The app is super easy to use.', rating: 5 },
    { name: 'Ananya Patel', city: 'Bangalore', text: 'Safe, affordable, and eco-friendly. What more can you ask for? Best carpooling platform in India.', rating: 5 },
  ];

  return (
    <div className="landing">
      {/* Hero Section */}
      <section className="hero">
        <div className="hero-bg">
          <div className="hero-gradient" />
          <div className="hero-particles">
            {[...Array(20)].map((_, i) => (
              <div key={i} className="particle" style={{
                left: `${Math.random() * 100}%`,
                top: `${Math.random() * 100}%`,
                animationDelay: `${Math.random() * 5}s`,
                animationDuration: `${3 + Math.random() * 4}s`,
              }} />
            ))}
          </div>
        </div>
        <div className="container hero-content">
          <motion.div className="hero-text" initial="hidden" animate="visible" variants={staggerContainer}>
            <motion.h1 variants={fadeInUp} className="hero-title">
              Share Rides,<br />
              <span className="gradient-text">Share Smiles</span>
            </motion.h1>
            <motion.p variants={fadeInUp} custom={1} className="hero-subtitle">
              India's most trusted carpooling platform. Save money, reduce emissions, and make every journey better — together.
            </motion.p>
            <motion.form variants={fadeInUp} custom={2} className="hero-search" onSubmit={handleSearch}>
              <div className="search-inputs">
                <div className="search-field">
                  <span className="search-icon">📍</span>
                  <input
                    type="text"
                    placeholder="Leaving from..."
                    value={from}
                    onChange={(e) => setFrom(e.target.value)}
                    className="search-input"
                  />
                </div>
                <div className="search-divider" />
                <div className="search-field">
                  <span className="search-icon">🎯</span>
                  <input
                    type="text"
                    placeholder="Going to..."
                    value={to}
                    onChange={(e) => setTo(e.target.value)}
                    className="search-input"
                  />
                </div>
                <button type="submit" className="btn btn-accent btn-lg search-btn">
                  Search
                </button>
              </div>
            </motion.form>
            <motion.div variants={fadeInUp} custom={3} className="hero-cta-row">
              {!isAuthenticated && (
                <Link to="/register" className="btn btn-outline btn-lg hero-cta-btn">
                  Get Started Free →
                </Link>
              )}
            </motion.div>
          </motion.div>
        </div>
        <div className="hero-scroll-indicator">
          <motion.div
            animate={{ y: [0, 10, 0] }}
            transition={{ repeat: Infinity, duration: 1.5 }}
            className="scroll-arrow"
          >
            ↓
          </motion.div>
        </div>
      </section>

      {/* Stats Strip */}
      <section className="stats-strip">
        <div className="container">
          <motion.div
            className="stats-grid"
            initial="hidden"
            whileInView="visible"
            viewport={{ once: true, margin: '-100px' }}
            variants={staggerContainer}
          >
            {stats.map((stat, i) => (
              <motion.div key={i} className="stat-item" variants={fadeInUp} custom={i}>
                <span className="stat-number">{stat.number}</span>
                <span className="stat-label">{stat.label}</span>
              </motion.div>
            ))}
          </motion.div>
        </div>
      </section>

      {/* How It Works */}
      <section className="section how-it-works">
        <div className="container">
          <motion.h2 className="section-title" initial="hidden" whileInView="visible" viewport={{ once: true }} variants={fadeInUp}>
            How It Works
          </motion.h2>
          <motion.p className="section-subtitle" initial="hidden" whileInView="visible" viewport={{ once: true }} variants={fadeInUp} custom={1}>
            Getting started with SATHI is as easy as 1-2-3
          </motion.p>
          <motion.div
            className="steps-grid"
            initial="hidden"
            whileInView="visible"
            viewport={{ once: true, margin: '-50px' }}
            variants={staggerContainer}
          >
            {howItWorks.map((item, i) => (
              <motion.div key={i} className="step-card" variants={fadeInUp} custom={i}>
                <div className="step-number">{item.step}</div>
                <div className="step-icon">{item.icon}</div>
                <h3>{item.title}</h3>
                <p>{item.desc}</p>
              </motion.div>
            ))}
          </motion.div>
        </div>
      </section>

      {/* Features */}
      <section className="section features-section">
        <div className="container">
          <motion.h2 className="section-title" initial="hidden" whileInView="visible" viewport={{ once: true }} variants={fadeInUp}>
            Why Choose SATHI?
          </motion.h2>
          <motion.p className="section-subtitle" initial="hidden" whileInView="visible" viewport={{ once: true }} variants={fadeInUp} custom={1}>
            Built for safety, savings, and sustainability
          </motion.p>
          <motion.div
            className="features-grid"
            initial="hidden"
            whileInView="visible"
            viewport={{ once: true, margin: '-50px' }}
            variants={staggerContainer}
          >
            {features.map((feature, i) => (
              <motion.div key={i} className="feature-card" variants={fadeInUp} custom={i}>
                <div className="feature-icon">{feature.icon}</div>
                <h3>{feature.title}</h3>
                <p>{feature.desc}</p>
              </motion.div>
            ))}
          </motion.div>
        </div>
      </section>

      {/* Popular Routes */}
      <section className="section popular-routes">
        <div className="container">
          <motion.h2 className="section-title" initial="hidden" whileInView="visible" viewport={{ once: true }} variants={fadeInUp}>
            Popular Routes
          </motion.h2>
          <motion.p className="section-subtitle" initial="hidden" whileInView="visible" viewport={{ once: true }} variants={fadeInUp} custom={1}>
            Thousands of rides daily on India's busiest routes
          </motion.p>
          <motion.div
            className="routes-grid"
            initial="hidden"
            whileInView="visible"
            viewport={{ once: true, margin: '-50px' }}
            variants={staggerContainer}
          >
            {popularRoutes.map((route, i) => (
              <motion.div
                key={i}
                className="route-card"
                variants={fadeInUp}
                custom={i}
                whileHover={{ y: -6, boxShadow: '0 20px 40px rgba(0,0,0,0.1)' }}
              >
                <div className="route-cities">
                  <span className="city-from">{route.from}</span>
                  <span className="route-arrow">→</span>
                  <span className="city-to">{route.to}</span>
                </div>
                <div className="route-meta">
                  <span className="route-price">{route.price}</span>
                  <span className="route-time">~{route.time}</span>
                </div>
              </motion.div>
            ))}
          </motion.div>
        </div>
      </section>

      {/* Testimonials */}
      <section className="section testimonials-section">
        <div className="container">
          <motion.h2 className="section-title" initial="hidden" whileInView="visible" viewport={{ once: true }} variants={fadeInUp}>
            What Our Riders Say
          </motion.h2>
          <motion.div
            className="testimonials-grid"
            initial="hidden"
            whileInView="visible"
            viewport={{ once: true, margin: '-50px' }}
            variants={staggerContainer}
          >
            {testimonials.map((t, i) => (
              <motion.div key={i} className="testimonial-card" variants={fadeInUp} custom={i}>
                <div className="testimonial-stars">
                  {'⭐'.repeat(t.rating)}
                </div>
                <p className="testimonial-text">"{t.text}"</p>
                <div className="testimonial-author">
                  <div className="author-avatar">{t.name.charAt(0)}</div>
                  <div>
                    <strong>{t.name}</strong>
                    <span>{t.city}</span>
                  </div>
                </div>
              </motion.div>
            ))}
          </motion.div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="cta-section">
        <div className="container">
          <motion.div
            className="cta-content"
            initial="hidden"
            whileInView="visible"
            viewport={{ once: true }}
            variants={staggerContainer}
          >
            <motion.h2 variants={fadeInUp}>Ready to Start Your Journey?</motion.h2>
            <motion.p variants={fadeInUp} custom={1}>
              Join thousands of smart commuters saving money and the planet.
            </motion.p>
            <motion.div variants={fadeInUp} custom={2} className="cta-buttons">
              <Link to="/register" className="btn btn-accent btn-lg">Join SATHI Now</Link>
              <Link to="/search" className="btn btn-outline btn-lg cta-outline-btn">Find a Ride</Link>
            </motion.div>
          </motion.div>
        </div>
      </section>
    </div>
  );
};

export default Landing;
