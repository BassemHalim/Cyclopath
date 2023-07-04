import { ActivityIndicator, View } from "react-native";
import { SafeAreaProvider } from "react-native-safe-area-context";
import SignIn from "./src/screens/signIn";
import SignUp from "./src/screens/SignUp";
import Home from "./src/screens/Home";
import { Suspense } from "react";
import { User, UserProvider } from "./src/hooks/AuthContext";
import { NativeRouter, Route, Routes } from "react-router-native";
export type StackParamList = {
  Home: undefined;
  SignIn: undefined;
  SignUp: undefined;
};

export default function App() {
  return (
    <UserProvider>
      <Suspense fallback={<ActivityIndicator />}>
        <SafeAreaProvider>
          <NativeRouter>
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/signup" element={<SignUp />} />
              <Route path="/signin" element={<SignIn />} />
            </Routes>
          </NativeRouter>
        </SafeAreaProvider>
      </Suspense>
    </UserProvider>
  );
}
