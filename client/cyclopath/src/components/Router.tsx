import React from "react";
import SignIn from "../screens/signin";
import SignUp from "../screens/signup";
import Home from "../screens/Home";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import { useAuth } from "../hooks/AuthContext";


const Stack = createNativeStackNavigator();

export default function Router() {
  const { isAuthenticated, token} = useAuth()
  return (
    <Stack.Navigator
      initialRouteName="signin"
      screenOptions={{ headerShown: false }}
    >
      {isAuthenticated ? (
        <Stack.Screen name="home" component={Home} />
      ) : (
        <>
          <Stack.Screen name="signup" component={SignUp} />
          <Stack.Screen name="signin" component={SignIn} />
        </>
      )}
    </Stack.Navigator>
  );
}