import { Stack } from 'expo-router';

export default function RootLayout() {
    return (
        <Stack screenOptions={{
            headerStyle: { backgroundColor: '#2196F3' },
            headerTintColor: '#fff',
            headerTitleStyle: { fontWeight: 'bold' },
        }}>
            {/* Основное меню (внизу) */}
            <Stack.Screen name="(tabs)" options={{ headerShown: false }} />

            {/* Форма пользователя - откроется как модальное окно */}
            <Stack.Screen
                name="user-form"
                options={{
                    presentation: 'modal',
                    title: 'Nowy Użytkownik'
                }}
            />

            {/* Форма аренды */}
            <Stack.Screen
                name="rent-form"
                options={{
                    presentation: 'modal',
                    title: 'Nowa Alokacja'
                }}
            />

            {/* Детали клиента */}
            <Stack.Screen
                name="client/[id]"
                options={{
                    title: 'Szczegóły Klienta'
                }}
            />
        </Stack>
    );
}