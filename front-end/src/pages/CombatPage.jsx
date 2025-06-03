import React, { useState, useEffect, useCallback } from 'react';
import { Link } from 'react-router-dom';
import { getAllCharacters } from '../services/characterApiService';
import { startFullCombat } from '../services/combatApiService';
import { useAuth } from '../contexts/AuthContext';


const playerActions = [
    { value: 1, label: 'Atacar' }
];

function CombatPage() {
    const { logout } = useAuth();
    const [characters, setCharacters] = useState([]);
    const [isLoadingCharacters, setIsLoadingCharacters] = useState(false);
    const [error, setError] = useState(null);

    const [selectedPlayer1Id, setSelectedPlayer1Id] = useState('');
    const [strategyPlayer1, setStrategyPlayer1] = useState(playerActions[0]?.value || 1);
    const [selectedPlayer2Id, setSelectedPlayer2Id] = useState('');
    const [strategyPlayer2, setStrategyPlayer2] = useState(playerActions[0]?.value || 1);

    const [isStartingCombat, setIsStartingCombat] = useState(false); // Para o loading do botão de iniciar
    const [combatData, setCombatData] = useState(null); // Armazenará o CombatResultDTO completo

    const [displayedLogLines, setDisplayedLogLines] = useState([]); // Linhas de log a serem exibidas na tela
    const [currentLogLineIndex, setCurrentLogLineIndex] = useState(0); // Índice para o "replay"
    const [isReplaying, setIsReplaying] = useState(false); // Controla se o replay está ativo

    const fetchCharactersForCombat = useCallback(async () => {
        setIsLoadingCharacters(true);
        setError(null);
        try {
            const data = await getAllCharacters();
            setCharacters(data);
            if (data.length > 0) {
                setSelectedPlayer1Id(data[0].id);
                if (data.length > 1) {
                    setSelectedPlayer2Id(data[1].id);
                } else {
                    setSelectedPlayer2Id('');
                }
            }
        } catch (err) {
            setError(err.message || 'Falha ao buscar personagens para o combate.');
            if (err.response?.status === 401 || err.response?.status === 403) {
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
        setCombatData(null);
        setDisplayedLogLines([]);
        setCurrentLogLineIndex(0);
        setIsReplaying(false);

        if (!selectedPlayer1Id || !selectedPlayer2Id) {
            setError("Selecione os dois personagens para o combate.");
            return;
        }
        if (selectedPlayer1Id === selectedPlayer2Id && characters.length > 1) {
            setError("Os personagens selecionados para o combate devem ser diferentes.");
            return;
        }

        const combatRequest = {
            player1: selectedPlayer1Id,
            strategy1: parseInt(strategyPlayer1, 10),
            player2: selectedPlayer2Id,
            strategy2: parseInt(strategyPlayer2, 10),
        };

        setIsStartingCombat(true);
        try {
            const result = await startFullCombat(combatRequest); // result é CombatResultDTO
            setCombatData(result);
            if (result && result.turnLogs && Array.isArray(result.turnLogs) && result.turnLogs.length > 0) {
                setIsReplaying(true); // Inicia o processo de replay
            } else {
                setError(result ? 'O combate não retornou logs de turno para exibir.' : 'O combate não retornou um resultado válido.');
            }
        } catch (err) {
            setError(err.message || 'Ocorreu um erro ao processar o combate.');
        } finally {
            setIsStartingCombat(false);
        }
    };

    useEffect(() => {
        if (isReplaying && combatData && combatData.turnLogs && currentLogLineIndex < combatData.turnLogs.length) {
            const timer = setTimeout(() => {
                const currentTurnLog = combatData.turnLogs[currentLogLineIndex];
                if (currentTurnLog && typeof currentTurnLog.actionDescription === 'string') {
                    setDisplayedLogLines(prevLogs => [...prevLogs, currentTurnLog.actionDescription]);
                } else {
                    console.warn("Log de turno inválido ou actionDescription ausente:", currentTurnLog);
                    setDisplayedLogLines(prevLogs => [...prevLogs, "--- Erro ao processar log do turno ---"]);
                }
                setCurrentLogLineIndex(prevIndex => prevIndex + 1);
            }, 1200); 
            return () => clearTimeout(timer);
        } else if (isReplaying && combatData && combatData.turnLogs && currentLogLineIndex >= combatData.turnLogs.length) {
            setIsReplaying(false);
        }
    }, [isReplaying, combatData, currentLogLineIndex]);

    const getCharacterDetailsById = (id) => characters.find(char => char.id === id);
    const player1Details = selectedPlayer1Id ? getCharacterDetailsById(selectedPlayer1Id) : null;
    const player2Details = selectedPlayer2Id ? getCharacterDetailsById(selectedPlayer2Id) : null;
    const availablePlayer2Options = characters.filter(char => char.id !== selectedPlayer1Id);

    const renderCharacterAttributes = (character, playerLabel) => {
        if (!character) {
            return (
                <div className="player-attributes-card placeholder">
                    <p>Selecione o {playerLabel}</p>
                </div>
            );
        }
        return (
            <div className="player-attributes-card">
                <h3>{playerLabel}: {character.name}</h3>
                <p className="char-subtitle-combat">{character.race} {character.classType}</p>
                <div className="char-main-stats-combat">
                    <span><strong>HP:</strong> {character.maxHealth ?? 'N/A'}</span>
                    <span><strong>Arma:</strong> {character.weapon ?? 'N/A'}</span>
                </div>
                <div className="char-attributes-grid-combat">
                    <span><strong>For:</strong> {character.strength ?? 'N/A'}</span>
                    <span><strong>Def:</strong> {character.defense ?? 'N/A'}</span>
                    <span><strong>Vel:</strong> {character.speed ?? 'N/A'}</span>
                    <span><strong>Arm:</strong> {character.armor ?? 'N/A'}</span>
                </div>
            </div>
        );
    };

    return (
        <div className="combat-page-container">
            <h2>Arena de Combate ⚔️</h2>
            {!isStartingCombat && !isReplaying && (
                <>
                    <div className="selected-combatants-attributes-display">
                        {renderCharacterAttributes(player1Details, "Jogador 1")}
                        <div className="attributes-vs-separator">VS</div>
                        {renderCharacterAttributes(player2Details, "Jogador 2")}
                    </div>

                    {isLoadingCharacters && <p className="loading-message">Carregando personagens...</p>}
                    
                    {!isLoadingCharacters && characters.length < 2 && !error && (
                        <p className="info-message">
                            Você precisa de pelo menos dois personagens criados para iniciar um combate.
                            <Link to="/personagens/lista" className="link-styled"> Crie mais personagens aqui.</Link>
                        </p>
                    )}

                    {characters.length >= 1 && !isLoadingCharacters && (
                        <form onSubmit={handleStartCombat} className="combat-setup-form">
                            <div className="combatants-selection-dropdowns">
                                <div className="combatant-selector">
                                    <label htmlFor="player1">Selecionar Jogador 1:</label>
                                    <select id="player1" value={selectedPlayer1Id} 
                                            onChange={(e) => { setSelectedPlayer1Id(e.target.value); setCombatData(null); setError(null); setDisplayedLogLines([]); setIsReplaying(false);}} 
                                            disabled={isLoadingCharacters || isStartingCombat}>
                                        <option value="" disabled>Escolha...</option>
                                        {characters.map(char => (<option key={char.id} value={char.id}>{char.name} ({char.classType})</option>))}
                                    </select>
                                    <label htmlFor="strategy1">Estratégia P1:</label>
                                    <select id="strategy1" value={strategyPlayer1} onChange={(e) => setStrategyPlayer1(e.target.value)} disabled={isLoadingCharacters || isStartingCombat}>
                                        {playerActions.map(action => (<option key={action.value} value={action.value}>{action.label}</option>))}
                                    </select>
                                </div>
                                <div className="combatant-selector">
                                    <label htmlFor="player2">Selecionar Jogador 2:</label>
                                    <select id="player2" value={selectedPlayer2Id} 
                                            onChange={(e) => { setSelectedPlayer2Id(e.target.value); setCombatData(null); setError(null); setDisplayedLogLines([]); setIsReplaying(false);}} 
                                            disabled={isLoadingCharacters || isStartingCombat || (characters.length > 1 && !selectedPlayer1Id) || availablePlayer2Options.length === 0}>
                                        <option value="" disabled>Escolha...</option>
                                        {availablePlayer2Options.map(char => (<option key={char.id} value={char.id}>{char.name} ({char.classType})</option>))}
                                        {characters.length === 1 && selectedPlayer1Id && (<option value="" disabled>Crie outro personagem</option>)}
                                    </select>
                                    <label htmlFor="strategy2">Estratégia P2:</label>
                                    <select id="strategy2" value={strategyPlayer2} onChange={(e) => setStrategyPlayer2(e.target.value)} disabled={isLoadingCharacters || isStartingCombat}>
                                        {playerActions.map(action => (<option key={action.value} value={action.value}>{action.label}</option>))}
                                    </select>
                                </div>
                            </div>
                            <button type="submit" className="start-combat-button" 
                                    disabled={isStartingCombat || isLoadingCharacters || characters.length < 2 || !selectedPlayer1Id || !selectedPlayer2Id || (selectedPlayer1Id === selectedPlayer2Id && characters.length > 1)}>
                                {isStartingCombat ? 'Simulando...' : 'Iniciar Combate!'}
                            </button>
                        </form>
                    )}
                </>
            )}
            
            {isStartingCombat && <p className="loading-message">Preparando combate...</p> }

            {displayedLogLines.length > 0 && (
                <div className="combat-live-log-display">
                    <h3>Andamento do Combate:</h3>
                    {displayedLogLines.map((logLine, index) => (
                        <div key={index} className="turn-log-message">
                            {logLine.split('\n').map((line, i) => (
                                <React.Fragment key={i}>{line}{i < logLine.split('\n').length -1 && <br/>}</React.Fragment>
                            ))}
                        </div>
                    ))}
                </div>
            )}

            {error && <p className="error-message combat-error">{error}</p>}

            {combatData && !isReplaying && ( 
                <div className="combat-result">
                    <h3>Resultado Final do Combate</h3>
                    <p>🎉 O vencedor é: <strong>{combatData.winnerName}</strong> 🎉</p>
                </div>
            )}
            
            {!isReplaying && !isStartingCombat && (
                 <div className="combat-page-navigation">
                    <Link to="/historico-combates" className="nav-button history-button">
                        Ver Histórico de Combates
                    </Link>
                    <Link to="/" className="nav-button secondary back-home-button">
                        Voltar para a Tela Inicial
                    </Link>
                </div>
            )}
        </div>
    );
}

export default CombatPage;