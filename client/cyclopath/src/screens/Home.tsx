import { Suspense, useCallback, useEffect, useState } from "react";
import { View, RefreshControl, ActivityIndicator } from "react-native";
import { Navigate } from "react-router";
import { ScrollView } from "react-native-gesture-handler";
import Activity, { Convert } from "../components/Activity";
import { ActivityDTO } from "../types";
import { styles } from "../Style";
import {
  SafeAreaProvider,
  SafeAreaView,
  useSafeAreaInsets,
} from "react-native-safe-area-context";
import { StatusBar } from "expo-status-bar";
import { useAuth } from "../hooks/AuthContext";

const activityListURL: string =
  "http://192.168.1.245:8080/activity/activity-list?limit=2";

async function getActivityList(
  token: string,
  garminSync: boolean
): Promise<ActivityDTO[] | null> {
  if (token == "") return null;
  //@TODO handle expired token
  var myHeaders = new Headers();
  myHeaders.append("Authorization", "Bearer " + token);
  var requestOptions = {
    method: "GET",
    headers: myHeaders,
    redirect: "follow",
  };

  try {
    const url = garminSync
      ? activityListURL + "&GarminSync=true"
      : activityListURL;
    const json: string = await fetch(url, requestOptions).then(function (
      response
    ) {
      if (!response.ok) {
        throw Error(response.statusText);
      }
      return response.text();
    });
    // handle error

    const activityList: ActivityDTO[] = Convert.toActivity(json);
    return activityList;
  } catch (e) {
    console.log("fetch error " + e);
  }
  return null;
}

export default function Home() {
  const insets = useSafeAreaInsets();
  const [refreshing, setRefreshing] = useState(false);
  const { token, setToken } = useAuth();
  if (!token) {
    return <Navigate to="/signin" replace />;
  }
  const [activitites, setActivities] = useState<ActivityDTO[]>([]);

  const fetchData = async (garminSync: boolean) => {
    if (token) {
      let activityLst: ActivityDTO[] | null = await getActivityList(
        token,
        garminSync
      );
      if (activityLst) {
        setActivities(activityLst);
      }
      setRefreshing(false);
    }
  };

  useEffect(() => {
    // handle infinite loop
    if (token) {
      fetchData(false);
    }
  }, [token]);

  const onRefresh = useCallback(async () => {
    setRefreshing(true);
    await fetchData(true);
    setRefreshing(false);
  }, []);

  return (
    <View
      style={[
        styles.home,
        {
          paddingBottom: Math.max(insets.bottom, 16),
          paddingTop: Math.max(insets.top, 16),
        },
      ]}
    >
      <StatusBar translucent={true} hidden={false} />
      <ScrollView
        refreshControl={
          <RefreshControl refreshing={refreshing} onRefresh={onRefresh} />
        }
      >
        {activitites.map((item, index) => (
          <Activity DTO={item} key={index} />
        ))}
      </ScrollView>
    </View>
  );
}
