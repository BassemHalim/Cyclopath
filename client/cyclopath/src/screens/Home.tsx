import { useEffect, useState } from "react";
import { View, Text, Button, BackHandler } from "react-native";
import { useAuth } from "../hooks/AuthContext";
import AcitvityList from "../components/AcitvityList";
import Timeline from "../components/Timeline";

export default function Home({ navigation }) {
  const { logout} = useAuth();
  
  return (
    <View className="bg-slate-950">
      <Button title="signout" onPress={logout} />
      <Timeline />
      <AcitvityList />
    </View>
  );
}
