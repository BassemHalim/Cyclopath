import { Button, Image, StyleSheet, Text, TextInput, View } from "react-native";
import { styles } from "../Style";
import { Props } from "../types";
import RegularText from "../components/CustomText";
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
        <RegularText> username</RegularText>
        <TextInput
          inputMode="email"
          style={styles.inputBox}
          placeholder="  name@email.com"
        ></TextInput>
        <RegularText> password</RegularText>
        <TextInput
          secureTextEntry={true}
          style={styles.inputBox}
          placeholder="  **********"
        ></TextInput>
        <RegularText> re-enter password</RegularText>

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
