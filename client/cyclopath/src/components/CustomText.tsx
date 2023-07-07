import React from "react";
import { Text, TextProps, View } from "react-native";
import { styles } from "../Style";

export const RegularText: React.FC<TextProps> = ({
  children,
  style,
  ...props
}) => {
  return (
    <View style={[style]}>
      <Text style={[styles.text]} {...props}>
        {children}
      </Text>
    </View>
  );
};

