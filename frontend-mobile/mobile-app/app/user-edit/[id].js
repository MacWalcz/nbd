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
    const [errors, setErrors] = useState({});

    useEffect(() => {
        fetchUserById(id, type).then(data => {
            setForm(data);
            setLoading(false);
        }).catch(() => {
            Alert.alert("Błąd", "Nie znaleziono użytkownika");
            router.back();
        });
    }, [id]);

    const validate = () => {
        let sErrors = {};
        if (!form?.firstName || form.firstName.trim().length < 2)
            sErrors.firstName = "Imię musi mieć co najmniej 2 znaki";
        if (!form?.lastName || form.lastName.trim().length < 2)
            sErrors.lastName = "Nazwisko musi mieć co najmniej 2 znaki";

        if (form?.phoneNumber && form.phoneNumber.length < 7)
            sErrors.phoneNumber = "Numer telefonu jest za krótki (min. 7 cyfr)";

        setErrors(sErrors);
        return Object.keys(sErrors).length === 0;
    };

    const handleUpdate = () => {
        if (!validate()) return;

        Alert.alert("Potwierdzenie", "Czy na pewno chcesz zapisać zmiany?", [
            { text: "Anuluj" },
            { text: "Tak", onPress: async () => {
                    try {
                        await updateUser(id, type, form);
                        Alert.alert("Sukces", "Dane zaktualizowane");
                        router.back();
                    } catch (e) {
                        const msg = e.response?.data?.reason || "Błąd zapisu";
                        Alert.alert("Błąd", msg);
                    }
                }}
        ]);
    };

    // ГЛАВНОЕ ИСПРАВЛЕНИЕ: Если данных нет или идет загрузка — показываем только спиннер
    if (loading || !form) {
        return (
            <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
                <ActivityIndicator size="large" color="#FFA000" />
                <Text style={{ marginTop: 10 }}>Ładowanie danych...</Text>
            </View>
        );
    }

    return (
        <ScrollView style={styles.container}>
            <Text style={styles.label}>Login (Zablokowane):</Text>
            <TextInput style={[styles.input, styles.disabled]} value={form.login} editable={false} />

            <Text style={styles.label}>Imię:</Text>
            <TextInput
                style={[styles.input, errors.firstName && styles.inputError]}
                value={form.firstName}
                onChangeText={t => {
                    setForm({...form, firstName: t});
                    if(errors.firstName) setErrors({...errors, firstName: null});
                }}
            />
            {errors.firstName && <Text style={styles.errorText}>{errors.firstName}</Text>}

            <Text style={styles.label}>Nazwisko:</Text>
            <TextInput
                style={[styles.input, errors.lastName && styles.inputError]}
                value={form.lastName}
                onChangeText={t => {
                    setForm({...form, lastName: t});
                    if(errors.lastName) setErrors({...errors, lastName: null});
                }}
            />
            {errors.lastName && <Text style={styles.errorText}>{errors.lastName}</Text>}

            <Text style={styles.label}>Telefon:</Text>
            <TextInput
                style={[styles.input, errors.phoneNumber && styles.inputError]}
                value={form.phoneNumber}
                keyboardType="phone-pad"
                onChangeText={t => {
                    setForm({...form, phoneNumber: t});
                    if(errors.phoneNumber) setErrors({...errors, phoneNumber: null});
                }}
            />
            {errors.phoneNumber && <Text style={styles.errorText}>{errors.phoneNumber}</Text>}

            {type === 'clients' && (
                <>
                    <Text style={styles.label}>Typ Klienta:</Text>
                    <View style={styles.pickerWrapper}>
                        <Picker selectedValue={String(form.clientType)} onValueChange={v => setForm({...form, clientType: v})}>
                            <Picker.Item label="Default (1)" value="1" />
                            <Picker.Item label="Premium (2)" value="2" />
                            <Picker.Item label="Luxury (3)" value="3" />
                        </Picker>
                    </View>
                </>
            )}

            <TouchableOpacity style={styles.saveBtn} onPress={handleUpdate}>
                <Text style={styles.btnText}>ZAPISZ ZMIANY</Text>
            </TouchableOpacity>
            <View style={{ height: 50 }} />
        </ScrollView>
    );
}

const styles = StyleSheet.create({
    container: { padding: 20, backgroundColor: 'white' },
    label: { fontWeight: 'bold', marginTop: 15, color: '#333' },
    input: { borderBottomWidth: 1, borderColor: '#ccc', padding: 8, fontSize: 16, marginBottom: 5 },
    inputError: { borderColor: 'red', borderBottomWidth: 2 },
    errorText: { color: 'red', fontSize: 12, marginBottom: 5 },
    disabled: { backgroundColor: '#f0f0f0', color: '#888' },
    pickerWrapper: { borderWidth: 1, borderColor: '#eee', borderRadius: 5, marginTop: 5 },
    saveBtn: { backgroundColor: '#FFA000', padding: 15, borderRadius: 8, marginTop: 30, alignItems: 'center', elevation: 2 },
    btnText: { color: 'white', fontWeight: 'bold', fontSize: 16 }
});