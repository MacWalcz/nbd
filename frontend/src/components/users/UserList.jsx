import React, { useState, useEffect } from 'react';
import { fetchAllUsers, toggleActiveStatus } from '../../api/apiService';
import { useNavigate } from 'react-router-dom';

const UserList = () => {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [filterId, setFilterId] = useState('');
    const navigate = useNavigate();

    const loadUsers = async () => {
        setLoading(true);
        try {
            const data = await fetchAllUsers();
            setUsers(data);
        } catch (error) {
            alert("Nie udało się pobrać danych użytkowników.");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadUsers();
    }, []);

    const handleActivation = async (id, type, isActive) => {
        try {
            await toggleActiveStatus(id, type, !isActive);
            loadUsers();
        } catch (e) {
            if (e.message !== "Anulowano przez użytkownika.") {
                alert(`Błąd zmiany statusu: ${e.response ? e.response.data.reason : e.message}`);
            }
        }
    };

    const filteredUsers = users.filter(user =>
        !filterId || user.id.toLowerCase().includes(filterId.toLowerCase())
    );

    if (loading) return <div>Ładowanie danych...</div>;

    return (
        <div>
            <h2>Lista Użytkowników</h2>

            <div style={{ marginBottom: '20px' }}>
                <input
                    type="text"
                    placeholder="Filtruj wg fragmentu ID"
                    value={filterId}
                    onChange={e => setFilterId(e.target.value)}
                    style={{ width: '300px' }}
                />
                <button className="primary" onClick={() => navigate('/users/new')} style={{ marginLeft: '10px' }}>
                    + Dodaj Nowego Użytkownika
                </button>
            </div>

            {filteredUsers.length === 0 ? <div>Brak użytkowników spełniających kryteria.</div> : (
                <table>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Login</th>
                        <th>Imię Nazwisko</th>
                        <th>Typ</th>
                        <th>Aktywny</th>
                        <th>Akcje</th>
                    </tr>
                    </thead>
                    <tbody>
                    {filteredUsers.map(user => {
                        let userType;
                        if (user.clientType !== undefined) {
                            userType = 'clients';
                        }
                        else if (user.position !== undefined) {
                            userType = 'employees';
                        }
                        else {
                            userType = 'administrators';
                        }

                        return (
                            <tr key={user.id}>
                                <td data-label="ID" style={{
                                    maxWidth: '120px',
                                    overflowX: 'hidden',
                                    fontSize: '12px',
                                    wordBreak: 'break-all'
                                }}>
                                    {user.id}
                                </td>
                                <td data-label="Login">{user.login}</td>
                                <td data-label="Imię Nazwisko">{user.firstName} {user.lastName}</td>
                                <td data-label="Typ">{userType.toUpperCase()}</td>
                                <td data-label="Aktywny" style={{ color: user.active ? 'green' : 'red' }}>
                                    {user.active ? 'TAK' : 'NIE'}
                                </td>
                                <td data-label="Akcje">
                                    {userType === 'clients' && (
                                        <button className="info" onClick={() => navigate(`/users/clients/${user.id}`)}>
                                            Podgląd Klienta
                                        </button>
                                    )}

                                    <button onClick={() => navigate(`/users/${userType}/${user.id}/edit`)}>
                                        Modyfikuj
                                    </button>

                                    <button
                                        onClick={() => handleActivation(user.id, userType, user.active)}
                                        className={user.active ? 'danger' : 'primary'}
                                    >
                                        {user.active ? 'Dezaktywuj' : 'Aktywuj'}
                                    </button>
                                </td>
                            </tr>
                        );
                    })}
                    </tbody>
                </table>
            )}
        </div>
    );
};

export default UserList;