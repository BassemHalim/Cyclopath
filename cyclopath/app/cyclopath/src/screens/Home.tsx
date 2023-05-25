import { useEffect, useState } from "react";
import { Button, Image, StyleSheet, Text, TextInput, View } from "react-native";
import { Props } from "../types";
import AsyncStorage from "@react-native-async-storage/async-storage";
import { ScrollView } from "react-native-gesture-handler";
import { Activity, Convert } from "../components/Activity";

const activityListURL: string =
  "http://192.168.1.245:8080/activity/activity-list?limit=50";

async function getActivityList(token: string): Promise<Activity[] | null> {
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
    const activityList: Activity[] = Convert.toActivity(json);
    return activityList;
    // console.log(json);
    // return JSON.parse(json);
  } catch (e) {
    console.error("fetch error " + e);
  }
  return null;
}
export default function Home() {
  const [token, setToken] = useState<string>("");
  const [activitites, setActivities] = useState<Activity[]>([]);

  useEffect(() => {
    const fetchData = async () => {
      let tkn = await AsyncStorage.getItem("access_token");
      if (tkn) {
        setToken(tkn);
        let activityLst: Activity[] | null = await getActivityList(token);
        if (activityLst) setActivities(activityLst);
      }
    };
    fetchData();
  }, [token]);

  return (
    <ScrollView>
      <Text> Welcome Home</Text>
      {activitites.map((item, index) => (
        <Text key={index}>
          Name:{item.activityName}
          Distance: {item.distance}
          Duration: {item.duration}
        </Text>
      ))}
    </ScrollView>
  );
}

interface ActivityDTO {
  activityId: number;
  activityName: string;
  averageHR: number;
  averageSpeed: number;
  bmrCalories: number;
  calories: number;
  distance: number;
  duration: number;
  elapsedDuration: number;
  elevationCorrected: boolean;
  elevationGain: number;
  elevationLoss: number;
  endLatitude: number;
  endLongitude: number;
  locationName: string;
  maxElevation: number;
  maxHR: number;
  maxSpeed: number;
  maxVerticalSpeed: number;
  minActivityLapDuration: number;
  minElevation: number;
  movingDuration: number;
  startLatitude: number;
  startLongitude: number;
  startTimeGMT: string;
  startTimeLocal: string;
  timeZoneId: number;
}
