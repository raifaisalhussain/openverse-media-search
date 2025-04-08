import {useState, useEffect} from 'react';
import PropTypes from 'prop-types';

const FiltersPanel = ({initialFilters, onFilterChange}) => {
    const [filters, setFilters] = useState(initialFilters);

    const filterOptions = {
        mediaType: [
            {value: '', label: 'All Media Types'},
            {value: 'image', label: 'Images'},
            {value: 'audio', label: 'Audio'}
        ],
        license: [
            {value: '', label: 'All Licenses'},
            {value: 'cc0', label: 'CC0 Public Domain'},
            {value: 'cc-by', label: 'CC BY'}
        ],
        source: [
            {value: '', label: 'All Sources'},
            {value: 'flickr', label: 'Flickr'},
            {value: 'wikimedia', label: 'Wikimedia'}
        ]
    };

    useEffect(() => {
        onFilterChange(filters);
    }, [filters]);

    const handleChange = (filterType) => (e) => {
        setFilters(prev => ({
            ...prev,
            [filterType]: e.target.value
        }));
    };

    return (
        <div className="bg-white p-4 rounded-lg shadow-md">
            <h3 className="text-lg font-semibold mb-4">Filter Results</h3>
            <div className="space-y-4">
                {Object.entries(filterOptions).map(([filterKey, options]) => (
                    <div key={filterKey}>
                        <label className="block text-sm font-medium text-gray-700 mb-1 capitalize">
                            {filterKey.replace(/([A-Z])/g, ' $1')}
                        </label>
                        <select
                            className="w-full border border-gray-300 rounded-md shadow-sm p-2"
                            value={filters[filterKey]}
                            onChange={handleChange(filterKey)}
                        >
                            {options.map(option => (
                                <option key={option.value} value={option.value}>
                                    {option.label}
                                </option>
                            ))}
                        </select>
                    </div>
                ))}
            </div>
        </div>
    );
};

FiltersPanel.propTypes = {
    initialFilters: PropTypes.object.isRequired,
    onFilterChange: PropTypes.func.isRequired
};

export default FiltersPanel;