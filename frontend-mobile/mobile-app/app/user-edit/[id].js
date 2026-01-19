import React, { useEffect, useState } from 'react';
import { View, Text, TextInput, TouchableOpacity, StyleSheet, Alert, ActivityIndicator, ScrollView } from 'react-native';
import { useLocalSearchParams, useRouter } from 'expo-router';
import { fetchUserById, updateUser } from '../../api/apiService';
import { Picker } from '@react-native-picker/picker';

export default function UserEdit() {
    const { id, type } = useLocalSearchParams();
    const router = useRouter();
    const [loading, setLoading] = useState(true);
    const [form, setForm] = useState(null);

    useEffect(() => {
        fetchUserById(id, type).then(data => {
            setForm(data);
            setLoading(false);
        }).catch(() => {
            Alert.alert("Błąd", "Nie znaleziono użytkownika");
            router.back();
        });
    }, [id]);

    const handleUpdate = () => {
        // ВАЛИДАЦИЯ КАК В ВЕБЕ
        if (!form.firstName || !form.lastName) {
            return Alert.alert("Błąd", "Imię i nazwisko są wymagane!");
        }

        Alert.alert("Potwierdzenie", "Czy na pewno chcesz zapisać zmiany?", [
            { text: "Anuluj" },
            { text: "Tak", onPress: async () => {
                    try {
                        await updateUser(id, type, form);
                        Alert.alert("Sukces", "Dane zaktualizowane");
                        router.back();
                    } catch (e) { Alert.alert("Błąd", "Nie udało się zapisać zmian"); }
                }}
        ]);
    };

    if (loading) return <ActivityIndicator size="large" style={{flex:1}} />;

    return (
        <ScrollView style={styles.container}>
            <Text style={styles.label}>Login (ZABLOKOWANE):</Text>
            <TextInput style={[styles.input, styles.disabled]} value={form.login} editable={false} />

            <Text style={styles.label}>Imię:</Text>
            <TextInput style={styles.input} value={form.firstName} onChangeText={t => setForm({...form, firstName: t})} />

            <Text style={styles.label}>Nazwisko:</Text>
            <TextInput style={styles.input} value={form.lastName} onChangeText={t => setForm({...form, lastName: t})} />

            <Text style={styles.label}>Telefon:</Text>
            <TextInput style={styles.input} value={form.phoneNumber} keyboardType="phone-pad" onChangeText={t => setForm({...form, phoneNumber: t})} />

            {type === 'clients' && (
                <>
                    <Text style={styles.label}>Typ Klienta:</Text>
                    <Picker selectedValue={String(form.clientType)} onValueChange={v => setForm({...form, clientType: v})}>
                        <Picker.Item label="Default (1)" value="1" />
                        <Picker.Item label="Premium (2)" value="2" />
                        <Picker.Item label="Luxury (3)" value="3" />
                    </Picker>
                </>
            )}

            <TouchableOpacity style={styles.saveBtn} onPress={handleUpdate}>
                <Text style={styles.btnText}>ZAPISZ ZMIANY</Text>
            </TouchableOpacity>
        </ScrollView>
    );
}

const styles = StyleSheet.create({
    container: { padding: 20, backgroundColor: 'white' },
    label: { fontWeight: 'bold', marginTop: 15 },
    input: { borderBottomWidth: 1, borderColor: '#ccc', padding: 8, fontSize: 16 },
    disabled: { backgroundColor: '#f0f0f0', color: '#888' },
    saveBtn: { backgroundColor: '#FFA000', padding: 15, borderRadius: 8, marginTop: 30, alignItems: 'center' },
    btnText: { color: 'white', fontWeight: 'bold' }
});