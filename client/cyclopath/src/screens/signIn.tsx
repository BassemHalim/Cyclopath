import { Image, TextInput, View, Text, Alert, Pressable } from "react-native";
import { useEffect, useState } from "react";
import AsyncStorage from "@react-native-async-storage/async-storage";
import React from "react";
import { styles } from "../Style";
import {
  SafeAreaView,
  useSafeAreaInsets,
} from "react-native-safe-area-context";
import { useAuth } from "../hooks/AuthContext";
import { Link, useNavigate } from "react-router-native";

const loginURL = "http://192.168.1.245:8080/auth/authenticate";

export const storeToken = (value: string) => {
  AsyncStorage.setItem("access_token", value)
    .then(() => console.log("stored token"))
    .catch((e) => console.error(e));
};

export default function SignIn() {
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
    <SafeAreaView className="flex-1 flex-col items-center bg-black text-white justify-center">
      <Image
        source={require("../../assets/media/logo.png")}
        resizeMode="contain"
        style={styles.logo}
      />
      <View className="flex flex-col w-auto">
        <Text className="ml-2 text-white text-lg">Username</Text>
        <TextInput
          inputMode="email"
          className="bg-gray-300 rounded-xl m-1 p-1 px-1.5 w-72"
          placeholder="  name@email.com"
          onChangeText={(email) => setEmail(email)}
        ></TextInput>
        <Text className="ml-2  text-white text-lg">Password</Text>
        <TextInput
          secureTextEntry={true}
          className="bg-gray-300 rounded-xl m-1 p-1 px-1.5 w-72"
          placeholder="  **********"
          onChangeText={(password) => setPassword(password)}
        ></TextInput>

        <Pressable
          className="bg-blue-600 rounded-xl m-2 w-40 p-2 self-center"
          onPress={onSignin}
        >
          <Text className="text-white text-center">sign in</Text>
        </Pressable>

        <Link to="/signup">
          <View>
            <Text className="text-white m-2">New here? create account</Text>
          </View>
        </Link>
      </View>
    </SafeAreaView>
  );
}
