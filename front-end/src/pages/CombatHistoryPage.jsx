import React, { useState, useEffect, useCallback } from 'react';
import { Link } from 'react-router-dom';
import { getCombatHistory } from '../services/combatApiService';
import { useAuth } from '../contexts/AuthContext';

function CombatHistoryPage() {
    const { logout } = useAuth();
    const [history, setHistory] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);

    const fetchHistory = useCallback(async () => {
        setIsLoading(true);
        setError(null);
        try {
            const data = await getCombatHistory();
            data.sort((a, b) => new Date(b.timestamp) - new Date(a.timestamp));
            setHistory(data);
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

    const formatTimestamp = (timestamp) => {
        if (!timestamp) return 'Data indisponível';
        try {
            return new Date(timestamp).toLocaleString('pt-BR', {
                day: '2-digit',
                month: '2-digit',
                year: 'numeric',
                hour: '2-digit',
                minute: '2-digit',
            });
        } catch (e) {
            console.error("Erro ao formatar timestamp:", e);
            return timestamp; 
        }
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

            {history.length === 0 && !isLoading && (
                <p className="info-message">Nenhum combate registrado ainda.</p>
            )}

            {history.length > 0 && (
                <ul className="combat-log-list">
                    {history.map((log, index) => (
                        <li key={log.timestamp + '-' + index} className="combat-log-item"> 
                            <div className="log-timestamp">
                                <strong>Data:</strong> {formatTimestamp(log.timestamp)}
                            </div>
                            <div className="log-participants">
                                <div className="log-player">
                                    <strong>Jogador 1:</strong> {log.player1.name} ({log.player1.classType})
                                </div>
                                <div className="log-vs">vs</div>
                                <div className="log-player">
                                    <strong>Jogador 2:</strong> {log.player2.name} ({log.player2.classType})
                                </div>
                            </div>
                            <div className="log-winner">
                                🏆 <strong>Vencedor:</strong> {log.winner.name}
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