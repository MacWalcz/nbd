// Plik: frontend/src/components/users/UserForm.jsx

import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { createUser, fetchUserById, updateUser } from '../../api/apiService';

// Podstawowa walidacja po stronie klienta (Wymaganie!)
const validate = (data, isClient) => {
    const errors = {};
    if (!data.login || data.login.length < 3 || data.login.length > 30) {
        errors.login = "Login musi mieć 3-30 znaków.";
    }
    if (!data.firstName) errors.firstName = "Imię jest wymagane.";
    if (!data.lastName) errors.lastName = "Nazwisko jest wymagane.";

    if (data.phoneNumber && (data.phoneNumber.length < 7 || data.phoneNumber.length > 15)) {
        errors.phoneNumber = "Telefon musi mieć 7-15 cyfr.";
    }
    if (isClient && !data.clientType) errors.clientType = "Typ klienta jest wymagany.";

    return errors;
};

const UserForm = () => {
    const { id, type } = useParams(); // type to 'clients', 'employees', 'administrators'
    const navigate = useNavigate();
    const isEdit = !!id;
    const initialType = type || 'clients'; // Domyślnie Klient przy tworzeniu
    const isClientForm = initialType === 'clients';

    const [formData, setFormData] = useState({
        login: '', firstName: '', lastName: '', phoneNumber: '', active: false,
        clientType: '1',
    });
    const [currentType, setCurrentType] = useState(initialType);
    const [errors, setErrors] = useState({});
    const [loading, setLoading] = useState(isEdit);

    // Ładowanie danych do edycji
    useEffect(() => {
        if (isEdit) {
            setLoading(true);
            fetchUserById(id, currentType)
                .then(data => {
                    setFormData({
                        login: data.login || '',
                        firstName: data.firstName || '',
                        lastName: data.lastName || '',
                        phoneNumber: data.phoneNumber || '',
                        active: data.active,
                        // ClientType jest obiektem, ale w DTO jest Stringiem (1, 2, 3)
                        clientType: data.clientType || '1',
                        id: data.id,
                        version: data.version
                    });
                })
                .catch(err => {
                    alert("Błąd ładowania danych użytkownika.");
                    navigate('/users');
                })
                .finally(() => setLoading(false));
        }
    }, [isEdit, id, currentType, navigate]);

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: type === 'checkbox' ? checked : value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const currentErrors = validate(formData, currentType === 'clients');
        setErrors(currentErrors);

        if (Object.keys(currentErrors).length > 0) {
            alert("Proszę poprawić błędy w formularzu.");
            return;
        }

        // Potwierdzenie akcji (Wymaganie!)
        const actionText = isEdit ? `modyfikację użytkownika ${id.substring(0, 8)}` : "utworzenie nowego użytkownika";
        if (!window.confirm(`Czy na pewno chcesz wykonać ${actionText}?`)) {
            return;
        }

        try {
            if (isEdit) {
                await updateUser(id, currentType, formData);
                alert("Użytkownik pomyślnie zmodyfikowany.");
            } else {
                await createUser(currentType, formData);
                alert("Użytkownik pomyślnie utworzony.");
            }
            navigate('/users');
        } catch (error) {
            alert(`Błąd: ${error.response ? error.response.data.reason : error.message}`);
        }
    };

    if (loading) return <div>Ładowanie danych formularza...</div>;

    return (
        <div>
            <h2>{isEdit ? `Edycja Użytkownika (${currentType.toUpperCase()})` : 'Tworzenie Nowego Użytkownika'}</h2>

            {!isEdit && (
                <div className="form-group">
                    <label>Typ Użytkownika:</label>
                    <select value={currentType} onChange={e => setCurrentType(e.target.value)}>
                        <option value="clients">Klient</option>
                        <option value="employees">Pracownik</option>
                        <option value="administrators">Administrator</option>
                    </select>
                </div>
            )}

            <form onSubmit={handleSubmit} style={{ backgroundColor: 'white', padding: '20px', borderRadius: '8px' }}>
                <div className="form-group">
                    <label>Login:</label>
                    <input type="text" name="login" value={formData.login} onChange={handleChange} disabled={isEdit} />
                    {errors.login && <span style={{ color: 'red' }}>{errors.login}</span>}
                </div>

                <div className="form-group">
                    <label>Imię:</label>
                    <input type="text" name="firstName" value={formData.firstName} onChange={handleChange} />
                    {errors.firstName && <span style={{ color: 'red' }}>{errors.firstName}</span>}
                </div>

                <div className="form-group">
                    <label>Nazwisko:</label>
                    <input type="text" name="lastName" value={formData.lastName} onChange={handleChange} />
                    {errors.lastName && <span style={{ color: 'red' }}>{errors.lastName}</span>}
                </div>

                <div className="form-group">
                    <label>Telefon:</label>
                    <input type="text" name="phoneNumber" value={formData.phoneNumber} onChange={handleChange} />
                    {errors.phoneNumber && <span style={{ color: 'red' }}>{errors.phoneNumber}</span>}
                </div>

                {currentType === 'clients' && (
                    <div className="form-group">
                        <label>Typ Klienta:</label>
                        <select name="clientType" value={formData.clientType} onChange={handleChange}>
                            <option value="1">Default (1)</option>
                            <option value="2">Premium (2)</option>
                            <option value="3">Luxury (3)</option>
                        </select>
                        {errors.clientType && <span style={{ color: 'red' }}>{errors.clientType}</span>}
                    </div>
                )}



                <div className="form-group">
                    <label>Aktywny:</label>
                    <input type="checkbox" name="active" checked={formData.active} onChange={handleChange} style={{ display: 'inline', width: 'auto' }} />
                </div>

                <button type="submit" className="primary">{isEdit ? 'Zapisz Zmiany' : 'Utwórz'}</button>
                <button type="button" onClick={() => navigate('/users')} style={{ marginLeft: '10px' }}>Anuluj</button>
            </form>
        </div>
    );
};

export default UserForm;