import { Button, Image, StyleSheet, Text, TextInput, View } from "react-native";
import { styles } from "../Style";
import { Props } from "../types";

import {
  SafeAreaProvider,
  useSafeAreaInsets,
} from "react-native-safe-area-context";

const onSignup = (props: Props) => {
  console.warn("sign up");
  //@TODO handle signup
  props.navigation.navigate("SignIn");
};

export default function SignUp({ navigation }: Props) {
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

