// src/components/Media/SearchBar.jsx
import { useState, useRef } from 'react';
import { useAuth } from '../../contexts/AuthContext';
import axios from 'axios';

export default function SearchBar({ onSearch }) {
    const { user } = useAuth();

    const [query, setQuery] = useState('');
    const [mediaType, setMediaType] = useState('');
    const [license, setLicense] = useState('');
    const [source, setSource] = useState('');

    const [recentSearches, setRecentSearches] = useState([]);
    const [showSuggestions, setShowSuggestions] = useState(false);
    const [loadingHistory, setLoadingHistory] = useState(false);

    const containerRef = useRef(null);

    // 1) Called when the user focuses the search input
    async function handleFocus() {
        if (!user?.username) return;
        setShowSuggestions(true); // open the dropdown
        setLoadingHistory(true);

        try {
            // fetch user's entire search history
            const res = await axios.get(`/api/history/user/${user.username}`);
            // keep only last 5 or so. Adjust to your preference
            const allHistory = res.data || [];
            const lastFive = allHistory.slice(-5).reverse();
            setRecentSearches(lastFive);
        } catch (err) {
            console.error('Failed to fetch recent searches:', err);
        } finally {
            setLoadingHistory(false);
        }
    }

    // 2) Hide suggestions if user clicks outside the search container
    //    Typically you'd attach an onBlur, but it's tricky due to clicks.
    //    This approach is simpler if you add a "click outside" listener.
    function handleBlur() {
        // small timeout to let click events register
        setTimeout(() => {
            setShowSuggestions(false);
        }, 200);
    }

    // 3) Once a user clicks a suggestion, fill the input
    function handleSelectSuggestion(suggestion) {
        setQuery(suggestion);
    }

    // 4) Submitting the form triggers your existing onSearch callback
    function handleSubmit(e) {
        e.preventDefault();
        onSearch({ query, mediaType, license, source });
        setShowSuggestions(false);
    }

    return (
        <div className="relative" ref={containerRef}>
            <form onSubmit={handleSubmit} className="flex gap-2">
                {/* Query input */}
                <input
                    type="text"
                    value={query}
                    onChange={(e) => setQuery(e.target.value)}
                    onFocus={handleFocus}
                    onBlur={handleBlur}
                    placeholder="Search open media..."
                    className="flex-grow border px-4 py-2 rounded"
                    aria-label="Search media"
                />

                {/* MediaType filter */}
                <select
                    className="border px-2 py-2 rounded"
                    value={mediaType}
                    onChange={(e) => setMediaType(e.target.value)}
                >
                    <option value="">Images</option>
                    <option value="audio">Audio</option>
                </select>

                {/* License filter */}
                <select
                    className="border px-2 py-2 rounded"
                    value={license}
                    onChange={(e) => setLicense(e.target.value)}
                >
                    <option value="">All Licenses</option>
                    <option value="cc0">CC0</option>
                    <option value="by">BY</option>
                    <option value="by-sa">BY-SA</option>
                </select>

                {/* Source filter */}
                <select
                    className="border px-2 py-2 rounded"
                    value={source}
                    onChange={(e) => setSource(e.target.value)}
                >
                    <option value="">All Sources</option>
                    <option value="flickr">Flickr</option>
                    <option value="wikimedia">Wikimedia</option>
                    {/* add more as needed */}
                </select>

                <button type="submit" className="btn-primary">
                    Search
                </button>
            </form>

            {/* 5) The dropdown of recent searches */}
            {showSuggestions && recentSearches.length > 0 && (
                <ul className="absolute bg-white border rounded shadow mt-1 w-full z-10 max-h-40 overflow-auto">
                    {loadingHistory ? (
                        <li className="px-4 py-2 text-gray-500">Loading...</li>
                    ) : (
                        recentSearches.map(item => (
                            <li
                                key={item.id}
                                className="px-4 py-2 hover:bg-gray-100 cursor-pointer"
                                onMouseDown={() => handleSelectSuggestion(item.searchQuery)}
                            >
                                {item.searchQuery}
                            </li>
                        ))
                    )}
                </ul>
            )}
        </div>
    );
}
