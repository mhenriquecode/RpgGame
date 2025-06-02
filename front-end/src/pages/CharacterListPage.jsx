import React, { useState, useEffect, useCallback } from 'react';
import { Link } from 'react-router-dom';
import CharacterList from '../components/CharacterList'; 
import { getAllCharacters } from '../services/characterApiService';
import { useAuth } from '../contexts/AuthContext';
import '../styles/CharacterListPage.css';

function CharacterListPage() {
    const { logout } = useAuth();
    const [characters, setCharacters] = useState([]);
    const [isLoadingCharacters, setIsLoadingCharacters] = useState(false);
    const [characterListError, setCharacterListError] = useState(null);

    const fetchCharacters = useCallback(async () => {
        setIsLoadingCharacters(true);
        setCharacterListError(null);
        try {
            const data = await getAllCharacters();
            setCharacters(data);
        } catch (error) {
            const errorMessage = error.message || 'Falha ao buscar personagens.';
            setCharacterListError(errorMessage);
            if (error.status === 401 || error.status === 403 || (error.response && (error.response.status === 401 || error.response.status === 403))) {
                logout();
            }
        } finally {
            setIsLoadingCharacters(false);
        }
    }, [logout]);

    useEffect(() => {
        fetchCharacters();
    }, [fetchCharacters]);

    const handleCharacterDeleted = () => {
        fetchCharacters();
    };

    return (
        <div className="character-list-page-container">
            <div className="page-header">
                <h2>Meus Personagens Criados</h2>
                <Link to="/personagens/criar" className="nav-button create-new-char-button">
                    + Criar Novo Personagem
                </Link>
            </div>

            {isLoadingCharacters && <p className="loading-message">Carregando personagens...</p>}
            {characterListError && <p className="error-message">{characterListError}</p>}
            
            {!isLoadingCharacters && !characterListError && characters.length === 0 && (
                <p className="info-message">
                    Você ainda não criou nenhum personagem. 
                    <Link to="/personagens/criar" className="link-styled"> Comece criando um agora!</Link>
                </p>
            )}

            {!isLoadingCharacters && !characterListError && characters.length > 0 && (
                <CharacterList
                    characters={characters}
                    isLoading={false} 
                    error={null}    
                    onCharacterDeleted={handleCharacterDeleted}
                />
            )}
            <div className="page-footer-nav">
                <Link to="/" className="nav-button secondary">Voltar para Home</Link>
            </div>
        </div>
    );
}

export default CharacterListPage;