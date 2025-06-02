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

/**
 @param {object} combatRequestData 
 @returns {Promise<object>} 
 */
export const startFullCombat = async (combatRequestData) => {
    try {
        const response = await apiClient.post('', combatRequestData);
        return response.data;
    } catch (error) {
        console.error("Erro ao iniciar combate:", error.response ? error.response.data : error.message);
        throw error.response?.data || { message: error.message || "Erro desconhecido ao iniciar combate." };
    }
};