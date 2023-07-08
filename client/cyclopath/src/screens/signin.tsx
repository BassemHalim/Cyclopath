import { Image, TextInput, View, Text, Pressable } from "react-native";
import { useEffect, useState } from "react";
import React from "react";
import { styles } from "../Style";
import { SafeAreaView } from "react-native-safe-area-context";
import { useAuth } from "../hooks/AuthContext";

export default function SignIn({ navigation }) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [emailValid, setEmailValid] = useState(true);
  const [passwordValid, setPasswordValid] = useState(true);
  const { login, isAuthenticated } = useAuth();

  const handleSubmit = async () => {
    const emailRegex =
      /^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$/;

    if (email.trim() === "" || !emailRegex.test(email)) {
      setEmailValid(false);
    } else if (password.trim() === "") {
      setPasswordValid(false);
    } else {
      await login(email, password);
    }
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
          onChangeText={(email) => {
            setEmail(email);
            setEmailValid(true);
          }}
        ></TextInput>
        {!emailValid && (
          <Text className="text-red-500 text-xs italic">
            Please enter a valid email address
          </Text>
        )}
        <Text className="ml-2  text-white text-lg">Password</Text>
        <TextInput
          secureTextEntry={true}
          className="bg-gray-300 rounded-xl m-1 p-1 px-1.5 w-72"
          placeholder="  **********"
          onChangeText={(password) => {
            setPassword(password);
            setPasswordValid(true);
          }}
        ></TextInput>
        {!passwordValid && (
          <Text className="text-red-500 text-xs italic">
            Please enter a valid password
          </Text>
        )}
        <Pressable
          className="bg-blue-600 rounded-xl m-2 w-40 p-2 self-center"
          onPress={handleSubmit}
        >
          <Text className="text-white text-center">sign in</Text>
        </Pressable>

        <Pressable onPress={() => navigation.navigate("signup")}>
          <Text className="text-white m-2">New here? create account</Text>
        </Pressable>
      </View>
    </SafeAreaView>
  );
}
