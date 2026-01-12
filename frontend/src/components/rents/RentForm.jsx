// Plik: frontend/src/components/rents/RentForm.jsx (POPRAWIONA WERSJA)

import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { createRent, fetchAllClients, fetchAllHouses, fetchAllRents } from '../../api/apiService';

const RentForm = () => {
    const navigate = useNavigate();
    const [clientId, setClientId] = useState('');
    const [houseId, setHouseId] = useState('');
    const [startDate, setStartDate] = useState('');

    const [clients, setClients] = useState([]);
    const [availableHouses, setAvailableHouses] = useState([]); // Zmieniona nazwa stanu
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const loadFormData = async () => {
            try {
                // 1. Pobieramy wszystkie Domy i Klientów
                const [clientsData, housesData] = await Promise.all([
                    fetchAllClients(),
                    fetchAllHouses()
                ]);

                // 2. Pobieramy WSZYSTKIE najmy (do filtrowania dostępności)
                const allRents = await fetchAllRents();

                // --- LOGIKA FILTROWANIA ZAJĘTYCH DOMÓW ---

                // Znajdź ID domów, które są AKTYWNIE wynajmowane (endDate jest null/undefined)
                const occupiedHouseIds = new Set(
                    allRents
                        .filter(rent => !rent.endDate) // Tylko niezakończone najmy
                        .map(rent => rent.house ? rent.house.id : null) // Pobieramy ID Domu
                        .filter(id => id !== null) // Filtrujemy null
                );

                // Filtrujemy pełną listę domów, zostawiając tylko te, których ID nie ma na liście zajętych
                const filteredHouses = housesData.filter(h => !occupiedHouseIds.has(h.id));

                // Ustawiamy stany
                setClients(clientsData.filter(c => c.active)); // Tylko aktywni klienci
                setAvailableHouses(filteredHouses); // TYLKO WOLNE DOMY

            } catch (error) {
                alert("Błąd ładowania danych klientów/domów lub najmów.");
                console.error("Błąd ładowania danych formularza:", error);
            } finally {
                setLoading(false);
            }
        };
        loadFormData();
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!clientId || !houseId || !startDate) {
            alert('Wszystkie pola muszą być wypełnione!');
            return;
        }

        try {
            const newRent = await createRent(clientId, houseId, startDate);
            if (newRent) {
                alert(`Pomyślnie utworzono alokację! ID: ${newRent.id.substring(0, 8)}`);
                navigate('/rents');
            }
        } catch (error) {
            if (error.message !== "Anulowano przez użytkownika.") {
                alert(`Błąd tworzenia alokacji: ${error.response ? error.response.data.reason : error.message}`);
            }
        }
    };

    if (loading) return <div>Ładowanie danych formularza...</div>;

    return (
        <div>
            <h2>Tworzenie Nowej Alokacji</h2>
            <form onSubmit={handleSubmit} style={{ backgroundColor: 'white', padding: '20px', borderRadius: '8px' }}>
                <div className="form-group">
                    <label>Klient (tylko aktywni):</label>
                    <select value={clientId} onChange={e => setClientId(e.target.value)} required>
                        <option value="">Wybierz Klienta</option>
                        {clients.map(c => (
                            <option key={c.id} value={c.id}>{c.firstName} {c.lastName} ({c.login})</option>
                        ))}
                    </select>
                </div>

                <div className="form-group">
                    <label>Dom (Tylko Wolne):</label>
                    <select value={houseId} onChange={e => setHouseId(e.target.value)} required disabled={availableHouses.length === 0}>
                        <option value="">Wybierz Dom</option>
                        {availableHouses.map(h => ( // Używamy tylko dostępnych domów!
                            <option key={h.id} value={h.id}>Nr {h.houseNumber} ({h.area} m², {h.price}/dzień)</option>
                        ))}
                    </select>
                    {availableHouses.length === 0 && (
                        <p style={{ color: 'red' }}>Brak dostępnych domów do wynajęcia!</p>
                    )}
                </div>

                <div className="form-group">
                    <label>Data Startu:</label>
                    <input
                        type="date"
                        value={startDate}
                        onChange={e => setStartDate(e.target.value)}
                        required
                    />
                </div>

                <button type="submit" className="primary" disabled={availableHouses.length === 0}>
                    Utwórz Alokację
                </button>
                <button type="button" onClick={() => navigate('/rents')} style={{ marginLeft: '10px' }}>Anuluj</button>
            </form>
        </div>
    );
};

export default RentForm;