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

export const fetchUserById = async (id, type) => {
    const path = getTypePath(type);
    return await axios.get(`${API_URL}/users/${path}/${id}`);
};



export const createUser = async (userType, data) => {
    const path = getTypePath(userType);
    const response = await axios.post(`${API_URL}/users/${path}`, data);
    return response.data;
};

export const updateUser = async (id, type, data, etag) => {
    return await axios.put(`${API_URL}/users/${type}/${id}`, data, {
        headers: {
            'If-Match': etag
        }
    });
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

export const fetchRentsLohForClient = async (clientId, current = true) => {
    const status = current ? 'current_for_client' : 'past_for_client';
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
    console.log(response.data)
    return response.data;
};

export const login = async (login, password) => {
    const response = await axios.post(`${API_URL}/auth/login`, { login, password });
    if (response.data.accessToken) {
        sessionStorage.setItem('token', response.data.accessToken);
        sessionStorage.setItem('user', JSON.stringify(parseJwt(response.data.accessToken)));
    }
    return response.data;
};

export const changePassword = async (id,password) => {
    await axios.post(`${API_URL}/users/change-password/${id}`,
        {password: password}, {

        });
}

export const logout = () => {
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('user');
    window.location.href = '/login';
};


function parseJwt(token) {
    try {
        return JSON.parse(atob(token.split('.')[1]));
    } catch (e) {
        return null;
    }
}

axios.interceptors.request.use(config => {
    const token = sessionStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

axios.interceptors.response.use(

    response => {
        console.log("[Axios Response OK]", response.config.url, response.status);
        return response;
    },
    async error => {
        console.log("[Axios Response ERROR]", error.config?.url, error.response?.status);
        const originalRequest = error.config;

        if (error.response && error.response.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;

            try {

                const refreshResponse = await axios.post(`${API_URL}/auth/refresh`, { refreshToken: sessionStorage.getItem('refreshToken') }, {

                });

                const newAccessToken = refreshResponse.data.accessToken;
                if (newAccessToken) {
                    sessionStorage.setItem('token', newAccessToken);


                    originalRequest.headers['Authorization'] = `Bearer ${newAccessToken}`;
                    return axios(originalRequest);
                }
            } catch (refreshError) {

                console.error("Nie udało się odświeżyć tokena", refreshError);
                sessionStorage.removeItem('token');
                sessionStorage.removeItem('refreshToken')
                sessionStorage.removeItem('user');
                window.location.href = '/login';
                return Promise.reject(refreshError);
            }
        }


        return Promise.reject(error);
    }
);

export const fetchAvailableHouses = async () => {
    const response = await axios.get(`${API_URL}/houses/available`);
    return response.data;
};