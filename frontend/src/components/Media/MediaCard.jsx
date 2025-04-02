export default function MediaCard({ media }) {
    if (media.mediaType === 'audio') {
        return (
            <div className="rounded shadow p-4 bg-white">
                <h3 className="font-semibold">{media.title}</h3>
                <audio controls src={media.url} className="w-full mt-2">
                    Your browser does not support the audio element.
                </audio>
                <p className="text-sm text-gray-600">License: {media.license}</p>
                <a href={media.sourceUrl} target="_blank" rel="noopener noreferrer" className="text-blue-600 underline text-sm">
                    Source
                </a>
            </div>
        );
    }

    // default to image
    return (
        <div className="rounded shadow p-4 bg-white">
            <img src={media.url} alt={media.title} className="mb-2 w-full h-48 object-cover" />
            <h3 className="font-semibold">{media.title}</h3>
            <p className="text-sm text-gray-600">License: {media.license}</p>
            <a href={media.sourceUrl} target="_blank" rel="noopener noreferrer" className="text-blue-600 underline text-sm">
                Source
            </a>
        </div>
    );
}
