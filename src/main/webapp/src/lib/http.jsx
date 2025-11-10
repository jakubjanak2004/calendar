import axios from "axios";
import { CONFIG } from "../config/constants.js";

export const http = {
    _token: undefined,

    setToken(t) {
        this._token = t ?? undefined;
    },
    clearToken() {
        this._token = undefined;
    },

    client: axios.create({
        baseURL: CONFIG.API_URL,
        timeout: CONFIG.TIMEOUT_MS,
        // withCredentials: true, // if you switch to cookie auth
        headers: {
            "Content-Type": "application/json",
            Accept: "application/json",
        },
    }),
};

// attach interceptors
http.client.interceptors.request.use(async (cfg) => {
    const token = http._token;
    if (token) cfg.headers.Authorization = `Bearer ${token}`;
    return cfg;
})

http.client.interceptors.response.use(
    (r) => r,
    (err) => Promise.reject(err)
)
