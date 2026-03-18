import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { createRent, fetchAllClients, fetchAllHouses, fetchAllRents, fetchAvailableHouses } from '../../api/apiService';

const RentForm = () => {
    const navigate = useNavigate();


    const user = JSON.parse(sessionStorage.getItem('user'));
    const isClient = user?.role === 'CLIENT';


    const [clientId, setClientId] = useState(isClient ? user.id : '');
    const [houseId, setHouseId] = useState('');
    const [startDate, setStartDate] = useState(new Date().toISOString().split('T')[0]);

    const [clients, setClients] = useState([]);
    const [availableHouses, setAvailableHouses] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const loadFormData = async () => {
            try {
                // Загружаем только то, что нужно
                const [clientsData, availableHousesData] = await Promise.all([
                    !isClient ? fetchAllClients() : Promise.resolve([]),
                    fetchAvailableHouses() // Наш новый метод
                ]);

                setAvailableHouses(availableHousesData);

                if (!isClient) {
                    setClients(clientsData.filter(c => c.active));
                }
            } catch (error) {
                console.error("Błąd ładowania:", error);
                alert("Błąd ładowania wolnych domów.");
            } finally {
                setLoading(false);
            }
        };
        loadFormData();
    }, [isClient]);

    const handleSubmit = async (e) => {
        e.preventDefault();

        // Валидация ID клиента (берем либо из сессии, либо из селекта)
        const finalClientId = isClient ? user.id : clientId;

        if (!houseId || !startDate) {
            alert('Wszystkie pola muszą być wypełnione!');
            return;
        }

        try {
            const newRent = await createRent(finalClientId, houseId, startDate);
            if (newRent) {
                alert(`Pomyślnie utworzono alokację!`);
                navigate('/rents');
            }
        } catch (error) {
            alert(`Błąd: ${error.response?.data?.reason || error.message}`);
        }
    };

    if (loading) return <div>Ładowanie danych...</div>;

    return (
        <div>
            <h2>{isClient ? 'Zarezerwuj dom' : 'Tworzenie Nowej Alokacji'}</h2>
            <form onSubmit={handleSubmit} style={{ backgroundColor: 'white', padding: '20px', borderRadius: '8px' }}>

                {/* Если НЕ клиент - показываем выбор клиента */}
                {!isClient ? (
                    <div className="form-group">
                        <label>Klient (tylko aktywni):</label>
                        <select value={clientId} onChange={e => setClientId(e.target.value)} required>
                            <option value="">Wybierz Klienta</option>
                            {clients.map(c => (
                                <option key={c.id} value={c.id}>{c.firstName} {c.lastName} ({c.login})</option>
                            ))}
                        </select>
                    </div>
                ) : (
                    <div className="form-group">
                        <p>Rezerwacja dla: <strong>{user.sub}</strong></p>
                    </div>
                )}

                <div className="form-group">
                    <label>Dom (Dostępne):</label>
                    <select value={houseId} onChange={e => setHouseId(e.target.value)} required disabled={availableHouses.length === 0}>
                        <option value="">Wybierz Dom</option>
                        {availableHouses.map(h => (
                            <option key={h.id} value={h.id}>
                                Nr {h.houseNumber} — {h.price} PLN/doba
                            </option>
                        ))}
                    </select>
                    {availableHouses.length === 0 && <p style={{ color: 'red' }}>Obecnie brak wolnych domów!</p>}
                </div>

                <div className="form-group">
                    <label>Data Startu:</label>
                    <input type="date" value={startDate} onChange={e => setStartDate(e.target.value)} required />
                </div>

                <button type="submit" className="primary" disabled={availableHouses.length === 0}>
                    Potwierdź rezerwację
                </button>
            </form>
        </div>
    );
};

export default RentForm;