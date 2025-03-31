// src/components/common/ErrorMessage.jsx
import PropTypes from 'prop-types';
import { ExclamationCircleIcon } from '@heroicons/react/24/solid';

const ErrorMessage = ({ error, className = '' }) => {
    if (!error) return null;

    const message = typeof error === 'string'
        ? error
        : error.message || 'An unexpected error occurred';

    return (
        <div
            role="alert"
            aria-live="polite"
            className={`bg-red-50 p-4 rounded-md flex items-start space-x-2 ${className}`}
        >
            <ExclamationCircleIcon className="h-5 w-5 text-red-600 shrink-0" />
            <div className="text-red-700 text-sm">
                <p className="font-medium">Error</p>
                <p>{message}</p>
            </div>
        </div>
    );
};

ErrorMessage.propTypes = {
    error: PropTypes.oneOfType([
        PropTypes.string,
        PropTypes.instanceOf(Error),
        PropTypes.shape({
            message: PropTypes.string
        })
    ]),
    className: PropTypes.string
};

export default ErrorMessage;