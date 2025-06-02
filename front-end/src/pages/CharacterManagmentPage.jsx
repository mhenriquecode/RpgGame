import React, { useState, useEffect, useCallback } from 'react';
import CharacterCreateForm from '../components/CharacterCreateForm';
import CharacterList from '../components/CharacterList';
import { getAllCharacters } from '../services/characterApiService';
import { useAuth } from '../contexts/AuthContext'; 
import { Link } from 'react-router-dom';

function CharacterManagementPage() {
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
                console.warn("Sessão expirada ou token inválido. Deslogando...");
                logout();
            }
        } finally {
            setIsLoadingCharacters(false);
        }
    }, [logout]);

    useEffect(() => {
        fetchCharacters();
    }, [fetchCharacters]);

    const handleCharacterChange = () => { 
        fetchCharacters();
    };

    return (
        <div>
            <Link to="/" className="back-home-button">
                Voltar para a Tela Inicial
            </Link>
            <h1>Gerenciar Personagens</h1>
            <CharacterCreateForm onCharacterCreated={handleCharacterChange} />
            <hr className="section-divider" />
            <CharacterList
                characters={characters}
                isLoading={isLoadingCharacters}
                error={characterListError}
                onCharacterDeleted={handleCharacterChange}
            />
        </div>
    );
}

export default CharacterManagementPage;