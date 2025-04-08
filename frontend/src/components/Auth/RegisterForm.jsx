import {useState} from 'react';
import {useNavigate} from 'react-router-dom';
import ErrorMessage from '../common/ErrorMessage';
import LoadingSpinner from '../common/LoadingSpinner';
import api from '../../utils/http';

const RegisterForm = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const [loading, setLoading] = useState(false);

    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');
        setSuccess('');

        try {
            await api.post('/api/auth/register', {username, password});
            setSuccess('Registration successful! You can now login.');
            setTimeout(() => navigate('/login'), 2000);
        } catch (err) {
            setError(err.response?.data || 'Registration failed. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    return (<div className="max-w-md mx-auto bg-white p-8 rounded shadow">
        <h2 className="text-2xl font-bold text-gray-800 mb-6">Register</h2>
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
                    minLength="3"
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
                    autoComplete="new-password"
                    minLength="6"
                />
            </div>

            <ErrorMessage error={error}/>
            {success && <div className="bg-green-100 text-green-700 p-2 rounded">{success}</div>}

            <button
                type="submit"
                disabled={loading}
                className="w-full bg-green-600 text-white py-2 px-4 rounded hover:bg-green-700 transition-colors flex justify-center items-center">
                {loading ? <LoadingSpinner size="small"/> : 'Register'}
            </button>

            <div className="text-center text-sm text-gray-600">
                Already have an account?{' '}
                <a href="/login" className="text-blue-600 hover:underline">
                    Login here
                </a>
            </div>
        </form>
    </div>);
};

export default RegisterForm;
