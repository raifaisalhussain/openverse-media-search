import PropTypes from 'prop-types';

const MediaCard = ({ media }) => {
    if (!media || typeof media !== 'object') return null;

    const safeMedia = {
        id: media.id || '',
        title: media.title || 'Untitled',
        mediaType: media.mediaType || 'unknown',
        thumbnail: media.thumbnail || '',
        license: media.license || '',
        creator: media.creator || '',
        sourceUrl: media.sourceUrl || '#'
    };

    return (
        <div className="group relative rounded-lg shadow-md overflow-hidden bg-white hover:shadow-lg transition-shadow">
            <div className="aspect-square bg-gray-100">
                {safeMedia.thumbnail ? (
                    <img
                        src={safeMedia.thumbnail}
                        alt={safeMedia.title}
                        className="w-full h-full object-cover"
                        loading="lazy"
                    />
                ) : (
                    <div className="w-full h-full bg-gray-200 flex items-center justify-center">
                        <span className="text-gray-500">No preview available</span>
                    </div>
                )}
            </div>

            <div className="p-4">
                <div className="flex items-center justify-between mb-2">
          <span className="text-sm font-medium text-blue-600">
            {safeMedia.mediaType.toUpperCase()}
          </span>
                    {safeMedia.license && (
                        <span className="px-2 py-1 bg-blue-100 text-blue-800 text-sm rounded">
              {safeMedia.license.toUpperCase()}
            </span>
                    )}
                </div>

                <h3 className="font-semibold text-lg truncate">{safeMedia.title}</h3>

                {safeMedia.creator && (
                    <p className="text-sm text-gray-600 mt-1 truncate">
                        Creator: {safeMedia.creator}
                    </p>
                )}

                <a
                    href={safeMedia.sourceUrl}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="mt-2 inline-flex items-center text-blue-600 hover:text-blue-800 text-sm"
                >
                    View Source
                    <svg className="w-4 h-4 ml-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                              d="M10 6H6a2 2 0 00-2 2v10a2 2 0 002 2h10a2 2 0 002-2v-4M14 4h6m0 0v6m0-6L10 14" />
                    </svg>
                </a>
            </div>
        </div>
    );
};

MediaCard.propTypes = {
    media: PropTypes.shape({
        id: PropTypes.string,
        title: PropTypes.string,
        mediaType: PropTypes.string,
        thumbnail: PropTypes.string,
        license: PropTypes.string,
        creator: PropTypes.string,
        sourceUrl: PropTypes.string
    })
};

export default MediaCard;