import React, { useState } from 'react';
import axios from 'axios';
import { jwtDecode } from "jwt-decode";
import { changePassword } from "../../api/apiService.js";


const ChangePassword = () => {
    const [newPassword, setNewPassword] = useState('');
    const [message, setMessage] = useState('');

    const handleChange = async (e) => {
        e.preventDefault();
        try {

            const token = sessionStorage.getItem('token');
            const decoded = jwtDecode(token);

            const a = await changePassword(decoded.id,newPassword);


            setMessage("Hasło zostało pomyślnie zmienione!");


            setNewPassword('');


        } catch (error) {
            console.log(error)
            setMessage("Błąd podczas zmiany hasła.");
        }
    };

    return (
        <div style={{ maxWidth: '400px', padding: '20px', backgroundColor: 'white' }}>
            <h2>Zmień własne hasło</h2>
            <form onSubmit={handleChange}>
                <div className="form-group">
                    <label>Nowe Hasło:</label>
                    <input
                        type="password"
                        value={newPassword}
                        onChange={e => setNewPassword(e.target.value)}
                        required
                    />
                </div>
                <button type="submit" className="primary">Zaktualizuj hasło</button>
            </form>
            {message && <p style={{ marginTop: '10px', color: 'blue' }}>{message}</p>}
        </div>
    );
};

export default ChangePassword;