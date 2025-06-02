import React, { useState, useEffect, useCallback } from 'react';
import { Link } from 'react-router-dom';
import { getAllCharacters } from '../services/characterApiService';
import { startFullCombat } from '../services/combatApiService'; 
import { useAuth } from '../contexts/AuthContext';
import '../styles/CombatPage.css';

const playerActions = [
    { value: 1, label: 'Atacar' }
];

function CombatPage() {
    const { logout } = useAuth();
    const [characters, setCharacters] = useState([]);
    const [isLoadingCharacters, setIsLoadingCharacters] = useState(false);
    const [error, setError] = useState(null);

    const [selectedPlayer1, setSelectedPlayer1] = useState('');
    const [strategyPlayer1, setStrategyPlayer1] = useState(playerActions[0].value); 
    const [selectedPlayer2, setSelectedPlayer2] = useState('');
    const [strategyPlayer2, setStrategyPlayer2] = useState(playerActions[0].value); 

    const [isStartingCombat, setIsStartingCombat] = useState(false);
    const [combatResult, setCombatResult] = useState(null); 

    const fetchCharactersForCombat = useCallback(async () => {
        setIsLoadingCharacters(true);
        setError(null);
        try {
            const data = await getAllCharacters();
            setCharacters(data);
            if (data.length > 0) {
                setSelectedPlayer1(data[0].id);
                if (data.length > 1) {
                    setSelectedPlayer2(data[1].id);
                } else {
                    setSelectedPlayer2(data[0].id); 
                }
            }
        } catch (err) {
            setError(err.message || 'Falha ao buscar personagens para o combate.');
            if (err.status === 401 || err.status === 403 || (err.response && (err.response.status === 401 || err.response.status === 403))) {
                logout();
            }
        } finally {
            setIsLoadingCharacters(false);
        }
    }, [logout]);

    useEffect(() => {
        fetchCharactersForCombat();
    }, [fetchCharactersForCombat]);

    const handleStartCombat = async (event) => {
        event.preventDefault();
        setError(null);
        setCombatResult(null);

        if (!selectedPlayer1 || !selectedPlayer2) {
            setError("Selecione os dois personagens para o combate.");
            return;
        }
        if (selectedPlayer1 === selectedPlayer2) {
            setError("Os personagens selecionados para o combate devem ser diferentes.");
            return;
        }

        const combatRequest = {
            player1: selectedPlayer1,
            strategy1: parseInt(strategyPlayer1, 10),
            player2: selectedPlayer2,
            strategy2: parseInt(strategyPlayer2, 10),
        };

        setIsStartingCombat(true);
        try {
            const result = await startFullCombat(combatRequest);
            setCombatResult(result); 
        } catch (err) {
            setError(err.message || 'Ocorreu um erro ao processar o combate.');
        } finally {
            setIsStartingCombat(false);
        }
    };

    const availablePlayer2Options = characters.filter(char => char.id !== selectedPlayer1);

    return (
        <div className="combat-page-container">
            <h2>Arena de Combate ⚔️</h2>

            {isLoadingCharacters && <p className="loading-message">Carregando personagens...</p>}
            
            {!isLoadingCharacters && characters.length < 2 && (
                 <p className="info-message">
                    Você precisa de pelo menos dois personagens criados para iniciar um combate.
                    <Link to="/personagens" className="link-styled"> Crie mais personagens aqui.</Link>
                </p>
            )}

            {characters.length >= 1 && ( 
                <form onSubmit={handleStartCombat} className="combat-setup-form">
                    <div className="combatants-selection">
                        <div className="combatant-selector">
                            <label htmlFor="player1">Jogador 1:</label>
                            <select
                                id="player1"
                                value={selectedPlayer1}
                                onChange={(e) => setSelectedPlayer1(e.target.value)}
                                disabled={isLoadingCharacters || isStartingCombat}
                            >
                                <option value="" disabled>Selecione um personagem</option>
                                {characters.map(char => (
                                    <option key={char.id} value={char.id}>{char.name} ({char.classType})</option>
                                ))}
                            </select>
                            <label htmlFor="strategy1">Estratégia Inicial P1:</label>
                            <select
                                id="strategy1"
                                value={strategyPlayer1}
                                onChange={(e) => setStrategyPlayer1(e.target.value)}
                                disabled={isLoadingCharacters || isStartingCombat}
                            >
                                {playerActions.map(action => (
                                    <option key={action.value} value={action.value}>{action.label}</option>
                                ))}
                            </select>
                        </div>

                        <div className="vs-separator">VS</div>

                        <div className="combatant-selector">
                            <label htmlFor="player2">Jogador 2:</label>
                            <select
                                id="player2"
                                value={selectedPlayer2}
                                onChange={(e) => setSelectedPlayer2(e.target.value)}
                                disabled={isLoadingCharacters || isStartingCombat || availablePlayer2Options.length === 0}
                            >
                                <option value="" disabled>Selecione um personagem</option>
                                {availablePlayer2Options.map(char => ( 
                                    <option key={char.id} value={char.id}>{char.name} ({char.classType})</option>
                                ))}
                                {characters.length === 1 && selectedPlayer1 && (
                                    <option value="" disabled>Crie outro personagem</option>
                                )}
                            </select>
                            <label htmlFor="strategy2">Estratégia Inicial P2:</label>
                            <select
                                id="strategy2"
                                value={strategyPlayer2}
                                onChange={(e) => setStrategyPlayer2(e.target.value)}
                                disabled={isLoadingCharacters || isStartingCombat}
                            >
                                {playerActions.map(action => (
                                    <option key={action.value} value={action.value}>{action.label}</option>
                                ))}
                            </select>
                        </div>
                    </div>

                    <button 
                        type="submit" 
                        className="start-combat-button" 
                        disabled={isStartingCombat || isLoadingCharacters || characters.length < 2 || !selectedPlayer1 || !selectedPlayer2}
                    >
                        {isStartingCombat ? 'Simulando Combate...' : 'Iniciar Combate!'}
                    </button>
                </form>
            )}

            {error && <p className="error-message combat-error">{error}</p>}

            {combatResult && (
                <div className="combat-result">
                    <h3>Resultado do Combate</h3>
                    <p>🎉 O vencedor é: <strong>{combatResult.winnerName}</strong> (ID: {combatResult.winnerId}) 🎉</p>
                </div>
            )}

            <div className="combat-page-navigation"> 
                <Link to="/historico-combates" className="nav-button history-button">
                    Ver Histórico de Combates
                </Link>
                <Link to="/" className="nav-button secondary back-home-button"> 
                    Voltar para a Tela Inicial
                </Link>
            </div>
        </div>
    );
}

export default CombatPage;