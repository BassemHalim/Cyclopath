import { TextInput, View, Alert, Pressable, Text } from "react-native";
import { useState } from "react";
import { useAuth } from "../hooks/AuthContext";

import { SafeAreaView } from "react-native-safe-area-context";

export default function SignUp({ navigation }) {
  const [email, setEmail] = useState("");
  const [password1, setPassword1] = useState("");
  const [password2, setPassword2] = useState("");
  const [error, setError] = useState<string | null>(null);
  const { isAuthenticated, register } = useAuth();

  const handleSubmit = async () => {
    const emailRegex =
      /^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$/;

    if (!email || !password1 || !password2) {
      setError("please fill all fields");
      return;
    }
    if (!emailRegex.test(email)) {
      setError("please enter a valid email");
      return;
    }
    if (password1 !== password2) {
      setError("passwords don't match");
      return;
    }

    await register(email, password1);
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
        {error && <Text className="text-red-500 text-xs italic">{error}</Text>}
        <Pressable
          className="bg-blue-600 rounded-xl m-2 w-40 p-2 self-center"
          onPress={handleSubmit}
        >
          <Text className="text-white text-center">sign up</Text>
        </Pressable>
      </View>
    </SafeAreaView>
  );
}
