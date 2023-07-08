import { ActivityIndicator, Image, Text, View } from "react-native";
import { ActivityDTO, Weather } from "../types";
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
    <View className="flex flex-col bg-gray-800 m-2 rounded-xl p-1 shadow-sm shadow-white">
      <View className="divide-y divide-gray-400">
        <View className="flex flex-row m-1 justify-around">
          <Text className="text-slate-100 font-bold text-base">
            {DTO.activityName}
          </Text>
          <Text className="text-slate-100 font-bold text-base">
            {formatDate(date)}
          </Text>
        </View>
        <View className="flex flex-row m-1 p-1 justify-around">
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
        <View className="flex flex-row m-1 p-1 justify-around">
          <Stat title="Calories" value={DTO.calories} units="" />
          <Stat title="Temperature" value={DTO.weather.temp} units="f" />
          <Stat title="Wind Speed" value={DTO.weather.windSpeed} units="mph" />
          <Stat
            title="Wind Dir."
            value={DTO.weather.windDirectionCompassPoint}
            units=""
          />
        </View>
      </View>

      <Image
        source={{
          uri: imageurl,
        }}
        className="h-52 w-4/5 my-2 self-center rounded-xl"
      />
    </View>
  );
}
