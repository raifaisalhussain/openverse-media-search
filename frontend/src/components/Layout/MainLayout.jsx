import { Outlet } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import AppHeader from './AppHeader'; // Renamed component

const MainLayout = () => {
    const { user } = useAuth(); // Remove logout from here

    return (
        <div className="min-h-screen flex flex-col">
            <AppHeader />

            <main className="flex-1 bg-gray-50">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                    <Outlet />
                </div>
            </main>

            <footer className="bg-white border-t border-gray-200">
                <div className="max-w-7xl mx-auto py-4 px-4 sm:px-6 lg:px-8 text-center text-sm text-gray-500">
                    © {new Date().getFullYear()} Openverse Media Search. All media content is licensed under Creative Commons.
                </div>
            </footer>
        </div>
    );
};


export default MainLayout;