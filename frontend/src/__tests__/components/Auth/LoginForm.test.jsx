import { render, screen, fireEvent } from '@testing-library/react';
import LoginForm from '../../../components/Auth/LoginForm';
import { AuthProvider } from '../../../contexts/AuthContext';

test('submits login form with valid credentials', async () => {
    render(
        <AuthProvider>
            <LoginForm />
        </AuthProvider>
    );

    fireEvent.change(screen.getByLabelText(/username/i), { target: { value: 'testuser' } });
    fireEvent.change(screen.getByLabelText(/password/i), { target: { value: 'Test123!' } });
    fireEvent.click(screen.getByRole('button', { name: /log in/i }));

});