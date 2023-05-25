import {
  Button,
  Image,
  StyleSheet,
  Text,
  TextInput,
  View,
  Alert,
} from "react-native";
import { useEffect, useState } from "react";
import AsyncStorage from "@react-native-async-storage/async-storage";
import React from "react";
import { styles } from "../Style";
import { useSafeAreaInsets } from "react-native-safe-area-context";
import { Props } from "../types";

const loginURL = "http://192.168.1.245:8080/auth/authenticate";

export const storeToken = (value: string) => {
  AsyncStorage.setItem("access_token", value)
    .then(() => console.log("stored token"))
    .catch((e) => console.error(e));
};

export default function SignIn({ navigation }: Props) {
  const insets = useSafeAreaInsets();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [token, setToken] = useState<string>("");

  useEffect(() => {
    // handle infinite loop
    if (!token) {
      const fetchData = async () => {
        let tkn = await AsyncStorage.getItem("access_token");
        if (tkn) setToken(tkn);
      };
      fetchData();
    }
    if (token !== "") {
      console.log("got token");
      navigation.navigate("Home", token);
    }
  }, [token]);

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
        if (!token) {
          Alert.alert("credentials incorrect");
          return;
        }
        storeToken(token);
        navigation.navigate("Home", token);
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
