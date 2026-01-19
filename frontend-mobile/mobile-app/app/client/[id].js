import React, { useState, useCallback } from 'react';
import { View, Text, StyleSheet, ScrollView, ActivityIndicator, Alert } from 'react-native';
import { useLocalSearchParams, useFocusEffect } from 'expo-router';
import { fetchUserById, fetchRentsForClient } from '../../api/apiService';

export default function ClientDetails() {
    const { id } = useLocalSearchParams();
    const [client, setClient] = useState(null);
    const [activeRents, setActiveRents] = useState([]);
    const [pastRents, setPastRents] = useState([]);
    const [loading, setLoading] = useState(true);

    const load = async () => {
        try {
            const [cData, aRents, pRents] = await Promise.all([
                fetchUserById(id, 'clients'),
                fetchRentsForClient(id, true),
                fetchRentsForClient(id, false)
            ]);
            setClient(cData);
            setActiveRents(aRents);
            setPastRents(pRents);
        } catch (e) { Alert.alert("Błąd", "Nie udało się pobrać danych klienta."); }
        finally { setLoading(false); }
    };

    useFocusEffect(useCallback(() => { load(); }, [id]));

    if (loading) return <ActivityIndicator size="large" style={{marginTop: 50}} />;
    if (!client) return <Text>Błąd danych.</Text>;

    const RentRow = ({ rent }) => (
        <View style={styles.rentRow}>
            <Text style={styles.bold}>Dom Nr {rent.house?.houseNumber}</Text>
            <Text>Od: {rent.startDate} {rent.endDate ? `Do: ${rent.endDate}` : '(Trwa)'}</Text>
            {rent.cost > 0 && <Text style={styles.greenText}>Koszt: {rent.cost.toFixed(2)} PLN</Text>}
        </View>
    );

    return (
        <ScrollView style={styles.container}>
            <View style={styles.infoCard}>
                <Text style={styles.name}>{client.firstName} {client.lastName}</Text>
                <Text>ID: {client.id}</Text>
                <Text>Login: {client.login}</Text>
                <Text>Telefon: {client.phoneNumber || 'Brak'}</Text>
                <Text>Status: <Text style={{color: client.active ? 'green' : 'red'}}>{client.active ? 'Aktywny' : 'Nieaktywny'}</Text></Text>
            </View>

            <Text style={styles.sectionTitle}>Aktywne Alokacje ({activeRents.length})</Text>
            {activeRents.map(r => <RentRow key={r.id} rent={r} />)}
            {activeRents.length === 0 && <Text style={styles.empty}>Brak aktywnych najmów.</Text>}

            <Text style={styles.sectionTitle}>Historia Alokacji ({pastRents.length})</Text>
            {pastRents.map(r => <RentRow key={r.id} rent={r} />)}
        </ScrollView>
    );
}

const styles = StyleSheet.create({
    container: { flex: 1, padding: 15, backgroundColor: '#f0f2f5' },
    infoCard: { backgroundColor: 'white', padding: 20, borderRadius: 12, elevation: 3, marginBottom: 20 },
    name: { fontSize: 22, fontWeight: 'bold', marginBottom: 10 },
    sectionTitle: { fontSize: 18, fontWeight: 'bold', marginBottom: 10, marginTop: 10 },
    rentRow: { backgroundColor: 'white', padding: 15, borderRadius: 8, marginBottom: 8, borderLeftWidth: 4, borderLeftColor: '#2196F3' },
    bold: { fontWeight: 'bold' },
    greenText: { color: 'green', fontWeight: 'bold' },
    empty: { fontStyle: 'italic', color: '#888' }
});