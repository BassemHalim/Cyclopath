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
    justifyContent: "center",
  },
  statTitle: {
    color: "#898F9C",
    fontSize: 14,
    fontFamily: "Roboto",
    textAlign: "center",
  },
  statValue: {
    color: "#e4e6eb",
    fontSize: 17,
    fontFamily: "Roboto",
    textAlign: "center",
  },
  loginForm: {
    borderColor: "#fff",
    margin: 5,
    flex: 1,
    maxHeight: 300,
  },
  logo: {
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
    margin: 5,
    marginHorizontal: 10,
    backgroundColor: "#242526",
  },
  activityTitle: {
    justifyContent: "space-around",
    alignItems: "center",
    flex: 1,
  },
  activityStat: {
    flex: 1,
    flexDirection: "column",
    justifyContent: "space-around",
    alignSelf: "center",
  },
  activityStatsRow: {
    // display: "flex",
    flex: 1,
    flexDirection: "row",
    flexWrap: "wrap",
    justifyContent: "space-around",
    // margin: 5,
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
  },
  home: {
    flex: 1,
    flexDirection: "column",
    backgroundColor: "#18191a",
  },
});

export { styles };
