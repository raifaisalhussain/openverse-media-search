// src/components/Layout/AppHeader.jsx
import { Link } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';

const AppHeader = () => {
    const { user, logout } = useAuth();

    return (
        <header className="bg-white shadow-sm">
            <nav className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="flex justify-between h-16 items-center">
                    <Link to="/" className="flex items-center">
                        <img src="/logo.svg" alt="Logo" className="h-8 w-auto" />
                        <span className="ml-2 text-xl font-bold text-gray-900">Openverse</span>
                    </Link>

                    <div className="flex items-center space-x-4">
                        {user ? (
                            <>
                                <Link to="/profile" className="text-gray-700 hover:text-blue-600">
                                    History
                                </Link>
                                <button onClick={logout} className="text-gray-700 hover:text-blue-600">
                                    Logout
                                </button>
                            </>
                        ) : (
                            <Link to="/login" className="text-gray-700 hover:text-blue-600">
                                Login
                            </Link>
                        )}
                    </div>
                </div>
            </nav>
        </header>
    );
};

export default AppHeader;
