import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import {fetchUserById, fetchRentsForClient, fetchRentsLohForClient} from '../../api/apiService';
import {jwtDecode} from "jwt-decode";

const RentTable = ({ rents, title }) => {
    if (!rents || rents.length === 0) return <p>Brak {title} alokacji.</p>;

    return (
        <div style={{ marginBottom: '30px' }}>
            <h3>{title} ({rents.length})</h3>
            <table>
                <thead>
                <tr>
                    <th>ID Najmu</th>
                    <th>Dom (Numer)</th>
                    <th>Data Startu</th>
                    <th>Data Końca</th>
                    <th>Koszt</th>
                </tr>
                </thead>
                <tbody>
                {rents.map(rent => (
                    <tr key={rent.id}>
                        <td data-label="ID Najmu">{rent.id}</td>
                        <td data-label="Dom (Numer)">{rent.house ? rent.house.houseNumber : 'N/A'}</td>
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


const ClientDetails = () => {
    const { id } = useParams();
    const [client, setClient] = useState(null);
    const [currentRents, setCurrentRents] = useState([]);
    const [pastRents, setPastRents] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const loadDetails = async () => {
            setLoading(true);
            try {

                const clientData = await fetchUserById(id, 'client');

                setClient(clientData.data);




                const currentDataRaw = await fetchRentsLohForClient(id, true);
                const currentData = currentDataRaw._embedded ? currentDataRaw._embedded.rentDTOList : [];
                setCurrentRents(currentDataRaw);


                const pastDataRaw = await fetchRentsLohForClient(id, false);
                const pastData = pastDataRaw._embedded ? pastDataRaw._embedded.rentDTOList : [];
                setPastRents(pastDataRaw);

            } catch (err) {
                setError(`Nie udało się pobrać danych: ${err.response ? err.response.data.reason : err.message}`);
                console.error(err);
            } finally {
                setLoading(false);
            }
        };

        loadDetails();
    }, [id]);

    if (loading) return <div>Ładowanie szczegółów klienta...</div>;
    if (error) return <div style={{ color: 'red' }}>Błąd: {error}</div>;
    if (!client) return <div>Brak danych klienta.</div>;

    return (
        <div>
            <h2>Szczegóły Klienta: {client.firstName} {client.lastName}</h2>

            <div style={{ border: '1px solid #ccc', padding: '15px', marginBottom: '20px', backgroundColor: 'white' }}>
                <p><strong>ID:</strong> {client.id}</p>
                <p><strong>Login:</strong> {client.login}</p>
                <p><strong>Typ Klienta:</strong> {client.clientType}</p>
                <p><strong>Telefon:</strong> {client.phoneNumber}</p>
                <p><strong>Status:</strong> <span style={{ color: client.active ? 'green' : 'red' }}>{client.active ? 'Aktywny' : 'Nieaktywny'}</span></p>
            </div>

            <RentTable rents={currentRents} title="Aktualne (Niezakończone) Alokacje" />
            <RentTable rents={pastRents} title="Zakończone Alokacje" />

        </div>
    );
};

export default ClientDetails;