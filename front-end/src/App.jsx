import React, { useState, useEffect, useCallback } from 'react';
import { AuthProvider, useAuth } from './contexts/AuthContext'; 
import LoginForm from './components/LoginForm';
import RegisterForm from './components/RegisterForm';
import CharacterCreateForm from './components/CharacterCreateForm';
import CharacterList from './components/CharacterList';
import { getAllCharacters } from './services/characterApiService';

function AppContent() {
    const { isAuthenticated, user, logout, isLoading: authIsLoading } = useAuth();
    const [showLogin, setShowLogin] = useState(true); 

    const [characters, setCharacters] = useState([]);
    const [isLoadingCharacters, setIsLoadingCharacters] = useState(false);
    const [characterListError, setCharacterListError] = useState(null);

    const fetchCharacters = useCallback(async () => {
        if (!isAuthenticated) return; 
        setIsLoadingCharacters(true);
        setCharacterListError(null);
        try {
            const data = await getAllCharacters();
            setCharacters(data);
        } catch (error) {
            setCharacterListError(error.message || 'Falha ao buscar personagens.');
            if (error.status === 401 || error.status === 403) { 
                logout(); 
            }
        } finally {
            setIsLoadingCharacters(false);
        }
    }, [isAuthenticated, logout]); 

    useEffect(() => {
        if (isAuthenticated) {
            fetchCharacters();
        } else {
            setCharacters([]); 
        }
    }, [isAuthenticated, fetchCharacters]);

    const handleCharacterCreated = () => {
        fetchCharacters(); 
    };

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
            <button onClick={logout} className="logout-button">Sair</button>
            {user && <p className="welcome-message">Bem-vindo, {user.name || user.email}!</p>}
            <CharacterCreateForm onCharacterCreated={handleCharacterCreated} />
            <hr className="section-divider" />
            <CharacterList
                characters={characters}
                isLoading={isLoadingCharacters}
                error={characterListError}
            />
        </>
    );
}

function App() {
    return (
        <AuthProvider> {}
            <div className="app-container">
                <header className="app-header">
                    <h1>⚔️ Simulador de Batalha RPG 🛡️</h1>
                </header>
                <main className="app-main">
                    <AppContent />
                </main>
                <footer className="app-footer">
                    <p>&copy; {new Date().getFullYear()} Seu Jogo de RPG. Todos os direitos reservados.</p>
                </footer>
            </div>
        </AuthProvider>
    );
}

export default App;