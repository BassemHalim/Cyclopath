import { useEffect, useState } from "react";
import { View, Text, Button, BackHandler } from "react-native";
import { Navigate, useNavigate } from "react-router";
import { styles } from "../Style";
import { useAuth } from "../hooks/AuthContext";
import AcitvityList from "../components/AcitvityList";
import Timeline from "../components/Timeline";

export default function Home() {
  const { isAuthenticated, logout } = useAuth();

  useEffect(() => {
    BackHandler.addEventListener("hardwareBackPress", () => {
      return true;
    });
    return () => {
      BackHandler.removeEventListener("hardwareBackPress", () => {
        useNavigate()(-1);
        return true;
      });
    };
  }, []);

  if (!isAuthenticated) {
    return <Navigate to="/signin" replace />;
  }

  return (
    <View style={[styles.home]}>
      <Button title="signout" onPress={logout} />
      <Timeline />
      <AcitvityList />
    </View>
  );
}
