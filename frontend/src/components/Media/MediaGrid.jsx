import PropTypes from 'prop-types';
import MediaCard from './MediaCard';

const MediaGrid = ({results}) => {
    return (
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
            {results?.map((item) => (
                <MediaCard key={item.id} media={item}/>
            ))}
        </div>
    );
};

MediaGrid.propTypes = {
    results: PropTypes.array.isRequired
};

export default MediaGrid;