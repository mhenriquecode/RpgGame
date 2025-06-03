import React, { useState } from 'react';
import { deleteCharacter } from '../services/characterApiService'; 
import '../styles/CharacterList.css'; 

function CharacterList({ characters, isLoading, error, onCharacterDeleted }) {
    const [deletingId, setDeletingId] = useState(null);
    const [deleteError, setDeleteError] = useState(null);

    const handleDelete = async (characterId, characterName) => {
        if (!window.confirm(`Tem certeza que deseja deletar o personagem "${characterName}"?`)) {
            return;
        }

        setDeletingId(characterId);
        setDeleteError(null);
        try {
            await deleteCharacter(characterId);
            if (onCharacterDeleted) {
                onCharacterDeleted();
            }
        } catch (err) {
            setDeleteError(err.message || `Falha ao deletar ${characterName}.`);
        } finally {
            setDeletingId(null);
        }
    };

    if (isLoading) {
        return <p className="loading-message">Carregando personagens...</p>;
    }

    if (error) {
        return <p className="error-message">Erro ao carregar personagens: {error}</p>;
    }

    if (!characters || characters.length === 0) {
        return <p className="info-message">Nenhum personagem encontrado. Crie um novo!</p>;
    }

    return (
        <div className="character-list-container">
            <h3>Personagens Criados 📜</h3>
            {deleteError && <p className="error-message item-error-message">{deleteError}</p>}
            <ul>
                {characters.map((char) => (
                    <li key={char.id} className="character-item">
                        <div className="character-details">
                            <div className="character-main-info">
                                <strong>{char.name}</strong> ({char.race}, {char.classType}) - Arma: {char.weapon}
                            </div>
                            <div className="character-attributes">
                                <span className='character-hp'>HP: {char.maxHealth ?? 'N/A'}</span>
                                <span className='character-strenght'>Força: {char.strength ?? 'N/A'}</span>
                                <span className='character-defense'>Defesa: {char.defense ?? 'N/A'}</span>
                                <span className='character-speed'>Vel: {char.speed ?? 'N/A'}</span>
                                <span className='character-armor'>Arm: {char.armor ?? 'N/A'}</span>
                            </div>
                        </div>
                        <button
                            onClick={() => handleDelete(char.id, char.name)}
                            disabled={deletingId === char.id}
                            className="delete-button"
                        >
                            {deletingId === char.id ? 'Deletando...' : 'Deletar'}
                        </button>
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default CharacterList;