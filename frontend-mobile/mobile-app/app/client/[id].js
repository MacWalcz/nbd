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

            if (!cData || !cData.login) throw new Error("Niekompletne dane klienta");

            setClient(cData);
            setActiveRents(aRents);
            setPastRents(pRents);
        } catch (e) {
            const msg = e.response?.data?.reason || e.message;
            Alert.alert("Błąd danych", `Nie udało się pobrać szczegółów: ${msg}`);
        } finally { setLoading(false); }
    };

    useFocusEffect(useCallback(() => { load(); }, [id]));

    if (loading) return <ActivityIndicator size="large" style={{marginTop: 50}} />;
    if (!client) return (
        <View style={styles.center}><Text>Błąd ładowania danych klienta.</Text></View>
    );

    const RentRow = ({ rent }) => (
        <View style={styles.rentRow}>
            <View style={styles.rentHeader}>
                <Text style={styles.bold}>Dom Nr {rent.house?.houseNumber || '???'}</Text>
                <Text style={styles.rentId}>ID: {rent.id?.substring(0,8)}...</Text>
            </View>
            <Text style={styles.dateText}>Start: {rent.startDate}</Text>
            <Text style={styles.dateText}>Koniec: {rent.endDate || 'W trakcie'}</Text>
            {rent.cost !== null && rent.cost !== undefined && (
                <Text style={styles.greenText}>Należność: {rent.cost.toFixed(2)} PLN</Text>
            )}
        </View>
    );

    return (
        <ScrollView style={styles.container}>
            <View style={styles.infoCard}>
                <Text style={styles.name}>{client.firstName} {client.lastName}</Text>
                <View style={styles.divider} />
                <Text style={styles.infoItem}><Text style={styles.bold}>ID: </Text>{client.id}</Text>
                <Text style={styles.infoItem}><Text style={styles.bold}>Login: </Text>{client.login}</Text>
                <Text style={styles.infoItem}><Text style={styles.bold}>Telefon: </Text>{client.phoneNumber || 'Brak danych'}</Text>
                <Text style={styles.infoItem}>
                    <Text style={styles.bold}>Status: </Text>
                    <Text style={{color: client.active ? 'green' : 'red', fontWeight: 'bold'}}>
                        {client.active ? 'AKTYWNY' : 'NIEAKTYWNY'}
                    </Text>
                </Text>
            </View>

            <Text style={styles.sectionTitle}>Aktywne Alokacje ({activeRents.length})</Text>
            {activeRents.map(r => <RentRow key={r.id} rent={r} />)}
            {activeRents.length === 0 && <Text style={styles.empty}>Brak aktualnie wynajmowanych domów.</Text>}

            <Text style={styles.sectionTitle}>Historia Wynajmu ({pastRents.length})</Text>
            {pastRents.map(r => <RentRow key={r.id} rent={r} />)}
            {pastRents.length === 0 && <Text style={styles.empty}>Brak historii najmu.</Text>}
            <View style={{height: 40}} />
        </ScrollView>
    );
}

const styles = StyleSheet.create({
    container: { flex: 1, padding: 15, backgroundColor: '#f0f2f5' },
    center: { flex: 1, justifyContent: 'center', alignItems: 'center' },
    infoCard: { backgroundColor: 'white', padding: 20, borderRadius: 12, elevation: 3, marginBottom: 20 },
    name: { fontSize: 24, fontWeight: 'bold', color: '#333' },
    divider: { height: 1, backgroundColor: '#eee', marginVertical: 10 },
    infoItem: { fontSize: 16, marginBottom: 5 },
    sectionTitle: { fontSize: 18, fontWeight: 'bold', marginBottom: 12, marginTop: 10, color: '#444' },
    rentRow: { backgroundColor: 'white', padding: 15, borderRadius: 10, marginBottom: 10, borderLeftWidth: 5, borderLeftColor: '#2196F3', elevation: 1 },
    rentHeader: { flexDirection: 'row', justifyContent: 'space-between', marginBottom: 5 },
    rentId: { fontSize: 10, color: '#aaa' },
    bold: { fontWeight: 'bold' },
    dateText: { fontSize: 14, color: '#666' },
    greenText: { color: '#2e7d32', fontWeight: 'bold', marginTop: 8, fontSize: 15, textAlign: 'right' },
    empty: { fontStyle: 'italic', color: '#888', textAlign: 'center', marginTop: 10, marginBottom: 20 }
});