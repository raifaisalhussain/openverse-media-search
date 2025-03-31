// src/pages/ProfilePage.jsx
import { useEffect, useState } from 'react';
import api from '../utils/http';
import { useAuth } from '../contexts/AuthContext';

const ProfilePage = () => {
    const { user } = useAuth();
    const [searchHistory, setSearchHistory] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchHistory = async () => {
            try {
                // user.id must exist
                const response = await api.get(`/api/history/user/${user.id}`);
                setSearchHistory(response.data);
            } catch (error) {
                console.error('Failed to fetch history:', error);
            } finally {
                setLoading(false);
            }
        };

        if (user?.id) {
            fetchHistory();
        } else {
            setLoading(false);
        }
    }, [user]);

    if (loading) {
        return <div className="text-center">Loading history...</div>;
    }

    return (
        <div className="max-w-4xl mx-auto p-4">
            <h1 className="text-2xl font-bold mb-4">Search History</h1>
            {searchHistory.length === 0 ? (
                <div className="text-gray-500">No search history found</div>
            ) : (
                <div className="space-y-2">
                    {searchHistory.map((entry) => (
                        <div key={entry.id} className="bg-white p-3 rounded shadow">
                            <div className="flex justify-between items-center">
                                <span className="font-medium">{entry.searchQuery}</span>
                                <span className="text-sm text-gray-500">
                  {new Date(entry.timestamp).toLocaleString()}
                </span>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default ProfilePage;
