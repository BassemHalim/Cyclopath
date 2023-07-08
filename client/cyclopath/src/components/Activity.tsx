import { ActivityIndicator, Image, Text, View } from "react-native";
import { ActivityDTO, Weather } from "../types";
import { styles } from "../Style";
import { RegularText } from "./CustomText";
import { Suspense, useState, useEffect } from "react";
import { useAuth } from "../hooks/AuthContext";
import { Stat } from "./stat";

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

  const formattedToday = mm + "/" + dd + "/" + yyyy;
  return formattedToday;
};

export default function Activity(props: { DTO: ActivityDTO }) {
  const [imageurl, setImageurl] = useState("../../assets/media/samplemap.png");
  let { token } = useAuth();
  let DTO = props.DTO;
  const distanceInMiles: number = DTO.distance / 1609;
  const durationInHours: number = DTO.duration / 3600;
  const movingDuration: number = DTO.movingDuration / 3600;
  const averageSpeedMPH: number = DTO.averageSpeed * 2.237;
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
        const imageBlob = await response.blob();
        function blobToDataURL(blob: Blob, callback: any) {
          var a = new FileReader();
          a.onload = function (e) {
            if (e.target) callback(e.target.result);
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
          <RegularText style={styles.activityTitle}>
            {DTO.activityName}
          </RegularText>
          <RegularText style={styles.activityTitle}>
            {formatDate(date)}
          </RegularText>
        </View>
        <View style={styles.activityStatsRow}>
          <Stat
            title="Distance"
            value={distanceInMiles.toFixed(2)}
            units="mi"
          />
          <Stat
            title="Duration"
            value={durationInHours.toFixed(2)}
            units="hrs"
          />
          <Stat title="Speed" value={averageSpeedMPH.toFixed(2)} units="mph" />
          <Stat
            title="Elev. Gain"
            value={elevationGaininft.toFixed(2)}
            units="ft"
          />
        </View>
        <View style={styles.activityStatsRow}>
          <Stat title="Calories" value={DTO.calories} units="" />
          <Stat title="Temperature" value={DTO.weather.temp} units="f" />
          <Stat title="Wind speed" value={DTO.weather.windSpeed} units="mph" />
          <Stat
            title="Wind dir."
            value={DTO.weather.windDirectionCompassPoint}
            units=""
          />
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
