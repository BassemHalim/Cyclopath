import { TextInput, View, Alert, Pressable, Text } from "react-native";
import { useState } from "react";
import { useAuth } from "../hooks/AuthContext";

import { SafeAreaView } from "react-native-safe-area-context";
import { storeToken } from "./signIn";
import { useNavigate } from "react-router-native";

const registerURL = "http://192.168.1.245:8080/auth/register";

export default function SignUp() {
  const [email, setEmail] = useState("");
  const [password1, setPassword1] = useState("");
  const [password2, setPassword2] = useState("");
  const navigate = useNavigate();
  const { token, setToken } = useAuth();

  const onSignUp = () => {
    if (!email || !password1 || !password2) {
      console.log(email, password1, password2);
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
        navigate("/");
      })
      .catch((error) => console.log("error", error));
  };
  return (
    <SafeAreaView className="flex-1 flex-col items-center bg-black text-white justify-center">
      <View className="flex flex-col w-auto">
        <Text className="text-white text-lg"> Username</Text>
        <TextInput
          inputMode="email"
          className="bg-gray-300 rounded-xl m-1 p-1 px-1.5 w-72"
          placeholder="  name@email.com"
          onChangeText={(email) => setEmail(email)}
        ></TextInput>
        <Text className="text-white text-lg"> Password</Text>
        <TextInput
          secureTextEntry={true}
          className="bg-gray-300 rounded-xl m-1 p-1 px-1.5 w-72"
          placeholder="  **********"
          onChangeText={(password) => setPassword1(password)}
        ></TextInput>
        <Text className="text-white text-lg"> re-enter password</Text>
        <TextInput
          secureTextEntry={true}
          className="bg-gray-300 rounded-xl m-1 p-1 px-1.5 w-72"
          placeholder="  **********"
          onChangeText={(password) => setPassword2(password)}
        ></TextInput>
        <Pressable
          className="bg-blue-600 rounded-xl m-2 w-40 p-2 self-center"
          onPress={onSignUp}
        >
          <Text className="text-white text-center">sign up</Text>
        </Pressable>
      </View>
    </SafeAreaView>
  );
}
