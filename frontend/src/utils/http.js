// src/utils/http.js
import axios from 'axios';
import Cookies from 'js-cookie';

// You can set REACT_APP_API_BASE_URL in .env or docker compose
const api = axios.create({
    baseURL: process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080',
    withCredentials: true
});

api.interceptors.request.use(config => {
    // read token from cookies
    const token = Cookies.get('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

api.interceptors.response.use(
    response => response,
    error => {
        // If 401, remove token
        if (error.response?.status === 401) {
            Cookies.remove('token');
            window.location = '/login';
        }
        return Promise.reject(error);
    }
);

export default api;
