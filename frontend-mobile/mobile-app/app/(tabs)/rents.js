import React, { useState, useCallback } from 'react'; // Добавили useCallback
import { View, Text, FlatList, TouchableOpacity, StyleSheet, Alert, ScrollView, ActivityIndicator } from 'react-native';
import { useRouter, useFocusEffect } from 'expo-router'; // Добавили useFocusEffect
import { Picker } from '@react-native-picker/picker';
import DateTimePicker from '@react-native-community/datetimepicker';
import {fetchAllRents, endRent, updateUser} from '../../api/apiService';

export default function RentList() {
    const [rents, setRents] = useState([]);
    const [loading, setLoading] = useState(true);
    const [rentIdToTerminate, setRentIdToTerminate] = useState('');
    const [endDate, setEndDate] = useState(new Date());
    const [showPicker, setShowPicker] = useState(false);
    const router = useRouter();

    const loadData = async () => {
        try {
            const data = await fetchAllRents();
            setRents(data);
            const active = data.filter(r => !r.endDate);
            if (active.length > 0) {
                setRentIdToTerminate(prev => prev || active[0].id);
            }
        } catch (e) {
            Alert.alert("Błąd", "Nie udało się pobrać alokacji");
        } finally {
            setLoading(false);
        }
    };

    useFocusEffect(
        useCallback(() => {
            loadData();
        }, [])
    );

    const handleEndRent = async () => {
        if (!rentIdToTerminate) return Alert.alert("Błąd", "Wybierz najem");

        const selectedRent = rents.find(r => r.id === rentIdToTerminate);

        if (selectedRent) {
            const start = new Date(selectedRent.startDate);
            const end = new Date(endDate);

            start.setHours(0, 0, 0, 0);
            end.setHours(0, 0, 0, 0);

            if (end < start) {
                return Alert.alert(
                    "Błąd daty",
                    `Data zakończenia nie może być wcześniejsza niż data rozpoczęcia (${selectedRent.startDate})`
                );
            }
        }


        Alert.alert("Potwierdzenie", "Czy na pewno chcesz zakończyć wypożyczenie?", [
            { text: "Anuluj" },
            { text: "Tak", onPress: async () => {
                    try {
                        await endRent(rentIdToTerminate, endDate);
                        Alert.alert("Sukces", "Pomyślnie zakończono najem!");
                        setRentIdToTerminate('');
                        loadData();
                    } catch (error) {
                        const msg = error.response?.data?.reason || error.response?.data?.message || error.message;
                        Alert.alert("Błąd zakończenia najmu", msg);
                    }
                }
            }
        ]);


    };

    const currentRents = rents.filter(r => !r.endDate);
    const pastRents = rents.filter(r => r.endDate);

    if (loading && rents.length === 0) return <ActivityIndicator size="large" style={{marginTop: 50}} />;

    return (
        <ScrollView style={styles.container}>
            <Text style={styles.header}>Zarządzanie Alokacjami</Text>

            <TouchableOpacity style={styles.primaryBtn} onPress={() => router.push('/rent-form')}>
                <Text style={styles.btnText}>+ UTWÓRZ NOWĄ ALOKACJĘ</Text>
            </TouchableOpacity>

            <View style={styles.formSection}>
                <Text style={styles.subHeader}>Zakończenie Aktywnej Alokacji</Text>

                <Text style={styles.label}>Wybierz Aktywny Najem:</Text>
                <View style={styles.pickerContainer}>
                    <Picker
                        selectedValue={rentIdToTerminate}
                        onValueChange={(itemValue) => setRentIdToTerminate(itemValue)}
                    >
                        {currentRents.length === 0 ? (
                            <Picker.Item label="Brak aktywnych najmów" value="" />
                        ) : (
                            currentRents.map(r => (
                                <Picker.Item
                                    key={r.id}
                                    label={`${r.client?.login || 'N/A'} | Dom: ${r.house?.houseNumber || 'N/A'}`}
                                    value={r.id}
                                />
                            ))
                        )}
                    </Picker>
                </View>

                <Text style={styles.label}>Data Zakończenia:</Text>
                <TouchableOpacity style={styles.dateInput} onPress={() => setShowPicker(true)}>
                    <Text>{endDate.toISOString().split('T')[0]}</Text>
                </TouchableOpacity>

                {showPicker && (
                    <DateTimePicker
                        value={endDate}
                        mode="date"
                        display="default"
                        onChange={(e, d) => { setShowPicker(false); if(d) setEndDate(d); }}
                    />
                )}

                <TouchableOpacity
                    style={[styles.dangerBtn, currentRents.length === 0 && {backgroundColor: '#ccc'}]}
                    onPress={handleEndRent}
                    disabled={currentRents.length === 0}
                >
                    <Text style={styles.btnText}>ZAKOŃCZ WYBRANĄ ALOKACJĘ</Text>
                </TouchableOpacity>
            </View>

            <Text style={styles.sectionTitle}>Aktywne Alokacje ({currentRents.length})</Text>
            {currentRents.map(item => (
                <View key={item.id} style={styles.card}>
                    <Text style={styles.cardId}>ID: {item.id}</Text>
                    <Text>Klient: {item.client?.login} | Dom: {item.house?.houseNumber}</Text>
                    <Text>Start: {item.startDate}</Text>
                </View>
            ))}

            <Text style={styles.sectionTitle}>Zakończone Alokacje ({pastRents.length})</Text>
            {pastRents.map(item => (
                <View key={item.id} style={[styles.card, {backgroundColor: '#f9f9f9'}]}>
                    <Text style={styles.cardId}>ID: {item.id}</Text>
                    <Text>Klient: {item.client?.login} | Dom: {item.house?.houseNumber}</Text>
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
    subHeader: { fontSize: 18, fontWeight: 'bold', marginBottom: 10, color: '#d9534f' },
    sectionTitle: { fontSize: 16, fontWeight: 'bold', marginTop: 25, marginBottom: 10, color: '#555' },
    formSection: { padding: 15, borderWidth: 1, borderColor: '#eee', borderRadius: 10, backgroundColor: '#fcfcfc', elevation: 2 },
    label: { fontWeight: 'bold', marginTop: 10, color: '#333' },
    pickerContainer: { borderWidth: 1, borderColor: '#ccc', borderRadius: 5, marginVertical: 5 },
    dateInput: { padding: 12, backgroundColor: '#eee', borderRadius: 5, marginVertical: 10, alignItems: 'center' },
    primaryBtn: { backgroundColor: '#007bff', padding: 15, borderRadius: 8, alignItems: 'center', marginBottom: 20 },
    dangerBtn: { backgroundColor: '#dc3545', padding: 15, borderRadius: 8, alignItems: 'center', marginTop: 15 },
    btnText: { color: 'white', fontWeight: 'bold' },
    card: { padding: 15, borderBottomWidth: 1, borderBottomColor: '#eee', backgroundColor: '#fff' },
    cardId: { fontSize: 10, color: '#888', marginBottom: 4 },
    cost: { fontWeight: 'bold', color: 'green', marginTop: 5 }
});