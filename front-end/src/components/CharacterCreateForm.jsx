import React, { useState } from 'react';
import { races, classTypes, weapons } from '../data/rpgOptions';
import { createCharacter } from '../services/characterApiService';

function CharacterCreateForm({ onCharacterCreated }) {
    const [name, setName] = useState('');
    const [selectedRace, setSelectedRace] = useState(races[0]?.value || '');
    const [selectedClassType, setSelectedClassType] = useState(classTypes[0]?.value || '');
    const [selectedWeapon, setSelectedWeapon] = useState(weapons[0]?.value || '');
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);
    const [successMessage, setSuccessMessage] = useState('');

    const handleSubmit = async (event) => {
        event.preventDefault();
        setError(null);
        setSuccessMessage('');
        setIsLoading(true);

        if (!name.trim()) {
            setError('O nome é obrigatório.');
            setIsLoading(false);
            return;
        }
        if (!selectedRace || !selectedClassType || !selectedWeapon) {
            setError('Todos os campos de seleção são obrigatórios.');
            setIsLoading(false);
            return;
        }

        const characterData = {
            name,
            race: selectedRace,
            classType: selectedClassType,
            weapon: selectedWeapon,
        };

        try {
            const newCharacter = await createCharacter(characterData);
            setSuccessMessage(`Personagem "${newCharacter.name}" criado com sucesso!`);
            setName(''); // Limpa o campo nome
            // Opcionalmente, resete os selects para o valor padrão ou deixe como está
            if (onCharacterCreated) {
                onCharacterCreated(newCharacter); // Chama a função de callback passada como prop
            }
        } catch (err) {
            setError(err.response?.data?.message || err.message || 'Falha ao criar personagem. Tente novamente.');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="character-form-container">
            <h2>Criar Novo Personagem 🧙‍♂️</h2>
            {error && <p className="error-message">{error}</p>}
            {successMessage && <p className="success-message">{successMessage}</p>}
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="name">Nome:</label>
                    <input
                        type="text"
                        id="name"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        disabled={isLoading}
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="race">Raça:</label>
                    <select
                        id="race"
                        value={selectedRace}
                        onChange={(e) => setSelectedRace(e.target.value)}
                        disabled={isLoading}
                    >
                        {races.map((race) => (
                            <option key={race.value} value={race.value}>
                                {race.label}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="form-group">
                    <label htmlFor="classType">Classe:</label>
                    <select
                        id="classType"
                        value={selectedClassType}
                        onChange={(e) => setSelectedClassType(e.target.value)}
                        disabled={isLoading}
                    >
                        {classTypes.map((classType) => (
                            <option key={classType.value} value={classType.value}>
                                {classType.label}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="form-group">
                    <label htmlFor="weapon">Arma:</label>
                    <select
                        id="weapon"
                        value={selectedWeapon}
                        onChange={(e) => setSelectedWeapon(e.target.value)}
                        disabled={isLoading}
                    >
                        {weapons.map((weapon) => (
                            <option key={weapon.value} value={weapon.value}>
                                {weapon.label}
                            </option>
                        ))}
                    </select>
                </div>

                <button type="submit" disabled={isLoading}>
                    {isLoading ? 'Criando...' : 'Criar Personagem'}
                </button>
            </form>
        </div>
    );
}

export default CharacterCreateForm;