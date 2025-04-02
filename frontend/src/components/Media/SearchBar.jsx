import { useState } from 'react';

export default function SearchBar({ onSearch }) {
    const [query, setQuery] = useState('');
    const [mediaType, setMediaType] = useState('');
    const [license, setLicense] = useState('');
    const [source, setSource] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        onSearch({ query, mediaType, license, source });
    };

    return (
        <form onSubmit={handleSubmit} className="flex flex-col gap-2 md:flex-row md:items-center">
            <input
                type="text"
                value={query}
                onChange={(e) => setQuery(e.target.value)}
                placeholder="Search open media..."
                className="border px-4 py-2 rounded flex-grow"
                aria-label="Search media"
            />
            <select value={mediaType} onChange={(e) => setMediaType(e.target.value)} className="border px-2 py-2 rounded">
                <option value="">Images</option>
                <option value="audio">Audio</option>
            </select>
            <select value={license} onChange={(e) => setLicense(e.target.value)} className="border px-2 py-2 rounded">
                <option value="">All Licenses</option>
                <option value="cc0">CC0</option>
                <option value="by">BY</option>
                <option value="by-sa">BY-SA</option>
            </select>
            <select value={source} onChange={(e) => setSource(e.target.value)} className="border px-2 py-2 rounded">
                <option value="">All Sources</option>
                <option value="flickr">Flickr</option>
                <option value="wikimedia">Wikimedia</option>
                {/* more sources as needed */}
            </select>
            <button type="submit" className="btn-primary mt-2 md:mt-0 md:ml-2">Search</button>
        </form>
    );
}
