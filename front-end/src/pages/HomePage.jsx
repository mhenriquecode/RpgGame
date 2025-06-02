import React from 'react';
import { Link } from 'react-router-dom';
import '../styles/HomePage.css'; 

function HomePage() {
    return (
        <div className="home-page-container">
            <h2>Bem-vindo ao RPG Battle Simulator!</h2>
            <p>Escolha uma opção para continuar:</p>
            <div className="home-navigation-buttons">
                <Link to="/personagens/criar" className="home-button characters-create-button">
                    Criar Novo Personagem
                </Link>
                <Link to="/personagens/lista" className="home-button characters-view-button">
                    Ver Personagens Criados
                </Link>
                <Link to="/combate" className="home-button combat-button">
                    Iniciar Combate
                </Link>
                <Link to="/historico-combates" className="home-button history-button">
                    Histórico de Combates
                </Link>
            </div>
        </div>
    );
}

export default HomePage;