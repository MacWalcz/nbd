import axios from 'axios';
import { API_URL } from '../constants/Config'; // Upewnij się, że tu jest Twój IP, np. http://192.168.1.X:8080

const apiClient = axios.create({
    baseURL: API_URL,
    headers: {
        'Content-Type': 'application/json',
    }
});

const formatToISO = (date) => {
    if (!date) return null;
    const d = new Date(date);
    return d.toISOString().split('T')[0]; // Вырезает только YYYY-MM-DD
};

// Pomocnik do formatowania daty na YYYY-MM-DD (identycznie jak w Twoim helpers.js)
const formatToBackendDate = (date) => {
    if (!date) return '';
    if (typeof date === 'string') return date; // jeśli już jest stringiem
    const d = new Date(date);
    const year = d.getFullYear();
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
};

const getTypePath = (userType) => {
    const type = (userType || '').toLowerCase();
    if (type.includes('client')) return 'clients';
    if (type.includes('employee')) return 'employees';
    if (type.includes('administrator')) return 'administrators';
    return 'clients';
};

// --- UŻYTKOWNICY ---

export const fetchAllUsers = () => apiClient.get('/users').then(res => res.data);

export const fetchUserById = (id, userType) => {
    const path = getTypePath(userType);
    return apiClient.get(`/users/${path}/${id}`).then(res => res.data);
};

export const createUser = (userType, data) => {
    const path = getTypePath(userType);
    return apiClient.post(`/users/${path}`, data).then(res => res.data);
};

export const updateUser = (id, userType, data) => {
    const path = getTypePath(userType);
    return apiClient.put(`/users/${path}/${id}`, data).then(res => res.data);
};

export const toggleActiveStatus = (id, userType, activate) => {
    const path = getTypePath(userType);
    const action = activate ? 'activate' : 'deactivate';
    return apiClient.patch(`/users/${path}/${id}/${action}`).then(res => res.data);
};

export const fetchAllClients = () => apiClient.get('/users/clients').then(res => res.data);

// --- DOMY ---

export const fetchAllHouses = () => apiClient.get('/houses').then(res => res.data);

// --- NAJMY ---

export const fetchAllRents = () => apiClient.get('/rents').then(res => res.data);

export const fetchRentsForClient = (clientId, current = true) => {
    const status = current ? 'current' : 'past';
    return apiClient.get(`/rents/${status}/client/${clientId}`).then(res => res.data);
};

export const createRent = (clientId, houseId, startDate) => {
    // Spring Boot @RequestParam wymaga formatu YYYY-MM-DD w URL
    const dateStr = formatToBackendDate(startDate);
    return apiClient.post(`/rents?client=${clientId}&house=${houseId}&startTime=${dateStr}`).then(res => res.data);
};

export const endRent = async (rentId, endDate) => {
    const formattedDate = formatToISO(endDate);

    // ВАЖНО: axios.put(url, data, config).
    // Так как endTime — это @RequestParam, передаем его в params
    const response = await axios.put(`${API_URL}/rents/${rentId}/end`, null, {
        params: {
            endTime: formattedDate
        }
    });
    return response.data;
};