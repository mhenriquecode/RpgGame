import React, { useState } from 'react';
import { useAuth } from '../contexts/AuthContext';

function RegisterForm({ onSwitchToLogin }) {
    const [name, setName] = useState('');
    const [lastname, setLastname] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const { register, isLoading, authError, setAuthError } = useAuth();
    const [successMessage, setSuccessMessage] = useState('');


    const handleSubmit = async (e) => {
        e.preventDefault();
        setAuthError(null);
        setSuccessMessage('');

        if (!name || !lastname || !email || !password || !confirmPassword) {
            setAuthError("Todos os campos são obrigatórios.");
            return;
        }
        if (password !== confirmPassword) {
            setAuthError("As senhas não coincidem.");
            return;
        }

        const success = await register({ name, lastname, email, password });
        if (success) {
            setSuccessMessage("Registro bem-sucedido! Você já pode fazer login.");
            setName('');
            setLastname('');
            setEmail('');
            setPassword('');
            setConfirmPassword('');
        }
    };

    return (
        <div className="auth-form-container">
            <h2>Registro 📝</h2>
            {authError && <p className="error-message">{authError}</p>}
            {successMessage && <p className="success-message">{successMessage}</p>}
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="register-name">Nome:</label>
                    <input type="text" id="register-name" value={name} onChange={(e) => setName(e.target.value)} disabled={isLoading} required />
                </div>
                <div className="form-group">
                    <label htmlFor="register-lastname">Sobrenome:</label>
                    <input type="text" id="register-lastname" value={lastname} onChange={(e) => setLastname(e.target.value)} disabled={isLoading} required />
                </div>
                <div className="form-group">
                    <label htmlFor="register-email">Email:</label>
                    <input type="email" id="register-email" value={email} onChange={(e) => setEmail(e.target.value)} disabled={isLoading} required />
                </div>
                <div className="form-group">
                    <label htmlFor="register-password">Senha:</label>
                    <input type="password" id="register-password" value={password} onChange={(e) => setPassword(e.target.value)} disabled={isLoading} required />
                </div>
                <div className="form-group">
                    <label htmlFor="register-confirm-password">Confirmar Senha:</label>
                    <input type="password" id="register-confirm-password" value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} disabled={isLoading} required />
                </div>
                <button type="submit" disabled={isLoading}>
                    {isLoading ? 'Registrando...' : 'Registrar'}
                </button>
            </form>
            <p className="switch-form-text">
                Já tem uma conta?{' '}
                <button type="button" onClick={onSwitchToLogin} className="link-button">
                    Faça login aqui
                </button>
            </p>
        </div>
    );
}

export default RegisterForm;