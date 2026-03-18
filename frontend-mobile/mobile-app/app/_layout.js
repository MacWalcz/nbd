import { Stack } from 'expo-router';

export default function RootLayout() {
    return (
        <Stack screenOptions={{
            headerStyle: { backgroundColor: '#2196F3' },
            headerTintColor: '#fff',
            headerTitleStyle: { fontWeight: 'bold' },
        }}>
            <Stack.Screen name="(tabs)" options={{ headerShown: false }} />

            <Stack.Screen
                name="user-create"
                options={{
                    presentation: 'modal',
                    title: 'Nowy Użytkownik'
                }}
            />

            <Stack.Screen
                name="user-edit/[id]"
                options={{
                    presentation: 'modal',
                    title: 'Edytuj Użytkownika'
                }}
            />

            <Stack.Screen
                name="rent-form"
                options={{
                    presentation: 'modal',
                    title: 'Nowa Alokacja'
                }}
            />

            <Stack.Screen
                name="clientEnt/[id]"
                options={{
                    title: 'Szczegóły Klienta'
                }}
            />
        </Stack>
    );
}