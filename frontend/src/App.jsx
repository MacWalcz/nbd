import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Link, Navigate } from 'react-router-dom';
import UserList from './components/users/UserList';
import UserForm from './components/users/UserForm';
import ClientDetails from './components/users/ClientDetails';
import RentList from './components/rents/RentList';
import RentForm from './components/rents/RentForm';
import Login from './components/auth/Login';
import ChangePassword from './components/auth/ChangePassword';

const ProtectedRoute = ({ children, allowedRoles }) => {
    const token = sessionStorage.getItem('token');
    const refreshToken = sessionStorage.getItem('refreshToken')
    const userData = JSON.parse(sessionStorage.getItem('user'));
    const userRole = userData?.role ? `ROLE_${userData.role}` : null;

    if (!token && !refreshToken) return <Navigate to="/login" replace />;
    if (allowedRoles && !allowedRoles.includes(userRole)) {
        return <Navigate to="/" replace />;
    }
    return children;
};

function App() {
    const [user, setUser] = useState(JSON.parse(sessionStorage.getItem('user')));
    const role = user?.role ? `ROLE_${user.role}` : null;

    const handleLogout = () => {
        sessionStorage.removeItem('token');
        sessionStorage.removeItem('refreshToken')
        sessionStorage.removeItem('user');
        setUser(null);
        window.location.href = '/login';
    };

    return (
        <Router>
            <header style={{ backgroundColor: '#333', padding: '10px 20px', color: 'white' }}>
                <nav style={{ display: 'flex', gap: '20px', alignItems: 'center' }}>

                    {/* КЛИЕНТ НЕ ВИДИТ ЭТУ ССЫЛКУ */}
                    {user && (role === 'ROLE_ADMINISTRATOR' || role === 'ROLE_EMPLOYEE') && (
                        <Link to="/users" style={{ color: 'white', textDecoration: 'none' }}>Użytkownicy</Link>
                    )}

                    {/* ССЫЛКА НА АРЕНДЫ */}
                    {user && (
                        <Link to="/rents" style={{ color: 'white', textDecoration: 'none' }}>
                            {role === 'ROLE_CLIENT' ? 'Moje Wynajmy' : 'Zarządzanie Wypożyczeniami'}
                        </Link>
                    )}

                    {user && <Link to="/change-password" style={{ color: 'white', textDecoration: 'none' }}>Zmień hasło</Link>}

                    <div style={{ marginLeft: 'auto', display: 'flex', alignItems: 'center', gap: '15px' }}>
                        {user && (
                            <>
                                <span style={{ fontSize: '14px' }}>
                                    <strong>{user.sub}</strong> ({role})
                                </span>
                                <button onClick={handleLogout} style={{ padding: '5px 10px' }}>Wyloguj</button>
                            </>
                        )}
                    </div>
                </nav>
            </header>

            <main style={{ padding: '20px' }}>
                <Routes>
                    <Route path="/login" element={<Login />} />

                    {/* Ограничиваем просмотр списка юзеров */}
                    <Route path="/users" element={
                        <ProtectedRoute allowedRoles={['ROLE_ADMINISTRATOR', 'ROLE_EMPLOYEE']}> <UserList /> </ProtectedRoute>
                    } />

                    <Route path="/change-password" element={<ProtectedRoute> <ChangePassword /> </ProtectedRoute>} />

                    {/* Страница аренды доступна всем (бэкенд сам отфильтрует данные по токену) */}
                    <Route path="/rents" element={<ProtectedRoute> <RentList /> </ProtectedRoute>} />
                    <Route path="/rents/new" element={<ProtectedRoute> <RentForm /> </ProtectedRoute>} />

                    <Route path="/users/clients/:id" element={
                        <ProtectedRoute allowedRoles={['ROLE_ADMINISTRATOR', 'ROLE_EMPLOYEE']}> <ClientDetails /> </ProtectedRoute>
                    } />

                    <Route path="/users/new" element={
                        <ProtectedRoute allowedRoles={['ROLE_ADMINISTRATOR']}> <UserForm /> </ProtectedRoute>
                    } />

                    <Route path="/users/:type/:id/edit" element={
                        <ProtectedRoute allowedRoles={['ROLE_ADMINISTRATOR']}> <UserForm /> </ProtectedRoute>
                    } />

                    <Route path="/" element={<Navigate to={user ? (role === 'ROLE_CLIENT' ? "/rents" : "/users") : "/login"} replace />} />
                </Routes>
            </main>
        </Router>
    );
}

export default App;