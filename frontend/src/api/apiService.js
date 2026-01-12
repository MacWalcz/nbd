import axios from 'axios';

const YOUR_LOCAL_IP = import.meta.env.VITE_API_BASE_URL;
const API_URL = `http://${YOUR_LOCAL_IP}:8080`;

if (!API_URL) {
    console.error("Błąd konfiguracji: YOUR_LOCAL_IP nie jest zdefiniowany w .env!");
}

// Pomocnicza funkcja do obsługi typów użytkowników w ścieżkach
const getTypePath = (userType) => {
    if (userType.toLowerCase().includes('client')) return 'clients';
    if (userType.toLowerCase().includes('employee')) return 'employees';
    if (userType.toLowerCase().includes('administrator')) return 'administrators';
    return ''; // W razie nieznanego typu
}

// --- ZARZĄDZANIE UŻYTKOWNIKAMI (USERS) ---

export const fetchAllUsers = async () => {
    // GET /users (pobiera wszystkich: Client, Employee, Administrator)
    const response = await axios.get(`${API_URL}/users`);
    return response.data;
};

export const fetchUserById = async (id, userType) => {
    // GET /users/clients/{id}
    const path = getTypePath(userType);
    const response = await axios.get(`${API_URL}/users/${path}/${id}`);
    return response.data;
};

export const createUser = async (userType, data) => {
    // POST /users/clients lub /users/employees lub /users/administrators
    const path = getTypePath(userType);
    const response = await axios.post(`${API_URL}/users/${path}`, data);
    return response.data;
};

export const updateUser = async (id, userType, data) => {
    // PUT /users/clients/{id}
    const path = getTypePath(userType);
    const response = await axios.put(`${API_URL}/users/${path}/${id}`, data);
    return response.data;
};

export const toggleActiveStatus = async (id, userType, activate) => {
    // PATCH /users/clients/{id}/activate LUB /deactivate
    const path = getTypePath(userType);
    const action = activate ? 'activate' : 'deactivate';

    // Potwierdzenie akcji (Wymaganie!)
    const confirmMessage = `Czy na pewno chcesz ${activate ? 'AKTYWOWAĆ' : 'DEZAKTYWOWAĆ'} użytkownika ${id}?`;
    if (!window.confirm(confirmMessage)) {
        throw new Error("Anulowano przez użytkownika.");
    }

    const response = await axios.patch(`${API_URL}/users/${path}/${id}/${action}`);
    return response.data;
};

// Wyszukiwanie po loginie (zgodne z Twoim /search)
export const searchUsersByLogin = async (userType, partialLogin) => {
    const path = getTypePath(userType);
    const response = await axios.get(`${API_URL}/users/${path}/search?q=${partialLogin}`);
    return response.data;
};

// --- ZARZĄDZANIE ALOKACJAMI (RENTS) ---

export const fetchAllClients = async () => {
    // Pomocnicze: potrzebne do formularza RentForm
    const response = await axios.get(`${API_URL}/users/clients`);
    return response.data;
};

export const fetchAllHouses = async () => {
    // Pomocnicze: potrzebne do formularza RentForm
    const response = await axios.get(`${API_URL}/houses`);
    return response.data;
};

export const fetchRentsForClient = async (clientId, current = true) => {
    // GET /rents/current/client/{clientId} LUB /rents/past/client/{clientId}
    const status = current ? 'current' : 'past';
    const response = await axios.get(`${API_URL}/rents/${status}/client/${clientId}`);
    return response.data;
};

export const createRent = async (clientId, houseId, startDate) => {
    // POST /rents?client={clientId}&house={houseId}&startTime={date}

    // Potwierdzenie akcji (Wymaganie!)
    if (!window.confirm(`Potwierdź utworzenie najmu dla Klienta ${clientId} i Domu ${houseId} od ${startDate}?`)) {
        throw new Error("Anulowano przez użytkownika.");
    }

    const response = await axios.post(
        `${API_URL}/rents?client=${clientId}&house=${houseId}&startTime=${startDate}`
    );
    return response.data;
};

export const endRent = async (rentId, endDate) => {
    // PUT /rents/{id}/end?endTime={date}

    // Potwierdzenie akcji (Wymaganie!)
    if (!window.confirm(`Potwierdź zakończenie najmu ${rentId} z datą ${endDate}?`)) {
        throw new Error("Anulowano przez użytkownika.");
    }

    const response = await axios.put(`${API_URL}/rents/${rentId}/end?endTime=${endDate}`);
    return response.data;
};

export const fetchAllRents = async () => {
    // Endpoint: GET /rents
    const response = await axios.get(`${API_URL}/rents`);
    return response.data;
};