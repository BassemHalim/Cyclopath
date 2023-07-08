import React from "react";
import { Text, TextProps, View } from "react-native";

interface StatProps {
  title: string;
  value: any;
  units: string;
}

export const Stat = ({ title, value, units }: StatProps) => {
  return (
    <View className="flex flex-col items-center ">
      <Text className="text-slate-400 font-semibold text-sm"> {title} </Text>
      <Text className="text-slate-100  font-semibold text-base">
        {value} {units}
      </Text>
    </View>
  );
};
