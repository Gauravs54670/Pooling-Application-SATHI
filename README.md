# 🚗 SATHI — Carpooling Application

**SATHI** (Share A Trip, Help India) is a full-stack carpooling/ride-sharing platform built with Spring Boot (backend) and React + Vite (frontend). Drivers can post rides, passengers can search and request to join — making commuting affordable, eco-friendly, and social.

---

## 📸 Screenshots

### Landing Page
![Landing Page Hero](./screenshots/landing-hero.png)

### Login & Registration
![Auth Pages](./screenshots/auth-pages.png)

---

## 🏗️ Tech Stack

| Layer        | Technology                                                        |
| ------------ | ----------------------------------------------------------------- |
| **Backend**  | Java 21, Spring Boot 4.x, Spring Security, Spring Data JPA, MySQL |
| **Frontend** | React 18, Vite, React Router v6, Axios, Framer Motion             |
| **Auth**     | HTTP Basic Authentication (stateless)                              |
| **Storage**  | Cloudinary (driver license / profile images)                       |
| **Email**    | Spring Mail (Gmail SMTP for OTP)                                   |

---

## 📁 Project Structure

```
CarPoolingApplication/
├── src/                          # Spring Boot backend
│   └── main/
│       ├── java/com/gaurav/CarPoolingApplication/
│       │   ├── Configuration/    # Security, CORS, Cloudinary config
│       │   ├── Controller/       # REST controllers (Public, User, Driver, Passenger)
│       │   ├── DTO/              # Request/Response DTOs
│       │   ├── Entity/           # JPA Entities
│       │   ├── Repository/       # Spring Data repositories
│       │   └── Service/          # Business logic
│       └── resources/
│           ├── application.properties
│           └── application.yml
├── sathi-frontend/               # React frontend
│   ├── src/
│   │   ├── components/           # Navbar, Footer, RideCard, ProtectedRoute
│   │   ├── context/              # AuthContext (login state management)
│   │   ├── pages/                # Landing, Dashboard, Search, Publish, Auth pages
│   │   └── services/             # API service layer (Axios)
│   ├── index.html
│   ├── vite.config.js
│   └── package.json
└── pom.xml
```

---

## 🚀 Getting Started (Local Setup)

### Prerequisites

Make sure you have the following installed on your machine:

| Tool         | Version   | Download Link                                       |
| ------------ | --------- | --------------------------------------------------- |
| **Java JDK** | 21+       | https://adoptium.net/                               |
| **Maven**    | 3.9+      | https://maven.apache.org/download.cgi               |
| **MySQL**    | 8.0+      | https://dev.mysql.com/downloads/installer/           |
| **Node.js**  | 18+       | https://nodejs.org/                                  |
| **npm**      | 9+        | Comes bundled with Node.js                           |
| **Git**      | Latest    | https://git-scm.com/                                 |

---

### Step 1: Clone the Repository

```bash
git clone https://github.com/dubeysanskar/Pooling-Application-SATHI.git
cd Pooling-Application-SATHI
```

---

### Step 2: Set Up the Database (MySQL)

Open MySQL and create the database:

```sql
CREATE DATABASE carpoolingapplication;
```

> **Note:** The default credentials in `application.properties` are `root`/`root`. If your MySQL uses different credentials, update them in `src/main/resources/application.properties`:
>
> ```properties
> spring.datasource.username = your_username
> spring.datasource.password = your_password
> ```

---

### Step 3: Set Environment Variables

The backend requires these environment variables for email and Cloudinary services:

#### Windows (PowerShell)

```powershell
$env:MAIL_USERNAME = "your-gmail@gmail.com"
$env:MAIL_PASSWORD = "your-gmail-app-password"
$env:CLOUDINARY_CLOUD_NAME = "your-cloud-name"
$env:CLOUDINARY_API_KEY = "your-api-key"
$env:CLOUDINARY_SECRET_KEY = "your-secret-key"
```

#### macOS / Linux (Terminal)

```bash
export MAIL_USERNAME="your-gmail@gmail.com"
export MAIL_PASSWORD="your-gmail-app-password"
export CLOUDINARY_CLOUD_NAME="your-cloud-name"
export CLOUDINARY_API_KEY="your-api-key"
export CLOUDINARY_SECRET_KEY="your-secret-key"
```

> **How to get these:**
>
> - **Gmail App Password:** Go to [Google Account → Security → App Passwords](https://myaccount.google.com/apppasswords) and generate one.
> - **Cloudinary:** Sign up at [cloudinary.com](https://cloudinary.com), go to Dashboard and copy your Cloud Name, API Key, and API Secret.

---

### Step 4: Run the Backend

```bash
# From the project root directory
mvn spring-boot:run
```

The backend will start at: **http://localhost:8080**

API base path: `http://localhost:8080/api/car-pooling`

> **Verify:** Open `http://localhost:8080/api/car-pooling/public/register-user` in your browser — you should see a response (even if it's an error, it means the server is running).

---

### Step 5: Set Up and Run the Frontend

```bash
# Navigate to the frontend directory
cd sathi-frontend

# Install dependencies
npm install

# Start the development server
npm run dev
```

The frontend will start at: **http://localhost:5173**

> The Vite dev server is configured to proxy all `/api` requests to `http://localhost:8080`, so the frontend talks to the backend seamlessly.

---

### Step 6: Open the App

Open your browser and go to:

🔗 **http://localhost:5173**

You should see the SATHI landing page with the animated hero section!

---

## 🔌 API Endpoints Overview

### Public (No Auth Required)

| Method | Endpoint                                   | Description          |
| ------ | ------------------------------------------ | -------------------- |
| POST   | `/api/car-pooling/public/register-user`    | Register new user    |
| POST   | `/api/car-pooling/public/request-otp`      | Request OTP (email)  |
| POST   | `/api/car-pooling/public/forgot-password`  | Reset password       |

### User (Auth Required)

| Method | Endpoint                                    | Description            |
| ------ | ------------------------------------------- | ---------------------- |
| GET    | `/api/car-pooling/user/my-profile`          | Get user profile       |
| PUT    | `/api/car-pooling/user/update-profile`      | Update profile         |
| PUT    | `/api/car-pooling/user/change-password`     | Change password        |
| POST   | `/api/car-pooling/user/register-driver`     | Register as driver     |
| GET    | `/api/car-pooling/user/my-roles`            | Get user roles         |

### Driver (Auth + DRIVER Role Required)

| Method | Endpoint                                          | Description              |
| ------ | ------------------------------------------------- | ------------------------ |
| GET    | `/api/car-pooling/driver/my-profile`              | Get driver profile       |
| POST   | `/api/car-pooling/driver/post-ride`               | Post a new ride          |
| GET    | `/api/car-pooling/driver/my-rides`                | List posted rides        |
| GET    | `/api/car-pooling/driver/requested-rides/{code}`  | View booking requests    |
| POST   | `/api/car-pooling/driver/ride-request/{decision}` | Accept/Reject booking    |

### Passenger (Auth Required)

| Method | Endpoint                                          | Description              |
| ------ | ------------------------------------------------- | ------------------------ |
| POST   | `/api/car-pooling/passenger/search-ride`          | Search available rides   |
| POST   | `/api/car-pooling/passenger/request-ride`         | Request to join a ride   |
| GET    | `/api/car-pooling/passenger/my-ride-requests`     | View my ride requests    |
| POST   | `/api/car-pooling/passenger/rate-driver`          | Rate a driver            |

---

## 🎨 Frontend Pages

| Page             | Route              | Description                                     |
| ---------------- | ------------------ | ----------------------------------------------- |
| Landing          | `/`                | Hero, features, how-it-works, testimonials, CTA |
| Login            | `/login`           | Email + password sign-in                        |
| Register         | `/register`        | Create new account                              |
| Forgot Password  | `/forgot-password` | OTP-based password reset (2-step)               |
| Search Rides     | `/search`          | Search and request rides                        |
| Publish Ride     | `/publish`         | Post a ride (drivers only)                      |
| Dashboard        | `/dashboard`       | Profile, rides, bookings, settings              |

---

## 🔐 Authentication

The app uses **HTTP Basic Authentication** (stateless). When a user logs in:

1. Credentials are Base64-encoded as `email:password`
2. Stored in `localStorage` as `sathi_credentials`
3. Sent with every API request via the `Authorization: Basic <token>` header
4. Backend validates credentials on each request (no sessions)

---

## 🛠️ Troubleshooting

| Issue                              | Solution                                                                 |
| ---------------------------------- | ------------------------------------------------------------------------ |
| `ECONNREFUSED` on frontend         | Make sure the backend is running on port 8080                            |
| MySQL connection refused            | Verify MySQL is running and the database `carpoolingapplication` exists   |
| Cloudinary upload fails             | Check your Cloudinary env variables are set correctly                    |
| OTP email not received              | Verify Gmail App Password and `MAIL_USERNAME` / `MAIL_PASSWORD` env vars |
| Frontend blank page                 | Run `npm install` in `sathi-frontend/` and restart with `npm run dev`    |
| CORS error in browser               | Backend CORS is configured for `localhost:5173` — make sure ports match  |

---

## 📜 License

This project is for educational purposes.

---

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

**Made with ❤️ by Sanskar Dubey**
