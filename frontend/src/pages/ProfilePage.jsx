import { useEffect, useState } from 'react';
import axios from 'axios';
import { useAuth } from '../contexts/AuthContext';

export default function ProfilePage() {
    const { user } = useAuth();
    const [history, setHistory] = useState([]);
    const [error, setError] = useState('');

    useEffect(() => {
        if (!user?.username) return;

        axios.get(`/api/history/user/${user.username}`)
            .then((res) => {
                setHistory(res.data);
            })
            .catch(() => setError('Failed to load history'));
    }, [user]);

    // DELETE request to remove a specific history item
    async function handleDelete(historyId) {
        try {
            await axios.delete(`/api/history/delete/${historyId}`);
            // Filter out the deleted item
            setHistory(prev => prev.filter(entry => entry.id !== historyId));
        } catch (err) {
            console.error("Error deleting search history:", err);
            setError('Failed to delete item');
        }
    }

    if (!user) {
        return (
            <div className="max-w-md mx-auto bg-white p-8 rounded shadow mt-10">
                <p className="text-red-600 font-semibold">Please login first.</p>
            </div>
        );
    }

    return (
        <div className="max-w-md mx-auto bg-white p-8 rounded shadow mt-10">
            <h2 className="text-2xl font-bold text-gray-800 mb-6">
                Search History for {user.username}
            </h2>

            {error && (
                <div className="text-red-600 mb-4">
                    {error}
                </div>
            )}

            {history.length === 0 ? (
                <p className="text-gray-700">No search history found</p>
            ) : (
                <ul className="space-y-4">
                    {history.map((entry) => (
                        <li key={entry.id} className="flex justify-between items-center">
                            <div>
                                <p className="font-medium text-gray-800">
                                    {entry.searchQuery}
                                    {entry.timestamp && (
                                        <span className="ml-2 text-sm text-gray-500">
                                            on {new Date(entry.timestamp).toLocaleString()}
                                        </span>
                                    )}
                                </p>
                            </div>
                            <button
                                onClick={() => handleDelete(entry.id)}
                                className="bg-red-600 hover:bg-red-700 text-white text-sm px-3 py-1.5 rounded"
                            >
                                Delete
                            </button>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}
