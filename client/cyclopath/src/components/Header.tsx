import { Button, Pressable, View } from "react-native";
import { useAuth } from "../hooks/AuthContext";
import Ionicons from "@expo/vector-icons/Ionicons";

export default function Header() {
  const { logout } = useAuth();

  return (
    <View className="flex flex-row justify-between">
      <Ionicons
        name="menu-outline"
        size={32}
        color="grey"
        // onPress={logout}
      />
      <Ionicons
        name="log-out-outline"
        size={32}
        color="grey"
        onPress={logout}
      />
    </View>
  );
}
