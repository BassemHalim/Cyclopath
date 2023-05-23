import { Button, Image, StyleSheet, Text, TextInput, View } from "react-native";

import {
  SafeAreaProvider,
  useSafeAreaInsets,
} from "react-native-safe-area-context";

const onSignin = (navigation) => {
  console.warn("sign in");
  //@TODO handle signup
  navigation.navigate("Home");
};


export default function SignIn({ navigation }) {
  const insets = useSafeAreaInsets();

  return (
    <View
      style={[
        styles.container,
        {
          paddingBottom: Math.max(insets.bottom, 16),
          paddingTop: Math.max(insets.top, 16),
        },
      ]}
    >
      <Image
        source={require("../../assets/logo.png")}
        resizeMode="contain"
        style={styles.logo}
      ></Image>
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
        <Button onPress={() => onSignin(navigation)} title="sign in" />
        <Text style={styles.smallText} onPress={() => navigation.navigate('SignUp')}>
          New here? Sign up
        </Text>
      </View>
    </View>
  );
}
// HomeScreen.prototype = {
//     navigation: PropTypes.object.isRequired
// }

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
