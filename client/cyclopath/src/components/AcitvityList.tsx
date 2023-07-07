import { useCallback, useEffect, useState } from "react";
import { ActivityDTO } from "../types";

import { ScrollView } from "react-native-gesture-handler";
import { RefreshControl, FlatList } from "react-native";
import Activity, { Convert } from "../components/Activity";
import { useAuth } from "../hooks/AuthContext";
import axios from "axios";

const activityListURL: string =
  "http://192.168.1.245:8080/activity/activity-list?limit=4";

export default function AcitvityList() {
  const [refreshing, setRefreshing] = useState(false);
  const [activitites, setActivities] = useState<ActivityDTO[]>([]);
  const { token, isAuthenticated } = useAuth();

  const fetchData = async (garminSync: boolean) => {
    let url = garminSync
      ? activityListURL + "&GarminSync=true"
      : activityListURL;
    if (isAuthenticated) {
      let config = {
        method: "get",
        maxBodyLength: Infinity,
        url: url,

        headers: {
          Authorization: "Bearer " + token,
        },
      };

      axios
        .request(config)
        .then((response) => {
          const activityList: ActivityDTO[] = response.data;
          setActivities(activityList);
        })
        .catch((error) => {
          console.log(error);
        });
    }
    setRefreshing(false);
  };

  useEffect(() => {
    fetchData(false);
  }, []);

  const onRefresh = useCallback(async () => {
    setRefreshing(true);
    await fetchData(true);
    setRefreshing(false);
  }, []);
  const renderActivity = ({ item, index }) => {
    return <Activity DTO={item} key={index} />;
  };
  return (
    <FlatList
      refreshControl={
        <RefreshControl refreshing={refreshing} onRefresh={onRefresh} />
      }
      data={activitites}
      renderItem={renderActivity}
      initialNumToRender={2}
      onEndReached={() => console.log("reached end")}
    />
  );
}
