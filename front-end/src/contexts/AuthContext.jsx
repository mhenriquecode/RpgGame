import React, { createContext, useState, useContext, useEffect, useCallback } from 'react';
import { login as apiLogin, register as apiRegister, logout as apiLogout, getToken } from '../services/authService';
import { jwtDecode } from 'jwt-decode'; 

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    const [token, setToken] = useState(getToken());
    const [user, setUser] = useState(null); 
    const [isLoading, setIsLoading] = useState(false); 
    const [authError, setAuthError] = useState(null);

    const decodeTokenAndSetUser = useCallback((currentToken) => {
        if (currentToken) {
            try {
                const decodedToken = jwtDecode(currentToken); 
                setUser({ email: decodedToken.sub, ...decodedToken }); 
                setToken(currentToken);
                localStorage.setItem('userToken', currentToken);
            } catch (error) {
                console.error("Erro ao decodificar token:", error);
                setUser(null);
                setToken(null);
                localStorage.removeItem('userToken');
            }
        } else {
            setUser(null);
            setToken(null);
            localStorage.removeItem('userToken');
        }
    }, []);

    useEffect(() => {
        const storedToken = getToken();
        if (storedToken) {
            decodeTokenAndSetUser(storedToken);
        }
    }, [decodeTokenAndSetUser]);

    const login = async (credentials) => {
        setIsLoading(true);
        setAuthError(null);
        try {
            const response = await apiLogin(credentials);
            if (response && response.token) {
                decodeTokenAndSetUser(response.token);
            } else {
                throw new Error("Token não recebido.");
            }
            return true; 
        } catch (error) {
            console.error("Falha no login (AuthContext):", error);
            setAuthError(error.message || "Email ou senha inválidos.");
            setUser(null);
            setToken(null);
            localStorage.removeItem('userToken');
            return false; 
        } finally {
            setIsLoading(false);
        }
    };

    const register = async (userData) => {
        setIsLoading(true);
        setAuthError(null);
        try {
            await apiRegister(userData);
            return true; 
        } catch (error) {
            console.error("Falha no registro (AuthContext):", error);
            setAuthError(error.message || "Falha ao registrar. Verifique os dados.");
            return false; 
        } finally {
            setIsLoading(false);
        }
    };

    const logout = () => {
        apiLogout(); 
        setUser(null);
        setToken(null);
    };

    return (
        <AuthContext.Provider value={{ token, user, isAuthenticated: !!token, login, register, logout, isLoading, authError, setAuthError }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error('useAuth deve ser usado dentro de um AuthProvider');
    }
    return context;
};