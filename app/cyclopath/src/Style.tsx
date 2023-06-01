import { StyleSheet } from "react-native";

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: "column",
    backgroundColor: "#000",
    alignItems: "center",
    justifyContent: "center",
  },
  headerText: {
    color: "#fff",
    fontSize: 30,
    fontWeight: "bold",
    margin: 10,
  },
  text: {
    color: "#e4e6eb",
    fontSize: 16,
    fontWeight: "400",
    fontFamily: "Roboto",
  },
  smallText: {
    color: "#fff",
    fontSize: 14,
    margin: 20,
  },
  inputBox: {
    color: "#fff",
    backgroundColor: "grey",
    fontSize: 15,
    width: 300,
    marginVertical: 10,
    borderRadius: 7,
  },
  loginForm: {
    borderColor: "#fff",
    margin: 5,
    flex: 1,
  },
  logo: {
    // flex: 1,
    width: "80%",
    height: 200,
    maxHeight: 200,
    maxWidth: 400,
    borderRadius: 10,
    margin: 30,
    marginTop: 45,
    marginBottom: 30,
  },
  activityStats: {
    borderRadius: 5,
    height: 400,
    // borderWidth: 2,
    margin: 5,
    marginHorizontal: 10,
    backgroundColor: "#242526",
  },
  activityStatsRow: {
    display: "flex",
    flexDirection: "row",
    flexWrap: "wrap",
    justifyContent: "space-around",
    margin: 10,
  },
  activityMap: {
    flex: 1,
    width: "80%",
    height: 200,
    maxHeight: 200,
    maxWidth: 400,
    borderRadius: 10,
    margin: 10,
    alignSelf: "center",
    backgroundColor: "white",
  },
  home: {
    flex: 1,
    flexDirection: "column",
    backgroundColor: "#18191a",
  },
});

export { styles };
