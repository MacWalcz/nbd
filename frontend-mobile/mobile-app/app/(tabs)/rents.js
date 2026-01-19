import React, { useState, useEffect } from 'react';
import { View, Text, FlatList, TouchableOpacity, StyleSheet, Alert, ScrollView } from 'react-native';
import { useRouter } from 'expo-router';
import { Picker } from '@react-native-picker/picker';
import DateTimePicker from '@react-native-community/datetimepicker';
import { fetchAllRents, endRent } from '../../api/apiService';

export default function RentList() {
    const [rents, setRents] = useState([]);
    const [loading, setLoading] = useState(true);
    const [rentIdToTerminate, setRentIdToTerminate] = useState('');
    const [endDate, setEndDate] = useState(new Date());
    const [showPicker, setShowPicker] = useState(false);
    const router = useRouter();

    const loadData = async () => {
        setLoading(true);
        try {
            const data = await fetchAllRents();
            setRents(data);
            const active = data.filter(r => !r.endDate);
            if (active.length > 0) setRentIdToTerminate(active[0].id);
        } catch (e) {
            Alert.alert("Błąd", "Nie udało się pobrać alokacji");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => { loadData(); }, []);

    const handleEndRent = async () => {
        if (!rentIdToTerminate) return Alert.alert("Błąd", "Wybierz najem");

        try {
            await endRent(rentIdToTerminate, endDate);
            Alert.alert("Sukces", "Pomyślnie zakończono najem!");
            loadData();
        } catch (error) {
            const msg = error.response?.data?.reason || error.message;
            Alert.alert("Błąd zakończenia najmu", msg);
        }
    };

    const currentRents = rents.filter(r => !r.endDate);
    const pastRents = rents.filter(r => r.endDate);

    return (
        <ScrollView style={styles.container}>
            <Text style={styles.header}>Zarządzanie Alokacjami</Text>

            <TouchableOpacity style={styles.primaryBtn} onPress={() => router.push('/rent-form')}>
                <Text style={styles.btnText}>+ UTWÓRZ NOWĄ ALOKACJĘ</Text>
            </TouchableOpacity>

            {/* SEKCOJA ZAKOŃCZENIA (RentEndForm) */}
            <View style={styles.formSection}>
                <Text style={styles.subHeader}>Zakończenie Aktywnej Alokacji</Text>
                <Text style={styles.label}>Wybierz Aktywny Najem:</Text>
                <Picker
                    selectedValue={rentIdToTerminate}
                    onValueChange={setRentIdToTerminate}
                    enabled={currentRents.length > 0}
                >
                    {currentRents.length === 0 && <Picker.Item label="Brak aktywnych najmów" value="" />}
                    {currentRents.map(r => (
                        <Picker.Item key={r.id} label={`${r.client.login} | Dom: ${r.house.houseNumber}`} value={r.id} />
                    ))}
                </Picker>

                <Text style={styles.label}>Data Zakończenia:</Text>
                <TouchableOpacity style={styles.dateInput} onPress={() => setShowPicker(true)}>
                    <Text>{endDate.toISOString().split('T')[0]}</Text>
                </TouchableOpacity>

                {showPicker && (
                    <DateTimePicker
                        value={endDate}
                        mode="date"
                        onChange={(e, d) => { setShowPicker(false); if(d) setEndDate(d); }}
                    />
                )}

                <TouchableOpacity
                    style={[styles.dangerBtn, currentRents.length === 0 && {opacity: 0.5}]}
                    onPress={handleEndRent}
                    disabled={currentRents.length === 0}
                >
                    <Text style={styles.btnText}>ZAKOŃCZ WYBRANĄ ALOKACJĘ</Text>
                </TouchableOpacity>
            </View>

            {/* TABELA AKTYWNYCH */}
            <Text style={styles.sectionTitle}>Aktywne Alokacje ({currentRents.length})</Text>
            {currentRents.map(item => (
                <View key={item.id} style={styles.card}>
                    <Text style={styles.cardId}>ID: {item.id}</Text>
                    <Text>Klient: {item.client.login} | Dom: {item.house.houseNumber}</Text>
                    <Text>Start: {item.startDate}</Text>
                </View>
            ))}

            {/* TABELA ZAKOŃCZONYCH */}
            <Text style={styles.sectionTitle}>Zakończone Alokacje ({pastRents.length})</Text>
            {pastRents.map(item => (
                <View key={item.id} style={[styles.card, {backgroundColor: '#f9f9f9'}]}>
                    <Text style={styles.cardId}>ID: {item.id}</Text>
                    <Text>Klient: {item.client.login} | Dom: {item.house.houseNumber}</Text>
                    <Text>Okres: {item.startDate} do {item.endDate}</Text>
                    <Text style={styles.cost}>Koszt: {item.cost?.toFixed(2)} PLN</Text>
                </View>
            ))}
        </ScrollView>
    );
}

const styles = StyleSheet.create({
    container: { flex: 1, padding: 15, backgroundColor: '#fff' },
    header: { fontSize: 22, fontWeight: 'bold', marginBottom: 20 },
    subHeader: { fontSize: 18, fontWeight: 'bold', marginBottom: 10 },
    sectionTitle: { fontSize: 16, fontWeight: 'bold', marginTop: 25, marginBottom: 10, color: '#555' },
    formSection: { padding: 15, borderWidth: 1, borderColor: '#ccc', borderRadius: 8, backgroundColor: '#fdfdfd' },
    label: { fontWeight: 'bold', marginTop: 10 },
    dateInput: { padding: 12, borderBottomWidth: 1, borderColor: '#ccc', marginVertical: 10 },
    primaryBtn: { backgroundColor: '#007bff', padding: 15, borderRadius: 5, alignItems: 'center', marginBottom: 20 },
    dangerBtn: { backgroundColor: '#dc3545', padding: 15, borderRadius: 5, alignItems: 'center', marginTop: 15 },
    btnText: { color: 'white', fontWeight: 'bold' },
    card: { padding: 12, borderBottomWidth: 1, borderBottomColor: '#eee' },
    cardId: { fontSize: 10, color: '#888' },
    cost: { fontWeight: 'bold', color: 'green' }
});