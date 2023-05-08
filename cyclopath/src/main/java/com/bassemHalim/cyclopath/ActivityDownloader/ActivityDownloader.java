package com.bassemHalim.cyclopath.ActivityDownloader;

import com.bassemHalim.cyclopath.Activity.Activity;
import com.bassemHalim.cyclopath.ActivityListItemDTO.ActivityListItemDTO;

import java.util.List;

public interface ActivityDownloader {
//    boolean login();

    List<ActivityListItemDTO> getActivitiesList();

    Activity getActivity(Long ID);

    byte[] downloadActivityRoute(Long id);

}
