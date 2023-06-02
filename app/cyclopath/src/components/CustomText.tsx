import React from "react";
import { Text, TextProps, View } from "react-native";
import { styles } from "../Style";

export const RegularText: React.FC<TextProps> = ({
  children,
  style,
  ...props
}) => {
  return (
    <View style={[{ justifyContent: "space-around", alignItems: "center", flex: 1}, style]}>
      <Text style={[styles.text]} {...props}>
        {children}
      </Text>
    </View>
  );
};

// export default RegularText;
