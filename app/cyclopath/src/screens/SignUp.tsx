import { Button, TextInput, View, Alert } from "react-native";
import { useEffect, useState } from "react";
import { useAuth } from "../hooks/AuthContext";

import { styles } from "../Style";
// import { Props } from "../types";
import { StackParamList } from "../../App";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { RegularText } from "../components/CustomText";
import { useSafeAreaInsets } from "react-native-safe-area-context";
import { storeToken } from "./signIn";

const registerURL = "http://192.168.1.245:8080/auth/register";

type Props = NativeStackScreenProps<StackParamList, "SignUp">;

export default function SignUp({ route, navigation }: Props) {
  const insets = useSafeAreaInsets();
  const [email, setEmail] = useState("");
  const [password1, setPassword1] = useState("");
  const [password2, setPassword2] = useState("");

  const { token, setToken } = useAuth();

  const onSignUp = () => {
    if (!email || !password1 || !password2) {
      console.log(email, password1, password2)
      Alert.alert("please fill all fields");
      return;
    }
    if (password1 !== password2) {
      Alert.alert("passwords don't match");
      return;
    }
    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    var raw = JSON.stringify({
      email: email,
      password: password1,
    });

    var requestOptions = {
      method: "POST",
      headers: myHeaders,
      body: raw,
      redirect: "follow",
    };

    fetch(registerURL, requestOptions)
      .then((response) => response.json())
      .then((data) => {
        if (!data.token) {
          Alert.alert("credentials incorrect");
          console.log(data);
          return;
        }
        storeToken(data.token);
        setToken(data.token);
        navigation.navigate("Home");
      })
      .catch((error) => console.log("error", error));
  };
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
          onChangeText={(email) => setEmail(email)}
        ></TextInput>
        <RegularText> password</RegularText>
        <TextInput
          secureTextEntry={true}
          style={styles.inputBox}
          placeholder="  **********"
          onChangeText={(password) => setPassword1(password)}
        ></TextInput>
        <RegularText> re-enter password</RegularText>
        <TextInput
          secureTextEntry={true}
          style={styles.inputBox}
          placeholder="  **********"
          onChangeText={(password) => setPassword2(password)}
        ></TextInput>
        <Button onPress={() => onSignUp()} title="Sign Up" />
      </View>
    </View>
  );
}
