import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import CharacterCreateForm from '../components/CharacterCreateForm';
import '../styles/CharacterCreatePage.css';

function CharacterCreatePage() {
    const navigate = useNavigate();
    const [creationSuccessMessage, setCreationSuccessMessage] = useState('');

    const handleCharacterCreated = (newCharacter) => {
        setCreationSuccessMessage(`Personagem "${newCharacter.name}" criado com sucesso!`);
    };

    return (
        <div className="character-create-page-container">
            <Link to="/personagens/lista" className="nav-button view-chars-button">
                Ver Personagens Criados
            </Link>

            {creationSuccessMessage && (
                <div className="success-message-global">
                    {creationSuccessMessage}
                    <Link to="/personagens/lista" className="link-styled"> Ver lista.</Link>
                </div>
            )}

            <CharacterCreateForm onCharacterCreated={handleCharacterCreated} />
            
            <div className="page-footer-nav">
                 <Link to="/" className="nav-button secondary">Voltar para Home</Link>
            </div>
        </div>
    );
}

export default CharacterCreatePage;