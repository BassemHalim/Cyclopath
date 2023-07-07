import { ScrollView, View } from "react-native";
import { useEffect, useState } from "react";
import Day from "./Day";
import axios from "axios";
import { useAuth } from "../hooks/AuthContext";

const STATS_URL: string = "http://192.168.1.245:8080/activity/stats";

export default function Timeline() {
  const [Stats, setStats] = useState<Array<number>>([]);
  const { isAuthenticated, token } = useAuth();
  const fetchData = async (garminSync: boolean) => {
    if (isAuthenticated) {
      let config = {
        method: "get",
        maxBodyLength: Infinity,
        url: STATS_URL,
        headers: {
          Authorization: "Bearer " + token,
        },
      };

      axios
        .request(config)
        .then((response) => {
          setStats(response.data);
        })
        .catch((error) => {
          console.log(error);
        });
    }
  };

  useEffect(() => {
    fetchData(false);
  }, []);

  return (
    <ScrollView horizontal>
      <View className="flex flex-col flex-wrap h-28 m-1">
        {Stats.slice(0, 365).map((item, index) => (
          <Day key={index} value={item} />
        ))}
      </View>
    </ScrollView>
  );
}
