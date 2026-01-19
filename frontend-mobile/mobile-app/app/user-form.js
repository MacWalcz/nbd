import React, { useState, useEffect } from 'react';
import { View, Text, TextInput, TouchableOpacity, StyleSheet, Alert, ScrollView } from 'react-native';
import { useRouter, useLocalSearchParams } from 'expo-router';
import { createUser, updateUser, fetchUserById } from '../api/apiService';

export default function UserForm() {
    const { id, type } = useLocalSearchParams();
    const router = useRouter();
    const [form, setForm] = useState({ firstName: '', lastName: '', login: '' });

    useEffect(() => {
        if (id) {
            fetchUserById(id).then(data => setForm({
                firstName: data.firstName,
                lastName: data.lastName,
                login: data.login
            }));
        }
    }, [id]);

    const handleSave = async () => {
        // Валидация
        if (!form.firstName || !form.lastName || !form.login) {
            Alert.alert("Błąd", "Wszystkie pola muszą być wypełnione!");
            return;
        }

        Alert.alert("Potwierdzenie", "Czy zapisać zmiany?", [
            { text: "Anuluj" },
            { text: "Zapisz", onPress: async () => {
                    try {
                        if (id) await updateUser(id, type, form);
                        else await createUser('clients', form);
                        router.back();
                    } catch (e) { Alert.alert("Błąd", "Operacja nie powiodła się."); }
                }}
        ]);
    };

    return (
        <ScrollView style={styles.container}>
            <Text style={styles.label}>Imię:</Text>
            <TextInput style={styles.input} value={form.firstName} onChangeText={v => setForm({...form, firstName: v})} />

            <Text style={styles.label}>Nazwisko:</Text>
            <TextInput style={styles.input} value={form.lastName} onChangeText={v => setForm({...form, lastName: v})} />

            <Text style={styles.label}>Login:</Text>
            <TextInput style={styles.input} value={form.login} onChangeText={v => setForm({...form, login: v})} />

            <TouchableOpacity style={styles.saveBtn} onPress={handleSave}>
                <Text style={styles.btnText}>Zapisz</Text>
            </TouchableOpacity>
        </ScrollView>
    );
}

const styles = StyleSheet.create({
    container: { padding: 20 },
    label: { fontWeight: 'bold', marginTop: 15 },
    input: { borderBottomWidth: 1, borderColor: '#ccc', padding: 8, fontSize: 16 },
    saveBtn: { backgroundColor: '#4CAF50', padding: 15, borderRadius: 8, marginTop: 30, alignItems: 'center' },
    btnText: { color: 'white', fontWeight: 'bold' }
});