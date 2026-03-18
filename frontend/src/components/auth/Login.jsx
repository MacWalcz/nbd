import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const Login = () => {
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const API_URL = `http://${import.meta.env.VITE_API_BASE_URL}:8080`;
            const response = await axios.post(`${API_URL}/auth/login`, { login, password });


            if (response.data.accessToken) {
                const token = response.data.accessToken;
                const refreshToken = response.data.refreshToken;
                console.log(response.data)
                sessionStorage.setItem('token', token);
                sessionStorage.setItem('refreshToken', refreshToken);


                const base64Url = token.split('.')[1];
                const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
                const payload = JSON.parse(decodeURIComponent(atob(base64).split('').map(function(c) {
                    return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
                }).join('')));

                sessionStorage.setItem('user', JSON.stringify(payload));


                if (payload.role === 'CLIENT') {
                    navigate('/rents');
                } else {
                    navigate('/users');
                }


                window.dispatchEvent(new Event("storage"));
            }
        } catch (error) {
            console.error("Login error:", error);
            alert("Błąd logowania: Nieprawidłowy login lub hasło");
        }
    };

    return (
        <div style={{ maxWidth: '400px', margin: '100px auto', padding: '20px', border: '1px solid #ccc', backgroundColor: 'white', borderRadius: '8px' }}>
            <h2 style={{ textAlign: 'center' }}>System Rezerwacji</h2>
            <form onSubmit={handleLogin}>
                <div className="form-group">
                    <label>Login:</label>
                    <input type="text" value={login} onChange={e => setLogin(e.target.value)} required />
                </div>
                <div className="form-group">
                    <label>Hasło:</label>
                    <input type="password" value={password} onChange={e => setPassword(e.target.value)} required />
                </div>
                <button type="submit" className="primary" style={{ width: '100%', marginTop: '10px' }}>
                    Zaloguj
                </button>
            </form>
        </div>
    );
};

export default Login;