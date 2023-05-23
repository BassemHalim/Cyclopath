import { StyleSheet } from "react-native";
import { SafeAreaProvider } from "react-native-safe-area-context";
import SignIn from "./src/screens/signIn";
import SignUp from "./src/screens/SignUp";
import Home from "./src/screens/Home";
import { NavigationContainer } from "@react-navigation/native";
import { createNativeStackNavigator } from "@react-navigation/native-stack";

const Stack = createNativeStackNavigator();

export default function App() {
  return (
    <SafeAreaProvider>
      <NavigationContainer>
        <Stack.Navigator
          initialRouteName="SignIn"
          screenOptions={{ headerShown: false }}
        >
          <Stack.Screen name="SignUp" component={SignUp} />
          <Stack.Screen name="SignIn" component={SignIn} />
          <Stack.Screen name="Home" component={Home} />

        </Stack.Navigator>
      </NavigationContainer>
    </SafeAreaProvider>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: "column",
    backgroundColor: "#000",
    alignItems: "center",
    justifyContent: "center",
  },
  headerText: {
    color: "#fff",
    fontSize: 30,
    fontWeight: "bold",
    margin: 10,
  },
  text: {
    color: "#fff",
    fontSize: 16,
    fontWeight: "400",
  },
  smallText: {
    color: "#fff",
    fontSize: 14,
    margin: 20,
  },
  inputBox: {
    color: "#fff",
    backgroundColor: "grey",
    fontSize: 15,
    width: 300,
    marginVertical: 10,
    borderRadius: 7,
  },
  loginForm: {
    borderColor: "#fff",
    margin: 5,
  },
  logo: {
    width: "80%",
    height: "30%",
    maxHeight: 200,
    borderRadius: 10,
    margin: 30,
    marginTop: 45,
    marginBottom: 30,
  },
});
