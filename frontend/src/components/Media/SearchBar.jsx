// src/components/Media/SearchBar.jsx
import { useState } from 'react';

const SearchBar = ({ onSearch }) => {
    const [query, setQuery] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        onSearch(query);
    };

    return (
        <form onSubmit={handleSubmit} className="flex gap-2">
            <input
                type="text"
                value={query}
                onChange={(e) => setQuery(e.target.value)}
                placeholder="Search open media..."
                className="flex-grow border px-4 py-2 rounded"
                aria-label="Search media"
            />
            <button type="submit" className="btn-primary">
                Search
            </button>
        </form>
    );
};

export default SearchBar;