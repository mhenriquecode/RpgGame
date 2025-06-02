import axios from 'axios';
import { getToken } from './authService'; 

const API_BASE_URL = 'http://localhost:8080/api/characters';

const apiClient = axios.create({
    baseURL: API_BASE_URL,
});

apiClient.interceptors.request.use(
    (config) => {
        const token = getToken();
        console.log('[Interceptor Request] Token pego de getToken():', token);
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        console.log('[Interceptor Request] Headers da Config:', config.headers); 
        return config;
    },
    (error) => {
        console.error('[Interceptor Request] Erro:', error); 
        return Promise.reject(error);
    }
);

/**
 @param {object} characterData 
 @returns {Promise<object>} 
 */
export const createCharacter = async (characterData) => {
    try {
        const response = await apiClient.post('', characterData);
        return response.data;
    } catch (error) {
        console.error("Erro ao criar personagem:", error.response ? error.response.data : error.message);
        throw error.response?.data || { message: error.message || "Erro desconhecido ao criar personagem." };
    }
};

/**
 @returns {Promise<Array<object>>}
 */
export const getAllCharacters = async () => {
    try {
        const response = await apiClient.get(''); 
        return response.data;
    } catch (error) {
        console.error("Erro ao buscar personagens:", error.response ? error.response.data : error.message);
        throw error.response?.data || { message: error.message || "Erro desconhecido ao buscar personagens." };
    }
};

/**
 * @param {string} characterId
 * @returns {Promise<void>}
 */
export const deleteCharacter = async (characterId) => {
    try {
        await apiClient.delete(`/${characterId}`);
    } catch (error) {
        console.error("Erro ao deletar personagem:", error.response ? error.response.data : error.message);
        throw error.response?.data || { message: error.message || "Erro desconhecido ao deletar personagem." };
    }
};