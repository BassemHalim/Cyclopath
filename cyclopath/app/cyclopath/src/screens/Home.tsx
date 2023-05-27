import { useCallback, useEffect, useState } from "react";
import {
  Button,
  Image,
  Text,
  TextInput,
  View,
  RefreshControl,
} from "react-native";
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
  //@TODO handle expired token
  var myHeaders = new Headers();
  myHeaders.append("Authorization", "Bearer " + token);
  var requestOptions = {
    method: "GET",
    headers: myHeaders,
    redirect: "follow",
  };

  try {
    const json: string = await fetch(activityListURL, requestOptions).then(
      function (response) {
        if (!response.ok) {
          throw Error(response.statusText);
        }
        return response.text();
      }
    );
    // handle error

    const activityList: ActivityDTO[] = Convert.toActivity(json);
    return activityList;
    // console.log(json);
    // return JSON.parse(json);
  } catch (e) {
    console.log("fetch error " + e);
  }
  return null;
}

export default function Home(props: { token: string }) {
  const insets = useSafeAreaInsets();
  const [refreshing, setRefreshing] = useState(false);

  // const [token, setToken] = useState<string>(props.token);
  let token = props.token;
  const [activitites, setActivities] = useState<ActivityDTO[]>([]);

  const fetchData = async () => {
    console.log("fetching activities");
    console.log(token);
    if (!token) {
      let tkn: string | null = await AsyncStorage.getItem("access_token");
      console.log("token! " + tkn);
      if (tkn) {
        //   setState({
        //     loggedIn: val
        // }, () => console.log(this.state.loggedIn));
        token = tkn;
      }
    }
    if (token) {
      let activityLst: ActivityDTO[] | null = await getActivityList(token);
      if (activityLst) {
        console.log("got activities");
        setActivities(activityLst);
      }
      setRefreshing(false);
    }
  };
  useEffect(() => {
    // handle infinite loop
    console.log("effect");
    if (token != "") {
      fetchData();
    }
  }, [token]);

  const onRefresh = useCallback(async () => {
    setRefreshing(true);
    await fetchData();
    setRefreshing(false);
  }, []);
  console.log(activitites);
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
