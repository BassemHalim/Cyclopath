package com.bassemHalim.cyclopath.Activity;

import java.util.List;

public interface ActivityDownloader {
//    boolean login();

    List<Activity> getActivitiesList();

    byte[] downloadActivity(Long id);

}
