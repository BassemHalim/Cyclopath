import {
  Button,
  Image,
  StyleSheet,
  Text,
  TextInput,
  View,
  Alert,
} from "react-native";
import { useState } from "react";
import AsyncStorage from "@react-native-async-storage/async-storage";
import React from "react";
import { styles } from "../Style";
import {
  SafeAreaProvider,
  useSafeAreaInsets,
} from "react-native-safe-area-context";
import { Props } from "../types";

const loginURL = "http://192.168.1.245:8080/auth/authenticate";

export const storeToken = (value: string) => {
  AsyncStorage.setItem("access_token", value).catch((e) => console.error(e));

  // try {
  //   await AsyncStorage.setItem("access_token", value);
  // } catch (e) {
  //   console.error(e);
  // }
};

export const  getToken = (): string | null => {
  AsyncStorage.getItem("access_token")
    .then((response) => {
      console.log(response)
      return response;
    })
    .catch((e) => console.error(e));

  // try {
  // const value: string | null = await AsyncStorage.getItem("access_token").then().catch(e => console.error(e));
  //   if (value !== null) {
  //     return value;
  //   }
  // } catch (e) {
  //   console.error(e);
  // }
  // return null;
  return null;
};

export default function SignIn({ navigation }: Props) {
  const insets = useSafeAreaInsets();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const onSignin = () => {
    //@TODO handle signup
    if (!email || !password) {
      Alert.alert("please enter credentials");
      return;
    }
    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    var raw = JSON.stringify({
      email: email,
      password: password,
    });

    var requestOptions = {
      method: "POST",
      headers: myHeaders,
      body: raw,
      redirect: "follow",
    };

    fetch(loginURL, requestOptions)
      .then((response) => response.json())
      .then((data) => {
        const token = data.token;
        storeToken(token);
        navigation.navigate("Home");
      })
      .catch((error) => console.log("error", error));
  };

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
          onChangeText={(email) => setEmail(email)}
        ></TextInput>
        <Text style={styles.text}> password</Text>
        <TextInput
          secureTextEntry={true}
          style={styles.inputBox}
          placeholder="  **********"
          onChangeText={(password) => setPassword(password)}
        ></TextInput>
        <Button onPress={onSignin} title="sign in" />
        <Text
          style={styles.smallText}
          onPress={() => navigation.navigate("SignUp")}
        >
          New here? Sign up
        </Text>
      </View>
    </View>
  );
}