import React, { useState } from 'react';
import { View, Text, TextInput, TouchableOpacity, StyleSheet, Alert, ScrollView } from 'react-native';
import { useRouter } from 'expo-router';
import { createUser } from '../api/apiService';
import { Picker } from '@react-native-picker/picker';

export default function UserCreate() {
    const router = useRouter();
    const [userType, setUserType] = useState('clients');
    const [form, setForm] = useState({
        login: '', firstName: '', lastName: '', phoneNumber: '', clientType: '1'
    });

    const validate = () => {
        if (form.login.length < 3) return "Login musi mieć min. 3 znaki";
        if (!form.firstName || !form.lastName) return "Imię i nazwisko są wymagane";
        if (form.phoneNumber && (form.phoneNumber.length < 7)) return "Numer telefonu za krótki";
        return null;
    };

    const handleCreate = async () => {
        const error = validate();
        if (error) return Alert.alert("Walidacja", error);

        Alert.alert("Potwierdzenie", "Czy na pewno utworzyć użytkownika?", [
            { text: "Anuluj" },
            { text: "Tak", onPress: async () => {
                    try {
                        await createUser(userType, form);
                        Alert.alert("Sukces", "Użytkownik utworzony");
                        router.back();
                    } catch (e) {
                        Alert.alert("Błąd", "Nie udało się utworzyć (możliwe, że login zajęty)");
                    }
                }}
        ]);
    };

    return (
        <ScrollView style={styles.container}>
            <Text style={styles.label}>Typ Użytkownika:</Text>
            <Picker selectedValue={userType} onValueChange={itemValue => setUserType(itemValue)}>
                <option label="Klient" value="clients" />
                <option label="Pracownik" value="employees" />
                <option label="Administrator" value="administrators" />
            </Picker>

            <Text style={styles.label}>Login:</Text>
            <TextInput style={styles.input} onChangeText={t => setForm({...form, login: t})} />

            <Text style={styles.label}>Imię:</Text>
            <TextInput style={styles.input} onChangeText={t => setForm({...form, firstName: t})} />

            <Text style={styles.label}>Nazwisko:</Text>
            <TextInput style={styles.input} onChangeText={t => setForm({...form, lastName: t})} />

            <Text style={styles.label}>Telefon:</Text>
            <TextInput style={styles.input} keyboardType="phone-pad" onChangeText={t => setForm({...form, phoneNumber: t})} />

            {userType === 'clients' && (
                <>
                    <Text style={styles.label}>Typ Klienta:</Text>
                    <Picker selectedValue={form.clientType} onValueChange={v => setForm({...form, clientType: v})}>
                        <option label="Default (1)" value="1" />
                        <option label="Premium (2)" value="2" />
                        <option label="Luxury (3)" value="3" />
                    </Picker>
                </>
            )}

            <TouchableOpacity style={styles.btn} onPress={handleCreate}>
                <Text style={styles.btnText}>UTWÓRZ</Text>
            </TouchableOpacity>
        </ScrollView>
    );
}

const styles = StyleSheet.create({
    container: { padding: 20, backgroundColor: 'white' },
    label: { fontWeight: 'bold', marginTop: 15 },
    input: { borderBottomWidth: 1, borderColor: '#ccc', padding: 8 },
    btn: { backgroundColor: '#2196F3', padding: 15, marginTop: 30, alignItems: 'center', borderRadius: 5 },
    btnText: { color: 'white', fontWeight: 'bold' }
});