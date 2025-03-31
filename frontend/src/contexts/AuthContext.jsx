// src/contexts/AuthContext.jsx
import { createContext, useContext, useState, useEffect } from 'react';
import { jwtDecode } from 'jwt-decode';
import Cookies from 'js-cookie';
import api from '../utils/http';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);

    // Attempt to restore user from cookie at startup
    useEffect(() => {
        const token = Cookies.get('token');
        if (token) {
            try {
                const decoded = jwtDecode(token);
                // Example: If your JWT has { sub: <userId>, username: ... }
                setUser({ id: decoded.sub, username: decoded.username });
            } catch {
                Cookies.remove('token');
            }
        }
    }, []);

    const login = async (credentials) => {
        // The backend returns a raw JWT string
        const { data: token } = await api.post('/api/auth/login', credentials);
        // Store token in cookies
        Cookies.set('token', token);

        // Decode token to get user info
        const decoded = jwtDecode(token);
        // Example: If your token has sub = userId
        const newUser = { id: decoded.sub, username: decoded.username };
        setUser(newUser);
    };

    const logout = () => {
        // We don't have a real /logout endpoint, so just remove the token
        Cookies.remove('token');
        setUser(null);
    };

    return (
        <AuthContext.Provider value={{ user, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);
