export interface Props {
  navigation: any;
  token: string;
}
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
  weather: undefined;
}
