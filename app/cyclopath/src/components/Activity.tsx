import { ActivityIndicator, Image, Text, View } from "react-native";

import { styles } from "../Style";
import RegularText from "./CustomText";
import { Suspense, useState, useEffect } from "react";
import { useAuth } from "../hooks/AuthContext";
import * as FileSystem from "expo-file-system";
import { resolveTripleslashReference } from "typescript";

export interface ActivityDTO {
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
  startTimeGMT: Date;
  startTimeLocal: Date;
  timeZoneId: number;
  weather: null;
}

export class Convert {
  public static toActivity(json: string): ActivityDTO[] {
    return JSON.parse(json);
  }

  public static activityToJson(value: ActivityDTO[]): string {
    return JSON.stringify(value);
  }
}

const formatDate = (date: Date): string => {
  const yyyy = date.getFullYear();
  let mm = date.getMonth() + 1; // Months start at 0!
  let dd = date.getDate();

  const formattedToday = dd + "/" + mm + "/" + yyyy;
  return formattedToday;
};

export default function Activity(props: { DTO: ActivityDTO; key: number }) {
  const [imageurl, setImageurl] = useState("../../assets/media/samplemap.png");
  let { token, setToken } = useAuth();
  let DTO = props.DTO;
  const distanceInMiles: number = DTO.distance / 1609;
  const durationInHours: number = DTO.duration / 3600;
  const elevationGaininft: number = DTO.elevationGain * 3.281;
  const date = new Date(DTO.startTimeLocal);
  const mapurl: string = `http://192.168.1.245:8080/activity/${DTO.activityId}/map`;

  useEffect(() => {
    const fetchImageWithRedirect = async () => {
      if (!token) {
        return;
      }
      try {
        var myHeaders = new Headers();
        myHeaders.append("Authorization", "Bearer " + token);

        var requestOptions = {
          method: "GET",
          headers: myHeaders,
          redirect: "follow",
        };

        const response = await fetch(mapurl, requestOptions);

        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        console.log('fetched image')
        const imageBlob = await response.blob();
        function blobToDataURL(blob: Blob, callback: any) {
          var a = new FileReader();
          a.onload = function (e) {
            callback(e.target.result);
          };
          a.readAsDataURL(blob);
        }
        blobToDataURL(imageBlob, function (dataurl: string) {
          setImageurl(dataurl);
        });
      } catch (error) {
        console.error("Error fetching image:", error);
      }
    };
    fetchImageWithRedirect();
  }, []);

  return (
    <View style={styles.activityStats}>
      <View style={{ flex: 1 }}>
        <View style={styles.activityStatsRow}>
          <RegularText>{DTO.activityName}</RegularText>
          <RegularText> {formatDate(date)} </RegularText>
        </View>
        <View style={styles.activityStatsRow}>
          <RegularText>Calories: {DTO.calories.toFixed(2)}</RegularText>
          <RegularText>Distance: {distanceInMiles.toFixed(2)}</RegularText>
        </View>
        <View style={styles.activityStatsRow}>
          <RegularText>Duration: {durationInHours.toFixed(2)}</RegularText>
          <RegularText>
            Elevation Gain: {elevationGaininft.toFixed(2)}
          </RegularText>
        </View>
      </View>
      <Suspense fallback={<ActivityIndicator />}>
        <View style={{ flex: 2 }}>
          <Image
            // source={require("../../assets/media/samplemap.png")}
            // resizeMode="center"
            source={{
              uri: imageurl,
            }}
            style={styles.activityMap}
          />
        </View>
      </Suspense>
    </View>
  );
}
