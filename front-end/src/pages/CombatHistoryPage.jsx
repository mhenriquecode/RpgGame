import React, { useState, useEffect, useCallback } from 'react';
import { Link } from 'react-router-dom';
import { getCombatHistory } from '../services/combatApiService';
import { useAuth } from '../contexts/AuthContext';

function CombatHistoryPage() {
    const { logout } = useAuth();
    const [originalHistory, setOriginalHistory] = useState([]);
    const [filteredHistory, setFilteredHistory] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);
    const [searchTerm, setSearchTerm] = useState('');

    const fetchHistory = useCallback(async () => {
        setIsLoading(true);
        setError(null);
        setSearchTerm('');
        try {
            const data = await getCombatHistory();
            setOriginalHistory(data);
        } catch (err) {
            setError(err.message || 'Falha ao carregar o histórico de combates.');
            if (err.status === 401 || err.status === 403 || (err.response && (err.response.status === 401 || err.response.status === 403))) {
                logout();
            }
        } finally {
            setIsLoading(false);
        }
    }, [logout]);

    useEffect(() => {
        fetchHistory();
    }, [fetchHistory]);

    useEffect(() => {
        if (!searchTerm.trim()) {
            setFilteredHistory(originalHistory);
        } else {
            const lowerCaseSearchTerm = searchTerm.toLowerCase();
            const filtered = originalHistory.filter(log =>
                log.player1Name?.toLowerCase().includes(lowerCaseSearchTerm) ||
                log.player2Name?.toLowerCase().includes(lowerCaseSearchTerm)
            );
            setFilteredHistory(filtered);
        }
    }, [searchTerm, originalHistory]);

    const formatTimestamp = (timestamp) => {
        if (!timestamp) return 'Data indisponível';
        try {
            return new Date(timestamp).toLocaleString('pt-BR', {
                day: '2-digit', month: '2-digit', year: 'numeric',
                hour: '2-digit', minute: '2-digit',
            });
        } catch (e) { return timestamp; }
    };

    if (isLoading) {
        return <div className="loading-message page-loading">Carregando histórico de combates...</div>;
    }
    if (error) {
        return <div className="error-message page-error">Erro: {error}</div>;
    }

    return (
        <div className="combat-history-page-container">
            <h2>Histórico de Combates 📜</h2>
            <div className="search-controls">
                <input
                    type="text"
                    placeholder="Buscar por nome do personagem..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    className="search-input-history"
                />
                {searchTerm && (
                    <button onClick={() => setSearchTerm('')} className="clear-search-button">
                        Limpar Busca
                    </button>
                )}
            </div>

            {originalHistory.length === 0 && !isLoading && (
                <p className="info-message">Nenhum combate registrado ainda.</p>
            )}
            {originalHistory.length > 0 && filteredHistory.length === 0 && !isLoading && searchTerm && (
                <p className="info-message">Nenhum combate encontrado para "{searchTerm}".</p>
            )}

            {filteredHistory.length > 0 && (
                <ul className="combat-log-list">
                    {filteredHistory.map((log) => (
                        <li key={log.logId} className="combat-log-item">
                            <div className="log-timestamp">
                                <strong>Data:</strong> {formatTimestamp(log.timestamp)}
                            </div>
                            <div className="log-participants">
                                <div className={`log-player ${log.winnerId === log.player1Id ? '' : 'defeated'}`}>
                                    <strong>Jogador 1:</strong> {log.player1Name} ({log.player1ClassType} {log.player1Race})
                                </div>
                                <div className="log-vs">vs</div>
                                <div className={`log-player ${log.winnerId === log.player2Id ? '' : 'defeated'}`}>
                                    <strong>Jogador 2:</strong> {log.player2Name} ({log.player2ClassType} {log.player2Race})
                                </div>
                            </div>
                            <div className="log-winner">
                                🏆 <strong>Vencedor:</strong> {log.winnerName} ({log.winnerClassType} {log.winnerRace})
                            </div>
                        </li>
                    ))}
                </ul>
            )}
            <div className="navigation-links-history">
                <Link to="/combate" className="nav-button">Iniciar Novo Combate</Link>
                <Link to="/" className="nav-button secondary">Voltar para Home</Link>
            </div>
        </div>
    );
}

export default CombatHistoryPage;