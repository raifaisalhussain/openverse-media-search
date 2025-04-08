import PropTypes from 'prop-types';

const LoadingSpinner = ({size = 'medium', className = ''}) => {
    const sizeConfig = {
        small: {
            dimensions: 'h-5 w-5',
            border: 'border-2'
        },
        medium: {
            dimensions: 'h-8 w-8',
            border: 'border-[3px]'
        },
        large: {
            dimensions: 'h-12 w-12',
            border: 'border-4'
        }
    };

    const {dimensions, border} = sizeConfig[size] || sizeConfig.medium;

    return (
        <div
            className={`animate-spin ${dimensions} ${className}`}
            role="status"
            aria-label="Loading"
        >
            <svg
                className="h-full w-full"
                viewBox="0 0 24 24"
                fill="none"
                xmlns="http://www.w3.org/2000/svg"
            >
                <circle
                    className="opacity-25"
                    cx="12"
                    cy="12"
                    r="10"
                    stroke="currentColor"
                    strokeWidth={border.split('-')[1]}
                    fill="none"
                />
                <path
                    className="opacity-75"
                    fill="currentColor"
                    d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
                />
            </svg>
        </div>
    );
};

LoadingSpinner.propTypes = {
    size: PropTypes.oneOf(['small', 'medium', 'large']),
    className: PropTypes.string
};

export default LoadingSpinner;