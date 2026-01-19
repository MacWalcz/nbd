import React, { useState, useCallback } from 'react';
import { View, Text, FlatList, TextInput, TouchableOpacity, StyleSheet, ActivityIndicator, Alert } from 'react-native';
import { useRouter, useFocusEffect } from 'expo-router';
import { fetchAllUsers, toggleActiveStatus } from '../../api/apiService';

export default function UserList() {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [filterId, setFilterId] = useState('');
    const router = useRouter();

    const load = async () => {
        setLoading(true);
        try {
            const data = await fetchAllUsers();
            setUsers(data);
        } catch (error) {
            Alert.alert("Błąd", "Nie udało się pobrać listy użytkowników");
        } finally {
            setLoading(false);
        }
    };

    // Автоматическое обновление при фокусе на экран
    useFocusEffect(
        useCallback(() => {
            load();
        }, [])
    );

    // НОВАЯ ФУНКЦИЯ ПОДТВЕРЖДЕНИЯ
    const handleToggleStatus = (id, userType, currentActive) => {
        const actionText = currentActive ? 'dezaktywować' : 'aktywować';

        Alert.alert(
            "Potwierdzenie", // Заголовок
            `Czy na pewno chcesz ${actionText} użytkownika ${id}?`, // Сообщение
            [
                {
                    text: "Anuluj",
                    style: "cancel"
                },
                {
                    text: "Tak",
                    onPress: async () => {
                        try {
                            await toggleActiveStatus(id, userType, !currentActive);
                            load(); // Обновляем список после успеха
                        } catch (e) {
                            Alert.alert("Błąd", "Nie udało się zmienić statusu");
                        }
                    }
                }
            ]
        );
    };

    const filtered = users.filter(u => u.id.toLowerCase().includes(filterId.toLowerCase()));

    const renderUser = ({ item }) => {
        const userType = item.clientType !== undefined ? 'clients' : (item.position !== undefined ? 'employees' : 'administrators');

        return (
            <View style={styles.card}>
                <View style={{flex:1}}>
                    <Text style={styles.idText}>{item.id}</Text>
                    <Text style={styles.name}>{item.firstName} {item.lastName} ({item.login})</Text>
                    <Text style={[styles.status, {color: item.active ? 'green' : 'red'}]}>
                        {item.active ? 'AKTYWNY' : 'NIEAKTYWNY'}
                    </Text>
                </View>
                <View style={styles.actions}>
                    {userType === 'clients' && (
                        <TouchableOpacity onPress={() => router.push(`/client/${item.id}`)} style={styles.btnInfo}>
                            <Text style={styles.btnText}>INFO</Text>
                        </TouchableOpacity>
                    )}
                    <TouchableOpacity
                        onPress={() => router.push({ pathname: `/user-edit/${item.id}`, params: { type: userType } })}
                        style={styles.btnEdit}
                    >
                        <Text style={styles.btnText}>EDYCJA</Text>
                    </TouchableOpacity>

                    {/* Кнопка с вызовом подтверждения */}
                    <TouchableOpacity
                        onPress={() => handleToggleStatus(item.id, userType, item.active)}
                        style={[styles.btnStatus, {backgroundColor: item.active ? '#ff4444' : '#00C851'}]}
                    >
                        <Text style={styles.btnText}>{item.active ? 'OFF' : 'ON'}</Text>
                    </TouchableOpacity>
                </View>
            </View>
        );
    };

    return (
        <View style={styles.container}>
            <TextInput
                placeholder="Filtruj po ID..."
                style={styles.input}
                value={filterId}
                onChangeText={setFilterId}
            />
            <TouchableOpacity style={styles.addBtn} onPress={() => router.push('/user-create')}>
                <Text style={styles.btnText}>+ DODAJ UŻYTKOWNIKA</Text>
            </TouchableOpacity>
            {loading ? (
                <ActivityIndicator size="large" color="#2196F3" />
            ) : (
                <FlatList
                    data={filtered}
                    renderItem={renderUser}
                    keyExtractor={item => item.id}
                    refreshing={loading}
                    onRefresh={load}
                />
            )}
        </View>
    );
}

const styles = StyleSheet.create({
    container: { flex: 1, padding: 10, backgroundColor: '#f5f5f5' },
    input: { backgroundColor: 'white', padding: 10, borderRadius: 5, marginBottom: 10, borderWidth: 1, borderColor: '#ccc' },
    card: { backgroundColor: 'white', padding: 15, borderRadius: 10, marginBottom: 10, elevation: 2, flexDirection: 'row' },
    idText: { fontSize: 10, color: '#888' },
    name: { fontSize: 16, fontWeight: 'bold' },
    status: { fontWeight: 'bold', fontSize: 12, marginTop: 4 },
    actions: { justifyContent: 'center', gap: 5 },
    btnInfo: { backgroundColor: '#2196F3', padding: 5, borderRadius: 4 },
    btnEdit: { backgroundColor: '#FFA000', padding: 5, borderRadius: 4 },
    btnStatus: { padding: 5, borderRadius: 4, minWidth: 40 },
    btnText: { color: 'white', fontWeight: 'bold', textAlign: 'center', fontSize: 12 },
    addBtn: { backgroundColor: '#4CAF50', padding: 15, borderRadius: 5, marginBottom: 10, alignItems: 'center' }
});