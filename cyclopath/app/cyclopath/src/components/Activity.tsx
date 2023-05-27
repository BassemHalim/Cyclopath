import { Image, Text, View } from "react-native";
import { styles } from "../Style";

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

export default function Activity(props: { DTO: ActivityDTO; key: number }) {
  let DTO = props.DTO;
  const distanceInMiles: number = DTO.distance / 1609;
  const durationInHours: number = DTO.duration / 3600;
  const elevationGaininft: number = DTO.elevationGain * 3.281;
  return (
    <View style={styles.activityStats}>
      <View style={styles.activityStatsRow}>
        <Text style={styles.text}>{DTO.activityName}</Text>
      </View>
      <View style={styles.activityStatsRow}>
        <Text style={styles.text}>Calories: {DTO.calories.toFixed(2)}</Text>
        <Text style={styles.text}>Distance: {distanceInMiles.toFixed(2)}</Text>
      </View>
      <View style={styles.activityStatsRow}>
        <Text style={styles.text}>Duration: {durationInHours.toFixed(2)}</Text>
        <Text style={styles.text}>
          Elevation Gain: {elevationGaininft.toFixed(2)}
        </Text>
      </View>
      <Image
        source={require("../../assets/logo.png")}
        resizeMode="contain"
        style={styles.activityMap}
      />
    </View>
  );
}
