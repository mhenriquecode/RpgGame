import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './contexts/AuthContext';
import LoginForm from './components/LoginForm';
import RegisterForm from './components/RegisterForm';
import HomePage from './pages/HomePage';
import CharacterCreatePage from './pages/CharacterCreatePage'; 
import CharacterListPage from './pages/CharacterListPage';   
import CombatPage from './pages/CombatPage';
import CombatHistoryPage from './pages/CombatHistoryPage';

function AppContent() {
    const { isAuthenticated, user, logout, isLoading: authIsLoading } = useAuth();
    const [showLogin, setShowLogin] = useState(true); 

    if (authIsLoading) {
        return <div className="loading-fullpage">Carregando Autenticação...</div>;
    }

    
    if (!isAuthenticated) {
        return (
            <div className="auth-page">
                {showLogin ? (
                    <LoginForm onSwitchToRegister={() => setShowLogin(false)} />
                ) : (
                    <RegisterForm onSwitchToLogin={() => setShowLogin(true)} />
                )}
            </div>
        );
    }

    return (
        <>
            <div className="user-session-controls">
                {user && <p className="welcome-message">Bem-vindo, {user.name || user.email}!</p>}
                <button onClick={logout} className="logout-button">Sair</button>
            </div>
            <Routes>
                <Route path="/" element={<HomePage />} />
                <Route path="/personagens/criar" element={<CharacterCreatePage />} />
                <Route path="/personagens/lista" element={<CharacterListPage />} />
                {/* Redireciona /personagens para a lista como padrão, se alguém usar o link antigo */}
                <Route path="/personagens" element={<Navigate to="/personagens/lista" replace />} />
                <Route path="/combate" element={<CombatPage />} />
                <Route path="/historico-combates" element={<CombatHistoryPage />} />
                <Route path="*" element={<Navigate to="/" replace />} />
            </Routes>
        </>
    );
}

function App() {
    return (
        <AuthProvider> 
            <Router>   
                <div className="app-container">
                    <header className="app-header">
                        <h1>⚔️ Simulador de Batalha RPG 🛡️</h1>
                    </header>
                    <main className="app-main">
                        <AppContent />
                    </main>
                    <footer className="app-footer">
                        <p>Projeto desenvolvido por Matheus Souza e Pedro Candido</p>
                    </footer>
                </div>
            </Router>
        </AuthProvider>
    );
}

export default App;