// src/contexts/AuthContext.jsx
import { createContext, useContext, useState, useEffect } from 'react';
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';

const AuthContext = createContext();

export function AuthProvider({ children }) {
    const [user, setUser] = useState(null);

    useEffect(() => {
        // ✅ Set the base URL for all axios calls
        axios.defaults.baseURL = 'http://localhost:8080'; // <--- add this

        const token = localStorage.getItem('token');
        if (token) {
            try {
                const decoded = jwtDecode(token);
                setUser({ username: decoded.sub });

                // ✅ Set global default Authorization header
                axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
            } catch (err) {
                console.error("Invalid token:", err);
                logout();
            }
        }
    }, []);

    async function login(credentials) {
        // Because we set baseURL, it becomes POST /api/auth/login
        const { data: token } = await axios.post(
            '/api/auth/login',
            credentials,
            { headers: { 'Content-Type': 'application/json' } }
        );

        localStorage.setItem('token', token);
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;

        const decoded = jwtDecode(token);
        setUser({ username: decoded.sub });
    }

    function logout() {
        localStorage.removeItem('token');
        delete axios.defaults.headers.common['Authorization'];
        setUser(null);
    }

    return (
        <AuthContext.Provider value={{ user, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    return useContext(AuthContext);
}
