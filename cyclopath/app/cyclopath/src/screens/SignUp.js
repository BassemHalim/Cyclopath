import { Button, Image, StyleSheet, Text, TextInput, View } from "react-native";

import {
  SafeAreaProvider,
  useSafeAreaInsets,
} from "react-native-safe-area-context";

const onSignup = (props) => {
  console.warn("sign up");
  //@TODO handle signup
  props.navigation.navigate("SignIn");
};

export default function SignUp(navigation) {
  const insets = useSafeAreaInsets();

  return (
    // <View style={styles.container}>
    <View
      style={[
        styles.container,
        {
          paddingBottom: Math.max(insets.bottom, 16),
          paddingTop: Math.max(insets.top, 16),
        },
      ]}
    >
      <View style={styles.loginForm}>
        <Text style={styles.text}> username</Text>
        <TextInput
          inputMode="email"
          style={styles.inputBox}
          placeholder="  name@email.com"
        ></TextInput>
        <Text style={styles.text}> password</Text>
        <TextInput
          secureTextEntry={true}
          style={styles.inputBox}
          placeholder="  **********"
        ></TextInput>
        <Text style={styles.text}> re-enter password</Text>

        <TextInput
          secureTextEntry={true}
          style={styles.inputBox}
          placeholder="  **********"
        ></TextInput>
        <Button onPress={() => onSignup(navigation)} title="Sign Up" />
      </View>
    </View>
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
    flex: 1,
  },
  logo: {
    // flex: 1,
    width: "80%",
    height: 200,
    maxHeight: 200,
    maxWidth: 400,
    borderRadius: 10,
    margin: 30,
    marginTop: 45,
    marginBottom: 30,
  },
});
