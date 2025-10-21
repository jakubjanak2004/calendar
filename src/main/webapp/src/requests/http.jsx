import axios from "axios";
import {CONFIG} from "../constants.js";
import {useAuth} from "../pages/auth/AuthContext.jsx";

export const http = axios.create({
    baseURL: CONFIG.API_URL,
    timeout: CONFIG.TIMEOUT_MS,
    // If your Spring Boot uses cookie-based auth/CSRF, enable this:
    // withCredentials: true,
    headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
    },
});

// Optional: normalize errors
http.interceptors.response.use(
    (r) => r,
    (err) => {
        // You can centralize refresh-token logic here if needed
        return Promise.reject(err);
    }
);
