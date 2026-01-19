import { Tabs } from 'expo-router';
import { Ionicons } from '@expo/vector-icons';

export default function TabLayout() {
    return (
        <Tabs screenOptions={{
            tabBarActiveTintColor: '#2196F3',
            headerStyle: { backgroundColor: '#2196F3' },
            headerTintColor: '#fff',
        }}>
            <Tabs.Screen
                name="index"
                options={{
                    title: 'Użytkownicy',
                    tabBarLabel: 'Użytkownicy',
                    tabBarIcon: ({ color, size }) => (
                        <Ionicons name="people" size={size} color={color} />
                    ),
                }}
            />
            <Tabs.Screen
                name="rents"
                options={{
                    title: 'Alokacje',
                    tabBarLabel: 'Alokacje',
                    tabBarIcon: ({ color, size }) => (
                        <Ionicons name="key" size={size} color={color} />
                    ),
                }}
            />
        </Tabs>
    );
}