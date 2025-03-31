// src/__tests__/Auth.test.jsx
import { render, screen, fireEvent } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { AuthProvider } from '../contexts/AuthContext';
import App from '../App';

const queryClient = new QueryClient();

test('full authentication flow', async () => {
    render(
        <QueryClientProvider client={queryClient}>
            <AuthProvider>
                <App />
            </AuthProvider>
        </QueryClientProvider>
    );

    // Test registration
    fireEvent.change(screen.getByLabelText(/username/i), { target: { value: 'testuser' } });
    fireEvent.change(screen.getByLabelText(/password/i), { target: { value: 'Test123!' } });
    fireEvent.click(screen.getByRole('button', { name: /register/i }));

    // Add assertions for registration success
});