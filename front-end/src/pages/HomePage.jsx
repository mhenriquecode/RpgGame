import React from 'react';
import { Link } from 'react-router-dom';

function HomePage() {
    return (
        <div className="home-page-container">
            <h2>Bem-vindo ao RPG Battle Simulator!</h2>
            <p>Escolha uma opção para continuar:</p>
            <div className="home-navigation-buttons">
                <Link to="/personagens" className="home-button characters-button">
                    Gerenciar Personagens
                </Link>
                <Link to="/combate" className="home-button combat-button">
                    Iniciar Combate
                </Link>
            </div>
        </div>
    );
}

export default HomePage;