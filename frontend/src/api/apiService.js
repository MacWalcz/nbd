import axios from 'axios';

const YOUR_LOCAL_IP = import.meta.env.VITE_API_BASE_URL;
const API_URL = `http://${YOUR_LOCAL_IP}:8080`;

if (!API_URL) {
    console.error("Błąd konfiguracji: YOUR_LOCAL_IP nie jest zdefiniowany w .env!");
}

const getTypePath = (userType) => {
    if (userType.toLowerCase().includes('client')) return 'clients';
    if (userType.toLowerCase().includes('employee')) return 'employees';
    if (userType.toLowerCase().includes('administrator')) return 'administrators';
    return '';
}

export const fetchAllUsers = async () => {
    const response = await axios.get(`${API_URL}/users`);
    return response.data;
};

export const fetchUserById = async (id, userType) => {
    const path = getTypePath(userType);
    const response = await axios.get(`${API_URL}/users/${path}/${id}`);
    return response.data;
};

export const createUser = async (userType, data) => {
    const path = getTypePath(userType);
    const response = await axios.post(`${API_URL}/users/${path}`, data);
    return response.data;
};

export const updateUser = async (id, userType, data) => {
    const path = getTypePath(userType);
    const response = await axios.put(`${API_URL}/users/${path}/${id}`, data);
    return response.data;
};

export const toggleActiveStatus = async (id, userType, activate) => {
    const path = getTypePath(userType);
    const action = activate ? 'activate' : 'deactivate';

    const confirmMessage = `Czy na pewno chcesz ${activate ? 'AKTYWOWAĆ' : 'DEZAKTYWOWAĆ'} użytkownika ${id}?`;
    if (!window.confirm(confirmMessage)) {
        throw new Error("Anulowano przez użytkownika.");
    }

    const response = await axios.patch(`${API_URL}/users/${path}/${id}/${action}`);
    return response.data;
};

export const searchUsersByLogin = async (userType, partialLogin) => {
    const path = getTypePath(userType);
    const response = await axios.get(`${API_URL}/users/${path}/search?q=${partialLogin}`);
    return response.data;
};

export const fetchAllClients = async () => {
    const response = await axios.get(`${API_URL}/users/clients`);
    return response.data;
};

export const fetchAllHouses = async () => {
    const response = await axios.get(`${API_URL}/houses`);
    return response.data;
};

export const fetchRentsForClient = async (clientId, current = true) => {
    const status = current ? 'current' : 'past';
    const response = await axios.get(`${API_URL}/rents/${status}/client/${clientId}`);
    return response.data;
};

export const createRent = async (clientId, houseId, startDate) => {
    if (!window.confirm(`Potwierdź utworzenie najmu dla Klienta ${clientId} i Domu ${houseId} od ${startDate}?`)) {
        throw new Error("Anulowano przez użytkownika.");
    }

    const response = await axios.post(
        `${API_URL}/rents?client=${clientId}&house=${houseId}&startTime=${startDate}`
    );
    return response.data;
};

export const endRent = async (rentId, endDate) => {
    if (!window.confirm(`Potwierdź zakończenie najmu ${rentId} z datą ${endDate}?`)) {
        throw new Error("Anulowano przez użytkownika.");
    }

    const response = await axios.put(`${API_URL}/rents/${rentId}/end?endTime=${endDate}`);
    return response.data;
};

export const fetchAllRents = async () => {
    const response = await axios.get(`${API_URL}/rents`);
    return response.data;
};