import axios from 'axios';

const API_AUTH_URL = 'http://localhost:8080/api/v1'; 

/**
 @param {object} userData 
 @returns {Promise<object>} 
 */
export const register = async (userData) => {
    try {
        const response = await axios.post(`${API_AUTH_URL}/register`, userData);
        return response.data;
    } catch (error) {
        console.error("Erro no registro:", error.response ? error.response.data : error.message);
        throw error.response?.data || { message: error.message || "Erro desconhecido no registro." };
    }
};

/**
 @param {object} credentials 
 @returns {Promise<object>} 
 */
export const login = async (credentials) => {
    try {
        const authRequest = { username: credentials.email, password: credentials.password };
        const response = await axios.post(`${API_AUTH_URL}/authenticate`, authRequest);
        if (response.data && response.data.token) {
            localStorage.setItem('userToken', response.data.token);
        }
        return response.data;
    } catch (error) {
        console.error("Erro no login:", error.response ? error.response.data : error.message);
        throw error.response?.data || { message: error.message || "Erro desconhecido no login." };
    }
};

export const logout = () => {
    localStorage.removeItem('userToken');
};

/**
 @returns {string|null} 
 */
export const getToken = () => {
    return localStorage.getItem('userToken');
};