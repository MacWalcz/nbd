// Plik: frontend/src/components/rents/RentList.jsx

import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { endRent, fetchAllRents } from '../../api/apiService';
import { getCurrentDateString } from '../../utils/helpers';


// --- Komponent wyświetlający pełną tabelę (dla aktywnych i zakończonych) ---
const RentTable = ({ rents, title }) => {
    if (!rents.length) return <h4>Brak {title}.</h4>;

    // Dodajemy inline style dla lepszego łamania słów dla ID w tabeli
    const idCellStyle = {
        maxWidth: '120px',
        overflowX: 'hidden',
        fontSize: '12px',
        wordBreak: 'break-all'
    };

    return (
        <div style={{ marginBottom: '30px', marginTop: '30px' }}>
            <h3>{title} ({rents.length})</h3>
            <table>
                <thead>
                <tr>
                    <th style={{ maxWidth: '120px' }}>ID</th>
                    <th>Klient (Login)</th>
                    <th>Nr Domu</th>
                    <th>Data Startu</th>
                    <th>Data Końca</th>
                    <th>Koszt</th>
                </tr>
                </thead>
                <tbody>
                {rents.map(rent => (
                    <tr key={rent.id}>
                        <td data-label="ID" style={idCellStyle}>
                            {rent.id}
                        </td>
                        <td data-label="Klient (Login)">{rent.client ? rent.client.login : 'N/A'}</td>
                        <td data-label="Dom (Nr)">{rent.house ? rent.house.houseNumber : 'N/A'}</td>
                        <td data-label="Data Startu">{rent.startDate}</td>
                        <td data-label="Data Końca">{rent.endDate ? rent.endDate : '—'}</td>
                        <td data-label="Koszt">{rent.cost ? `${rent.cost.toFixed(2)}` : '—'}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};


// --- NOWY KOMPONENT: Formularz zakończenia najmu z listą rozwijaną ---
const RentEndForm = ({ currentRents, onRentEnd }) => {
    const [rentIdToTerminate, setRentIdToTerminate] = useState('');
    const [endDate, setEndDate] = useState(getCurrentDateString());

    useEffect(() => {
        // Ustaw domyślny wybór na pierwszy najem, jeśli lista nie jest pusta
        if (currentRents.length > 0 && !rentIdToTerminate) {
            setRentIdToTerminate(currentRents[0].id);
        }
    }, [currentRents, rentIdToTerminate]);

    const handleEndRent = async (e) => {
        e.preventDefault();
        if (!rentIdToTerminate || !endDate) {
            alert("Musisz wybrać najem i datę zakończenia.");
            return;
        }

        try {
            await endRent(rentIdToTerminate, endDate);
            alert(`Pomyślnie zakończono najem ${rentIdToTerminate.substring(0, 8)}... Zostaną naliczone koszty.`);
            onRentEnd();
        } catch (error) {
            if (error.message !== "Anulowano przez użytkownika.") {
                alert(`Błąd zakończenia najmu: ${error.response ? error.response.data.reason : error.message}`);
            }
        }
    };

    return (
        <div style={{ padding: '20px', border: '1px solid #ccc', backgroundColor: 'white', marginBottom: '30px' }}>
            <h3>Zakończenie Aktywnej Alokacji</h3>
            <form onSubmit={handleEndRent}>
                <div className="form-group">
                    <label>Wybierz Aktywny Najem:</label>
                    <select
                        value={rentIdToTerminate}
                        onChange={e => setRentIdToTerminate(e.target.value)}
                        required
                        style={{ width: '100%', boxSizing: 'border-box' }}
                    >
                        {/* KORYGUJE TEKST WYBORU */}
                        <option value="" disabled>--- Wybierz Najem ---</option>
                        {currentRents.map(rent => (
                            <option key={rent.id} value={rent.id}>
                                ID: {rent.id.substring(0, 8)}... | Klient: {rent.client.login} | Dom: {rent.house.houseNumber} | Start: {rent.startDate}
                            </option>
                        ))}
                    </select>
                </div>
                <div className="form-group">
                    <label>Data Zakończenia:</label>
                    <input type="date" value={endDate} onChange={e => setEndDate(e.target.value)} required />
                </div>
                <button type="submit" className="danger" disabled={currentRents.length === 0}>
                    Zakończ Wybraną Alokację
                </button>
                {currentRents.length === 0 && <span style={{ marginLeft: '10px', color: 'gray' }}>Brak aktywnych najmów do zakończenia.</span>}
            </form>
        </div>
    );
};

// ... (Główny komponent RentList pozostaje ten sam)

const RentList = () => {
    const navigate = useNavigate();
    const [rents, setRents] = useState([]);
    const [loading, setLoading] = useState(true);

    const loadRents = async () => {
        setLoading(true);
        try {
            const data = await fetchAllRents();
            setRents(data);
        } catch (error) {
            alert("Nie udało się pobrać pełnej listy alokacji.");
            console.error(error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadRents();
    }, []);

    const handleRentEnd = () => {
        loadRents();
    };

    if (loading) return <div>Ładowanie pełnej listy alokacji...</div>;

    const currentRents = rents.filter(r => !r.endDate);
    const pastRents = rents.filter(r => r.endDate);

    return (
        <div>
            <h2>Zarządzanie Alokacjami</h2>

            <div style={{ marginBottom: '20px' }}>
                <button className="primary" onClick={() => navigate('/rents/new')}>
                    + Utwórz Nową Alokację
                </button>
            </div>

            <RentEndForm currentRents={currentRents} onRentEnd={handleRentEnd} />
            <RentTable rents={currentRents} title="Aktywne Alokacje" />
            <RentTable rents={pastRents} title="Zakończone Alokacje" />

        </div>
    );
};

export default RentList;