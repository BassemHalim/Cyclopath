import { useEffect, useState } from "react";
import { View, Text, Button, BackHandler } from "react-native";
import { useAuth } from "../hooks/AuthContext";
import AcitvityList from "../components/AcitvityList";
import Timeline from "../components/Timeline";
import Header from "../components/Header";

export default function Home({ navigation }) {
  return (
    <View className="bg-slate-950">
      <Header />
      <Timeline />
      <AcitvityList />
    </View>
  );
}
