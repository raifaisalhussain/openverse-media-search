import {useState} from 'react';
import {useNavigate} from 'react-router-dom';
import {useAuth} from '../../contexts/AuthContext';
import ErrorMessage from '../common/ErrorMessage';
import LoadingSpinner from '../common/LoadingSpinner';

const LoginForm = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const {login} = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');

        try {
            await login({username, password});
            navigate('/');
        } catch (err) {
            setError(err.response?.data || 'Login failed. Please check your credentials.');
        } finally {
            setLoading(false);
        }
    };

    // Redirect to Google OAuth endpoint
    function handleGoogleLogin() {
        window.location.href = 'http://localhost:8080/oauth2/authorization/google';
    }

    return (
        <div className="max-w-md mx-auto bg-white p-8 rounded shadow">
            <h2 className="text-2xl font-bold text-gray-800 mb-6">Login</h2>
            <form onSubmit={handleSubmit} className="space-y-4">
                <div>
                    <label htmlFor="username" className="block text-sm font-medium text-gray-700">
                        Username
                    </label>
                    <input
                        id="username"
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2"
                        required
                        autoComplete="username"
                    />
                </div>

                <div>
                    <label htmlFor="password" className="block text-sm font-medium text-gray-700">
                        Password
                    </label>
                    <input
                        id="password"
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2"
                        required
                        autoComplete="current-password"
                        minLength="6"
                    />
                </div>

                <ErrorMessage error={error}/>

                <button
                    type="submit"
                    disabled={loading}
                    className="w-full bg-blue-600 text-white py-2 px-4 rounded hover:bg-blue-700 transition-colors flex justify-center items-center"
                >
                    {loading ? <LoadingSpinner size="small"/> : 'Log In'}
                </button>

                <div className="text-center mt-4">
                    <button
                        type="button"
                        onClick={handleGoogleLogin}
                        className="w-full bg-red-600 text-white py-2 px-4 rounded hover:bg-red-700 transition-colors"
                    >
                        Login with Google
                    </button>
                </div>

                <div className="text-center text-sm text-gray-600 mt-4">
                    Don't have an account?{' '}
                    <a href="/register" className="text-blue-600 hover:underline">
                        Register here
                    </a>
                </div>
            </form>
        </div>
    );
};

export default LoginForm;
