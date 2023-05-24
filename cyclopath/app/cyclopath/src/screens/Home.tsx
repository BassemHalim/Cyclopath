import { useEffect, useState} from "react";
import { getToken } from "./signIn";
import { Button, Image, StyleSheet, Text, TextInput, View } from "react-native";
import { Props } from "../types";
import AsyncStorage from "@react-native-async-storage/async-storage";

export default function Home() {
  const [token, setToken] = useState<string | null>(null)
  useEffect(() => {
    const fetchData = async () => {
      let tkn = await AsyncStorage.getItem("access_token");
      setToken(tkn)
    };

    fetchData();
  }, [token]);
  return (
    <View>
      <Text> Welcome Home</Text>
      <Text>Token: {token} </Text>
    </View>
  );
}
