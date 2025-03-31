// src/pages/HomePage.jsx
import { useState } from 'react';
import SearchBar from '../components/Media/SearchBar';
import MediaGrid from '../components/Media/MediaGrid';
import FiltersPanel from '../components/Media/FiltersPanel';
import useMediaSearch from '../hooks/useMediaSearch';
import ErrorMessage from '../components/common/ErrorMessage';

const HomePage = () => {
    const [query, setQuery] = useState('');
    const [filters, setFilters] = useState({
        mediaType: '',
        license: '',
        source: ''
    });

    const { data, error, isLoading } = useMediaSearch(query, filters);

    return (
        <div className="flex flex-col lg:flex-row gap-6 p-4">
            <div className="lg:w-1/4">
                <FiltersPanel
                    initialFilters={filters}
                    onFilterChange={setFilters}
                />
            </div>

            <div className="lg:w-3/4 space-y-4">
                <SearchBar onSearch={setQuery} />
                <ErrorMessage error={error} />

                {isLoading ? (
                    <div className="text-center py-8">Loading results...</div>
                ) : (
                    <MediaGrid results={data?.results || []} />
                )}
            </div>
        </div>
    );
};

export default HomePage;
