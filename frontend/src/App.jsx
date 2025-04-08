import {RouterProvider} from 'react-router-dom';
import {QueryClientProvider} from '@tanstack/react-query';
import {AuthProvider, useAuth} from './contexts/AuthContext';
import {queryClient} from './utils/queryClient';
import router from './routes/router';

function App() {
    return (
        <QueryClientProvider client={queryClient}>
            <AuthProvider>
                <AuthGate/>
            </AuthProvider>
        </QueryClientProvider>
    );
}

function AuthGate() {
    const {loading} = useAuth();

    if (loading) {
        return (
            <div className="flex items-center justify-center h-screen text-gray-700">
                <p className="text-lg font-semibold">Loading...</p>
            </div>
        );
    }

    return <RouterProvider router={router}/>;
}

export default App;
