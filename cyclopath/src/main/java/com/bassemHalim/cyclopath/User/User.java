package com.bassemHalim.cyclopath.User;

import com.bassemHalim.cyclopath.Activity.Activity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<Activity> activityList;
}
