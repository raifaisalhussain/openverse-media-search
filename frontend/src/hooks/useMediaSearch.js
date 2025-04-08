import {useQuery} from '@tanstack/react-query';
import api from '../utils/http';

const useMediaSearch = (query, filters) => {
    return useQuery({
        queryKey: ['search', query, filters],
        queryFn: async () => {
            try {
                const {data} = await api.get('/api/media/search', {
                    params: {
                        query,
                        mediaType: filters.mediaType || '',
                        license: filters.license || '',
                        source: filters.source || '',
                        page: 1
                    }
                });

                return {
                    results: data.media?.map(item => ({
                        id: item.id || '',
                        title: item.title || 'Untitled',
                        mediaType: item.mediaType || 'unknown',
                        thumbnail: item.thumbnail || '',
                        license: item.license || '',
                        creator: item.creator || '',
                        sourceUrl: item.sourceUrl || '#'
                    })) || [],
                    totalResults: data.totalResults || 0
                };
            } catch (error) {
                throw new Error(error.response?.data?.error || 'Search failed');
            }
        },
        enabled: !!query,
        retry: 1
    });
};

export default useMediaSearch;