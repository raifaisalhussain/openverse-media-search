import { render } from '@testing-library/react';
import MediaCard from '../../../components/Media/MediaCard';

const mockMedia = {
    title: 'Test Image',
    thumbnail: 'test.jpg',
    license: 'cc-by',
    creator: 'Test Creator',
    sourceUrl: 'https://example.com'
};

test('renders media card with correct information', () => {
    const { getByAltText, getByText } = render(<MediaCard media={mockMedia} />);

    expect(getByAltText('Test Image')).toBeInTheDocument();
    expect(getByText('CC-BY')).toBeInTheDocument();
    expect(getByText(/Test Creator/)).toBeInTheDocument();
});