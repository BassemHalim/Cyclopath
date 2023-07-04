// import { ActivityIndicator, View } from "react-native";
import { SafeAreaProvider, SafeAreaView } from "react-native-safe-area-context";
import SignIn from "./src/screens/signIn";
import SignUp from "./src/screens/SignUp";
import Home from "./src/screens/Home";
import { User, UserProvider } from "./src/hooks/AuthContext";
import { NativeRouter, Route, Routes } from "react-router-native";
import { StatusBar } from "react-native";

export type StackParamList = {
  Home: undefined;
  SignIn: undefined;
  SignUp: undefined;
};

export default function App() {
  return (
    <SafeAreaProvider className="flex flex-col">
      <StatusBar />
      <UserProvider>
        <NativeRouter>
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/signup" element={<SignUp />} />
            <Route path="/signin" element={<SignIn />} />
          </Routes>
        </NativeRouter>
      </UserProvider>
    </SafeAreaProvider>
  );
}
