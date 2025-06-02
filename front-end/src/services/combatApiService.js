import axios from 'axios';
import { getToken } from './authService';

const API_COMBAT_URL = 'http://localhost:8080/api/combat';

const apiClient = axios.create({
    baseURL: API_COMBAT_URL,
});

apiClient.interceptors.request.use(
    (config) => {
        const token = getToken();
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

export const startFullCombat = async (combatRequestData) => {
    try {
        const response = await apiClient.post('', combatRequestData);
        return response.data;
    } catch (error) {
        console.error("Erro ao iniciar combate:", error.response ? error.response.data : error.message);
        throw error.response?.data || { message: error.message || "Erro desconhecido ao iniciar combate." };
    }
};

/**
 * @returns {Promise<Array<object>>} 
 */
export const getCombatHistory = async () => {
    try {
        const response = await apiClient.get('/history'); 
        return response.data;
    } catch (error) {
        console.error("Erro ao buscar histórico de combates:", error.response ? error.response.data : error.message);
        throw error.response?.data || { message: error.message || "Erro desconhecido ao buscar histórico de combates." };
    }
};