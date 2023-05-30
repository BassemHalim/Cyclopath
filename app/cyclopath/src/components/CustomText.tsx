import React from "react";
import { Text, TextProps } from "react-native";
import { styles } from "../Style";

const RegularText: React.FC<TextProps> = ({ children, style, ...props }) => {
  return (
    <Text style={[styles.text, style]} {...props}>
      {children}
    </Text>
  );
};

export default RegularText;
