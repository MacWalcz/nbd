import React, { useState, useEffect } from 'react';
import { View, Text, TouchableOpacity, StyleSheet, Alert, ActivityIndicator, ScrollView } from 'react-native';
import { useRouter } from 'expo-router';
import { Picker } from '@react-native-picker/picker';
import DateTimePicker from '@react-native-community/datetimepicker';
import {createRent, endRent, fetchAllClients, fetchAllHouses, fetchAllRents} from '../api/apiService';

export default function RentForm() {
    const [loading, setLoading] = useState(true);
    const [clients, setClients] = useState([]);
    const [availableHouses, setAvailableHouses] = useState([]);
    const [clientId, setClientId] = useState('');
    const [houseId, setHouseId] = useState('');
    const [startDate, setStartDate] = useState(new Date());
    const [showPicker, setShowPicker] = useState(false);
    const router = useRouter();

    useEffect(() => {
        const load = async () => {
            try {
                const [cData, hData, rData] = await Promise.all([fetchAllClients(), fetchAllHouses(), fetchAllRents()]);

                const occupied = new Set(rData.filter(r => !r.endDate).map(r => r.house?.id).filter(id => id));
                const filteredHouses = hData.filter(h => !occupied.has(h.id));

                setClients(cData.filter(c => c.active));
                setAvailableHouses(filteredHouses);
            } catch (e) {
                Alert.alert("Błąd", "Błąd ładowania danych formularza.");
            } finally { setLoading(false); }
        };
        load();
    }, []);

    const handleSubmit = async () => {
        if (!clientId || !houseId) return Alert.alert("Błąd", "Wypełnij wszystkie pola!");


        Alert.alert("Potwierdzenie", "Czy na pewno chcesz stworzyć wypożyczenie?", [
            { text: "Anuluj" },
            { text: "Tak", onPress: async () => {
                    try {
                        await createRent(clientId, houseId, startDate);
                        Alert.alert("Sukces", "Utworzono nową alokację.");
                        router.back();
                    } catch (e) {
                        Alert.alert("Błąd", e.response?.data?.reason || "Nie udało się utworzyć alokacji.");
                    }
                }
            }
        ]);
    };

    if (loading) return <ActivityIndicator size="large" style={{marginTop: 50}} />;

    return (
        <ScrollView style={styles.container}>
            <Text style={styles.label}>Klient (tylko aktywni):</Text>
            <View style={styles.pickerBox}>
                <Picker selectedValue={clientId} onValueChange={setClientId}>
                    <Picker.Item label="Wybierz Klienta" value="" />
                    {clients.map(c => <Picker.Item key={c.id} label={`${c.firstName} ${c.lastName} (${c.login})`} value={c.id} />)}
                </Picker>
            </View>

            <Text style={styles.label}>Dom (Tylko Wolne):</Text>
            <View style={styles.pickerBox}>
                <Picker selectedValue={houseId} onValueChange={setHouseId} enabled={availableHouses.length > 0}>
                    <Picker.Item label="Wybierz Dom" value="" />
                    {availableHouses.map(h => (
                        <Picker.Item key={h.id} label={`Nr ${h.houseNumber} (${h.area} m², ${h.price} PLN/d)`} value={h.id} />
                    ))}
                </Picker>
            </View>
            {availableHouses.length === 0 && <Text style={styles.errorText}>Brak dostępnych domów!</Text>}

            <Text style={styles.label}>Data Startu:</Text>
            <TouchableOpacity style={styles.dateBtn} onPress={() => setShowPicker(true)}>
                <Text>{startDate.toISOString().split('T')[0]}</Text>
            </TouchableOpacity>
            {showPicker && <DateTimePicker value={startDate} mode="date" onChange={(e, d) => { setShowPicker(false); if(d) setStartDate(d); }} />}

            <TouchableOpacity style={[styles.submitBtn, availableHouses.length === 0 && {backgroundColor: '#ccc'}]} onPress={handleSubmit} disabled={availableHouses.length === 0}>
                <Text style={styles.btnWhite}>UTWÓRZ ALOKACJĘ</Text>
            </TouchableOpacity>
        </ScrollView>
    );
}

const styles = StyleSheet.create({
    container: { flex: 1, padding: 20, backgroundColor: '#fff' },
    label: { fontWeight: 'bold', marginTop: 15, marginBottom: 5 },
    pickerBox: { borderWidth: 1, borderColor: '#ccc', borderRadius: 8, marginBottom: 10 },
    dateBtn: { padding: 15, backgroundColor: '#f0f0f0', borderRadius: 8, alignItems: 'center' },
    submitBtn: { backgroundColor: '#4CAF50', padding: 18, borderRadius: 10, marginTop: 40, alignItems: 'center' },
    btnWhite: { color: 'white', fontWeight: 'bold' },
    errorText: { color: 'red', fontSize: 12 }
});