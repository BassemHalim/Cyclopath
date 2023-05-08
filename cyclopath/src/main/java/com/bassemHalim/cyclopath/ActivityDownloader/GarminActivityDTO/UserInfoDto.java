package com.bassemHalim.cyclopath.ActivityDownloader.GarminActivityDTO;

import lombok.Data;

@Data
public class UserInfoDto {
    private Object profileImageUrlLarge;
    private String displayname;
    private boolean userPro;
    private int userProfilePk;
    private String profileImageUrlSmall;
    private String fullname;
    private String profileImageUrlMedium;
}
