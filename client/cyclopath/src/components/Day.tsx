import { View } from "react-native";
export default function Day({ value }) {
  const bgColor = getColor(value);
  return <View className={"flex rounded-sm w-2 h-2 m-0.5 " + bgColor} />;
}

function getColor(value: number) {
  const decade = 16093; // 10 miles
  if (value <= decade) {
    return "bg-white";
  } else if (value <= 2 * decade) {
    return "bg-lime-200";
  } else if (value <= 3 * decade) {
    return "bg-lime-400";
  } else if (value <= 4 * decade) {
    return "bg-lime-500";
  } else {
    return "bg-lime-700";
  }
}
