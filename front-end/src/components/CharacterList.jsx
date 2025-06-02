import React from 'react';

function CharacterList({ characters, isLoading, error }) {
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
            <ul>
                {characters.map((char) => (
                    <li key={char.id}>
                        <strong>{char.name}</strong> ({char.race}, {char.classType}) - Arma: {char.weapon}
                        {} {}
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default CharacterList;