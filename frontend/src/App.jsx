import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link, Navigate } from 'react-router-dom';
import UserList from './components/users/UserList';
import UserForm from './components/users/UserForm';
import ClientDetails from './components/users/ClientDetails';
import RentList from './components/rents/RentList';
import RentForm from './components/rents/RentForm';

function App() {
    return (
        <Router>
            <header>
                <nav style={{ padding: '10px', backgroundColor: '#333' }}>

                    <Link to="/users">Użytkownicy</Link>
                    <Link to="/rents">Zarządzanie Wypożyczeniami</Link>
                </nav>
            </header>
            <main style={{ padding: '20px' }}>
                <Routes>
                    <Route path="/" element={<Navigate to="/users" replace />} />

                    <Route path="/users" element={<UserList />} />

                    <Route path="/users/new" element={<UserForm />} />

                    <Route path="/users/:type/:id/edit" element={<UserForm />} />

                    <Route path="/users/clients/:id" element={<ClientDetails />} />

                    <Route path="/rents" element={<RentList />} />
                    <Route path="/rents/new" element={<RentForm />} />
                </Routes>
            </main>
        </Router>
    );
}

export default App;