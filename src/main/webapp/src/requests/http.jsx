import axios from "axios";
import {CONFIG} from "../constants.js";

export const http = axios.create({
    baseURL: CONFIG.API_URL,
    timeout: CONFIG.TIMEOUT_MS,
    // If Spring Boot will be using cookie-based auth/CSRF, enable this:
    // withCredentials: true,
    headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
    },
});

let getToken = null;

export function setAuthTokenProvider(fn) {
    getToken = fn;
}

http.interceptors.request.use((cfg) => {
    const token = getToken && getToken();
    if (token) cfg.headers.Authorization = `Bearer ${token}`;
    return cfg;
});

http.interceptors.response.use(
    (r) => r,
    (err) => {
        // You can centralize refresh-token logic here if needed
        return Promise.reject(err);
    }
);
