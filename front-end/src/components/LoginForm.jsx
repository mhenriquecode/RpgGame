import React, { useState } from 'react';
import { useAuth } from '../contexts/AuthContext';

function LoginForm({ onSwitchToRegister }) {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const { login, isLoading, authError, setAuthError } = useAuth();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setAuthError(null); 
        if (!email || !password) {
            setAuthError("Email e senha são obrigatórios.");
            return;
        }
        await login({ email, password });
    };

    return (
        <div className="auth-form-container">
            <h2>Login 🔑</h2>
            {authError && <p className="error-message">{authError}</p>}
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="login-email">Email:</label>
                    <input
                        type="email"
                        id="login-email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        disabled={isLoading}
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="login-password">Senha:</label>
                    <input
                        type="password"
                        id="login-password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        disabled={isLoading}
                        required
                    />
                </div>
                <button type="submit" disabled={isLoading}>
                    {isLoading ? 'Entrando...' : 'Entrar'}
                </button>
            </form>
            <p className="switch-form-text">
                Não tem uma conta?{' '}
                <button type="button" onClick={onSwitchToRegister} className="link-button">
                    Registre-se aqui
                </button>
            </p>
        </div>
    );
}

export default LoginForm;