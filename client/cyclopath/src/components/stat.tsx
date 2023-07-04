import React from "react";
import { Text, TextProps, View } from "react-native";
import { styles } from "../Style";

interface StatProps {
  title: string;
  value: any;
  units: string;
}

export const Stat = ({ title, value, units }: StatProps) => {
  return (
    <View style={styles.activityStat}>
      <Text style={[styles.statTitle]}> {title} </Text>
      <Text style={styles.statValue}>
        {value} {units}
      </Text>
    </View>
  );
};
