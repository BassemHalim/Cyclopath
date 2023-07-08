// import { ActivityIndicator, View } from "react-native";
import { SafeAreaProvider, SafeAreaView } from "react-native-safe-area-context";

import { User, UserProvider, useAuth } from "./src/hooks/AuthContext";
import { StatusBar } from "react-native";
import { NavigationContainer } from "@react-navigation/native";
import Router from "./src/components/Router";

export type StackParamList = {
  Home: undefined;
  SignIn: undefined;
  SignUp: undefined;
};

export default function App() {
  return (
    <SafeAreaProvider className="flex flex-col">
      <StatusBar />
      <NavigationContainer>
        <UserProvider>
          <Router />
        </UserProvider>
      </NavigationContainer>
    </SafeAreaProvider>
  );
}
