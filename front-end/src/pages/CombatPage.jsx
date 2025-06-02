import React from 'react';
import { Link } from 'react-router-dom';

function CombatPage() {
    return (
        <div className="combat-page-container">
            <h2>Arena de Combate ⚔️</h2>
            <br />
            <Link to="/" className="back-home-button">
                Voltar para a Tela Inicial
            </Link>
        </div>
    );
}

export default CombatPage;