import axios from 'axios';
import { getToken } from './authService'; // Importa getToken para pegar o token atual

const API_BASE_URL = 'http://localhost:8080/api/characters';

// Cria uma instância do Axios para os endpoints de personagens
const apiClient = axios.create({
    baseURL: API_BASE_URL,
});

// Interceptador para adicionar o token JWT a cada requisição
apiClient.interceptors.request.use(
    (config) => {
        const token = getToken();
        console.log('[Interceptor Request] Token pego de getToken():', token); // DEBUG
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        console.log('[Interceptor Request] Headers da Config:', config.headers); // DEBUG
        return config;
    },
    (error) => {
        console.error('[Interceptor Request] Erro:', error); // DEBUG
        return Promise.reject(error);
    }
);

/**
 * Cria um novo personagem.
 * @param {object} characterData - Dados do personagem { name, classType, race, weapon }.
 * @returns {Promise<object>} O personagem criado.
 */
export const createCharacter = async (characterData) => {
    try {
        const response = await apiClient.post('', characterData); // Rota relativa à baseURL
        return response.data;
    } catch (error) {
        console.error("Erro ao criar personagem:", error.response ? error.response.data : error.message);
        throw error.response?.data || { message: error.message || "Erro desconhecido ao criar personagem." };
    }
};

/**
 * Busca todos os personagens.
 * @returns {Promise<Array<object>>} Uma lista de personagens.
 */
export const getAllCharacters = async () => {
    try {
        const response = await apiClient.get(''); // Rota relativa à baseURL
        return response.data;
    } catch (error) {
        console.error("Erro ao buscar personagens:", error.response ? error.response.data : error.message);
        throw error.response?.data || { message: error.message || "Erro desconhecido ao buscar personagens." };
    }
};