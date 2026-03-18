import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { createUser, fetchUserById, updateUser } from '../../api/apiService';

const validate = (data, isClient, isEdit) => {
    const errors = {};
    if (!data.login || data.login.length < 3 || data.login.length > 30) {
        errors.login = "Login musi mieć 3-30 znaków.";
    }
    if (!data.firstName) errors.firstName = "Imię jest wymagane.";
    if (!data.lastName) errors.lastName = "Nazwisko jest wymagane.";

    // Пароль обязателен только при создании
    if (!isEdit && (!data.password || data.password.length < 4)) {
        errors.password = "Hasło jest wymagane (min. 4 znaki).";
    }

    if (data.phoneNumber && (data.phoneNumber.length < 7 || data.phoneNumber.length > 15)) {
        errors.phoneNumber = "Telefon musi mieć 7-15 cyfr.";
    }
    if (isClient && !data.clientType) errors.clientType = "Typ klienta jest wymagany.";

    return errors;
};

const UserForm = () => {
    const { id, type } = useParams();
    const navigate = useNavigate();
    const isEdit = !!id;
    const initialType = type || 'clients';

    const [formData, setFormData] = useState({
        login: '',
        firstName: '',
        lastName: '',
        phoneNumber: '',
        active: true,
        clientType: '1',
        password: ''
    });

    const [etag, setEtag] = useState('');
    const [currentType, setCurrentType] = useState(initialType);
    const [errors, setErrors] = useState({});
    const [loading, setLoading] = useState(isEdit);

    useEffect(() => {

        if (isEdit) {
            setLoading(true);
            fetchUserById(id, currentType)
                .then(response => {
                    const data = response.data;
                    console.log(response.headers)
                    setEtag(response.headers.etag || '');

                    setFormData({
                        login: data.login || '',
                        firstName: data.firstName || '',
                        lastName: data.lastName || '',
                        phoneNumber: data.phoneNumber || '',
                        active: data.active,
                        clientType: data.clientType || '1',
                        id: data.id,
                        password: ''
                    });
                })
                .catch(err => {
                    alert("Błąd ładowania danych.");
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
        const currentErrors = validate(formData, currentType === 'clients', isEdit);
        setErrors(currentErrors);

        if (Object.keys(currentErrors).length > 0) return;

        try {
            if (isEdit) {

                await updateUser(id, currentType, formData, etag);
                alert("Zmiany zostały zapisane.");
            } else {
                await createUser(currentType, formData);
                alert("Użytkownik został utworzony.");
            }
            navigate('/users');
        } catch (error) {
            const msg = error.response?.data?.message || error.response?.data?.reason || error.message;
            alert("Błąd: " + msg);
        }
    };

    if (loading) return <div>Ładowanie...</div>;

    return (
        <div>
            <h2>{isEdit ? `Edycja (${currentType})` : 'Nowy Użytkownik'}</h2>

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
                    {errors.login && <span style={{color: 'red'}}>{errors.login}</span>}
                </div>

                {!isEdit && (
                    <div className="form-group">
                        <label>Hasło:</label>
                        <input type="password" name="password" value={formData.password} onChange={handleChange} />
                        {errors.password && <span style={{color: 'red'}}>{errors.password}</span>}
                    </div>
                )}

                <div className="form-group">
                    <label>Imię:</label>
                    <input type="text" name="firstName" value={formData.firstName} onChange={handleChange} />
                    {errors.firstName && <span style={{color: 'red'}}>{errors.firstName}</span>}
                </div>

                <div className="form-group">
                    <label>Nazwisko:</label>
                    <input type="text" name="lastName" value={formData.lastName} onChange={handleChange} />
                    {errors.lastName && <span style={{color: 'red'}}>{errors.lastName}</span>}
                </div>

                <div className="form-group">
                    <label>Telefon:</label>
                    <input type="text" name="phoneNumber" value={formData.phoneNumber} onChange={handleChange} />
                    {errors.phoneNumber && <span style={{color: 'red'}}>{errors.phoneNumber}</span>}
                </div>

                {currentType === 'clients' && (
                    <div className="form-group">
                        <label>Typ Klienta:</label>
                        <select name="clientType" value={formData.clientType} onChange={handleChange}>
                            <option value="1">Default</option>
                            <option value="2">Premium</option>
                            <option value="3">Luxury</option>
                        </select>
                    </div>
                )}

                <div style={{ marginTop: '20px' }}>
                    <button type="submit" className="primary">{isEdit ? 'Zapisz Zmiany' : 'Utwórz'}</button>
                    <button type="button" onClick={() => navigate('/users')} style={{ marginLeft: '10px' }}>Anuluj</button>
                </div>
            </form>
        </div>
    );
};

export default UserForm;