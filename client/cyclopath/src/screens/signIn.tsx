import {
  Button,
  Image,
  StyleSheet,
  TextInput,
  View,
  Text,
  Alert,
} from "react-native";
import { useEffect, useState } from "react";
import AsyncStorage from "@react-native-async-storage/async-storage";
import React from "react";
import { styles } from "../Style";
import { useSafeAreaInsets } from "react-native-safe-area-context";
import { RegularText } from "../components/CustomText";
import { useAuth } from "../hooks/AuthContext";
import { Link, useNavigate } from "react-router-native";

const loginURL = "http://192.168.1.245:8080/auth/authenticate";

export const storeToken = (value: string) => {
  AsyncStorage.setItem("access_token", value)
    .then(() => console.log("stored token"))
    .catch((e) => console.error(e));
};

export default function SignIn() {
  const insets = useSafeAreaInsets();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const { token, setToken } = useAuth();
  const navigate = useNavigate();
  useEffect(() => {
    if (!token) {
      const fetchData = async () => {
        let tkn = await AsyncStorage.getItem("access_token");
        if (tkn) setToken(tkn);
      };
      fetchData();
    }
    if (token) {
      navigate("/");
    }
  }, [token]);

  const onSignin = () => {
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
          console.log(data);
          return;
        }
        storeToken(data.token);
        setToken(data.token);
        navigate("/");
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
      />
      <View style={styles.loginForm}>
        <RegularText style={{ maxHeight: 20, alignSelf: "flex-start" }}>
          username
        </RegularText>
        <TextInput
          inputMode="email"
          style={styles.inputBox}
          placeholder="  name@email.com"
          onChangeText={(email) => setEmail(email)}
        ></TextInput>
        <RegularText style={{ maxHeight: 20, alignSelf: "flex-start" }}>
          password
        </RegularText>
        <TextInput
          secureTextEntry={true}
          style={styles.inputBox}
          placeholder="  **********"
          onChangeText={(password) => setPassword(password)}
        ></TextInput>
        <Button onPress={onSignin} title="sign in" />
        <Link to="/signup">
          <View>
            <Text className="text-white">New here? Sign up</Text>
          </View>
        </Link>
      </View>
    </View>
  );
}
