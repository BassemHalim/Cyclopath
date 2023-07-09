import { useCallback, useEffect, useState } from "react";
import { ActivityDTO } from "../types";

import { ScrollView } from "react-native-gesture-handler";
import { RefreshControl, FlatList, View } from "react-native";
import Activity, { Convert } from "../components/Activity";
import { useAuth } from "../hooks/AuthContext";
import axios from "axios";

const activityListURL: string =
  "http://192.168.1.245:8080/activity/activity-list?limit=3";

export default function AcitvityList() {
  const [refreshing, setRefreshing] = useState(false);
  const [activitites, setActivities] = useState<ActivityDTO[]>([]);
  const { token, isAuthenticated } = useAuth();

  const fetchData = async (garminSync: boolean, offsetAtivityID: number) => {
    let url = garminSync
      ? activityListURL + "&GarminSync=true"
      : activityListURL;
    url = url + `&start=${offsetAtivityID}`;
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
          console.log("updating activities " + activityList.length);
          if (garminSync) {
            setActivities(activityList);
          } else {
            setActivities([...activitites, ...activityList]);
          }
        })
        .catch((error) => {
          console.log(error);
        })
        .finally(() => setRefreshing(false));
    }
  };

  const fetchMoreActivities = async () => {
    console.log("reached end");
    const last: ActivityDTO = activitites[activitites.length - 1];
    fetchData(false, last.activityId);
  };
  useEffect(() => {
    fetchData(false, 0);
  }, []);

  const onRefresh = useCallback(async () => {
    setRefreshing(true);
    await fetchData(true, 0);
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
      keyExtractor={(item) => item.activityId}
      renderItem={renderActivity}
      initialNumToRender={2}
      onEndReached={fetchMoreActivities}
      contentContainerStyle={{ paddingBottom: 180 }}
    />
  );
}
