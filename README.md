# Cyclopath

- a workout app that focuses on cycling. The app is meant to combine multiple features from existing
  apps into a single app.
- main features:
    - Sync activities from Garmin Connect
    - Display weather info for every activity especially wind direction and speed
    - Display the workout map in addition to Heart Rate data and speed/section
    - Monitor and display user progress over time
      ![img.png](img.png)
    - plan and save routes using maps and weather forecast
- Nice-to-have features:
    - record activities from within app
    - add pictures to activities
    - personal heatmap

## API:

### Users

```markdown
auth/register [POST]
auth/signin [POST]
/signout [POST]
```

### Activity

```md
/activity/list [GET]
/activity/{ID} [GET]
/activity/post [POST] (in the future)
```

### Progress/Stats

```md
/athlete/{ID}/zones [GET]
the amount of time spent in each zone
/athlete/{ID}/stats [GET]
longest distance, number of activities, etc.
```

## Schemas

### Dynamo DB Single Table

| Primary Key | Sort Key     | Attributes                                                                                  |
| ----------- | ------------ | ------------------------------------------------------------------------------------------- |
| UserUUID    | PROFILE      | {Username:STRING, Email: STRING, Password:STRING, ID:LONG, weight:FLOAT, Hight(cm):INT,...} |
| UserUUID    | STATS        | {LongestRideID:LONG, numActivities:INT, ...}                                                |
| UserUUID    | ACTIVITY#ID  | {Name, CreationTime, Distance, CaloriesBurned, Comments, weather:Weather}                   |
| UserUUID    | ROUTE#ID     | {geoJSON_zip[] }                                                                            |
| UserUUID    | ACTIVITYLIST | {Activity1ID,Activity2ID,...}                                                               |

- activity id is the same as unix time and is only unique per user
- this is useful for getting an activity by id or for getting activities within a date range

### Access Patterns

- Lookup user info by id
- lookup user by username
- get activity by id
- get activities withing a date range
- get athlete stats
