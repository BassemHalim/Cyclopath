import { useEffect, useState } from "react";
import { Button, Image, StyleSheet, Text, TextInput, View } from "react-native";
import { Props } from "../types";
import AsyncStorage from "@react-native-async-storage/async-storage";
import { ScrollView } from "react-native-gesture-handler";
import Activity, { ActivityDTO, Convert } from "../components/Activity";
import { styles } from "../Style";
import { useSafeAreaInsets } from "react-native-safe-area-context";
const activityListURL: string =
  "http://192.168.1.245:8080/activity/activity-list?limit=50";

async function getActivityList(token: string): Promise<ActivityDTO[] | null> {
  if (token == "") return null;
  var myHeaders = new Headers();
  myHeaders.append("Authorization", "Bearer " + token);
  var requestOptions = {
    method: "GET",
    headers: myHeaders,
    redirect: "follow",
  };

  try {
    const json: string = await fetch(activityListURL, requestOptions).then(
      (response) => response.text()
    );
    const activityList: ActivityDTO[] = Convert.toActivity(json);
    return activityList;
    // console.log(json);
    // return JSON.parse(json);
  } catch (e) {
    console.error("fetch error " + e);
  }
  return null;
}
export default function Home(props: { token: string }) {
  const insets = useSafeAreaInsets();

  const [token, setToken] = useState<string>(props.token);
  const [activitites, setActivities] = useState<ActivityDTO[]>([]);

  useEffect(() => {
    // handle infinite loop
    console.log("effect, token: " + token);
    if (token != "") {
      const fetchData = async () => {
        if (token != "") {
          let tkn = await AsyncStorage.getItem("access_token");
          if (tkn) {
            setToken(tkn);
          }
        }
        if (token) {
          let activityLst: ActivityDTO[] | null = await getActivityList(token);
          if (activityLst) setActivities(activityLst);
        }
      };
      fetchData();
    }
  }, [token]);

  return (
    <ScrollView
      style={[
        styles.home,
        {
          paddingBottom: Math.max(insets.bottom, 16),
          paddingTop: Math.max(insets.top, 16),
        },
      ]}
    >
      {activitites.map((item, index) => (
        <Activity DTO={item} key={index} />
      ))}
    </ScrollView>
  );
}
