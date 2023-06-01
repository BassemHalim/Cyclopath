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
// import { Props } from "../types";
import { StackParamList } from "../../App";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import RegularText from "../components/CustomText";
import { useAuth } from "../hooks/AuthContext";

const loginURL = "http://192.168.1.245:8080/auth/authenticate";

type Props = NativeStackScreenProps<StackParamList, "SignIn">;

export const storeToken = (value: string) => {
  AsyncStorage.setItem("access_token", value)
    .then(() => console.log("stored token"))
    .catch((e) => console.error(e));
};

export default function SignIn({ navigation }: Props) {
  const insets = useSafeAreaInsets();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  // const [token, setToken] = useState<string>("");
  const { token, setToken } = useAuth();

  useEffect(() => {
    if (!token) {
      const fetchData = async () => {
        let tkn = await AsyncStorage.getItem("access_token");
        if (tkn) setToken(tkn);
      };
      fetchData();
    }
    console.log(token);
    if (token) {
      console.log("got token");

      navigation.navigate("Home");
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
        if (!data.token) {
          Alert.alert("credentials incorrect");
          return;
        }
        storeToken(data.token);
        setToken(data.token);
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
        source={require("../../assets/media/logo.png")}
        resizeMode="contain"
        style={styles.logo}
      ></Image>
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
