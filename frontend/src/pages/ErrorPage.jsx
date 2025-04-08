import {useRouteError, Link} from 'react-router-dom';

const ErrorPage = () => {
    const error = useRouteError();

    return (
        <div className="min-h-screen flex items-center justify-center p-4">
            <div className="text-center">
                <h1 className="text-4xl font-bold text-red-600 mb-4">
                    {error.status || 500}
                </h1>
                <p className="text-xl text-gray-800 mb-4">
                    {error.statusText || error.message}
                </p>
                <Link
                    to="/"
                    className="text-blue-600 hover:underline text-lg"
                >
                    Return to Home
                </Link>
            </div>
        </div>
    );
};

export default ErrorPage;