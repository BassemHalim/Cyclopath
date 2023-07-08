import { useEffect, useState } from "react";
import { View, Text, Button, BackHandler } from "react-native";
import { styles } from "../Style";
import { useAuth } from "../hooks/AuthContext";
import AcitvityList from "../components/AcitvityList";
import Timeline from "../components/Timeline";

export default function Home({ navigation }) {
  const { logout} = useAuth();
  
  return (
    <View style={[styles.home]}>
      <Button title="signout" onPress={logout} />
      <Timeline />
      <AcitvityList />
    </View>
  );
}
