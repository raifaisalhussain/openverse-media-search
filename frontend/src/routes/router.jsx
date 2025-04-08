import {createBrowserRouter} from 'react-router-dom';
import MainLayout from '../components/Layout/MainLayout';
import HomePage from '../pages/HomePage';
import ProfilePage from '../pages/ProfilePage';
import ErrorPage from '../pages/ErrorPage';
import ProtectedRoute from '../components/Layout/ProtectedRoute';

import LoginForm from '../components/Auth/LoginForm';
import RegisterForm from '../components/Auth/RegisterForm';

const router = createBrowserRouter([
    {
        path: '/',
        element: <MainLayout/>,
        errorElement: <ErrorPage/>,
        children: [
            {
                index: true,
                element: <HomePage/>
            },
            {
                path: 'login',
                element: <LoginForm/>
            },
            {
                path: 'register',
                element: <RegisterForm/>
            },
            {
                element: <ProtectedRoute/>,
                children: [
                    {
                        path: 'profile',
                        element: <ProfilePage/>
                    }
                ]
            }
        ]
    }
]);

export default router;
