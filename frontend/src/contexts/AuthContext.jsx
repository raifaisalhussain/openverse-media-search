import {createContext, useContext, useState, useEffect} from 'react';
import axios from 'axios';
import {jwtDecode} from 'jwt-decode';

const AuthContext = createContext();

export function AuthProvider({children}) {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        axios.defaults.baseURL = 'http://localhost:8080';

        const token = localStorage.getItem('token');
        if (token) {
            try {
                const decoded = jwtDecode(token);
                setUser({username: decoded.sub});
                axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
            } catch (err) {
                console.error('Invalid token:', err);
                logout(false); // no redirect
            }
        }

        axios.get('/api/me', {withCredentials: true})
            .then((res) => {
                if (res.data.authenticated) {
                    setUser({
                        username: res.data.username || res.data.email,
                        email: res.data.email,
                        name: res.data.name,
                        googleSession: true,
                    });
                }
            })
            .catch(() => {
            })
            .finally(() => setLoading(false));
    }, []);

    async function login(credentials) {
        const {data: token} = await axios.post(
            '/api/auth/login',
            credentials,
            {headers: {'Content-Type': 'application/json'}}
        );

        localStorage.setItem('token', token);
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        const decoded = jwtDecode(token);
        setUser({username: decoded.sub});
    }

    function logout() {
        localStorage.removeItem('token');
        delete axios.defaults.headers.common['Authorization'];
        axios.post('/api/logout', {}, {withCredentials: true}).catch(console.error);
        setUser(null);
    }

    return (
        <AuthContext.Provider value={{user, login, logout, loading}}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    return useContext(AuthContext);
}
