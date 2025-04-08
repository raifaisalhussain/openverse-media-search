import {useState} from 'react';
import SearchBar from '../components/Media/SearchBar';
import MediaGrid from '../components/Media/MediaGrid';
import axios from 'axios';
import {useAuth} from '../contexts/AuthContext';

export default function HomePage() {
    const [mediaResults, setMediaResults] = useState([]);
    const [searchResponse, setSearchResponse] = useState(null);
    const [error, setError] = useState('');
    const {user} = useAuth();

    const handleSearch = async ({query, mediaType, license, source}) => {
        try {
            setError('');
            const res = await axios.get(`${process.env.REACT_APP_API_BASE_URL}/api/media/search`, {
                params: {query, mediaType, license, source, page: 1},
                withCredentials: true
            });
            setSearchResponse(res.data);
            setMediaResults(res.data.media || []);
        } catch (err) {
            setError('Search failed.');
        }
    };

    const handleNext = async () => {
        if (!searchResponse?.nextPage) return;
        try {
            setError('');
            const res = await axios.get(searchResponse.nextPage, {withCredentials: true});
            setSearchResponse(res.data);
            setMediaResults(res.data.media || []);
        } catch (err) {
            setError('Search failed.');
        }
    };

    const handlePrev = async () => {
        if (!searchResponse?.previousPage) return;
        try {
            setError('');
            const res = await axios.get(searchResponse.previousPage, {withCredentials: true});
            setSearchResponse(res.data);
            setMediaResults(res.data.media || []);
        } catch (err) {
            setError('Search failed.');
        }
    };

    return (
        <div className="p-4 space-y-4">
            <SearchBar onSearch={handleSearch}/>

            {error && <div className="text-red-600">{error}</div>}

            <MediaGrid results={mediaResults}/>

            {searchResponse && (searchResponse.nextPage || searchResponse.previousPage) && (
                <div className="flex items-center justify-center space-x-4 mt-4">
                    <button
                        onClick={handlePrev}
                        disabled={!searchResponse.previousPage}
                        className="px-4 py-2 bg-gray-200 rounded disabled:opacity-50"
                    >
                        Previous
                    </button>
                    <button
                        onClick={handleNext}
                        disabled={!searchResponse.nextPage}
                        className="px-4 py-2 bg-gray-200 rounded disabled:opacity-50"
                    >
                        Next
                    </button>
                </div>
            )}
        </div>
    );
}
