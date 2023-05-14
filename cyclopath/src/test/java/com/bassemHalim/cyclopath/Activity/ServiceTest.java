package com.bassemHalim.cyclopath.Activity;

import com.bassemHalim.cyclopath.Repositoy.SingleTableDB;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext_mock.xml"})
public class ServiceTest {
    @Autowired
    private SingleTableDB repository;

    @Test
    public void ActivityListMetadata() {
        ActivitiesMetatdata activitiesMetatdata = new ActivitiesMetatdata();
        activitiesMetatdata.addActivity(1000L);
        assertNotNull(activitiesMetatdata.getSavedActivities());
        assertEquals(activitiesMetatdata.getSavedActivities().size(), 1);
        assertEquals(activitiesMetatdata.getSavedActivities().get(0), 1000L);
    }

//    @Test
//    public void testSaveActivityMedadata() {
//        ActivitiesMetatdata activitiesMedatdata = new ActivitiesMetatdata();
//        activitiesMedatdata.setUUID("9e3f9fbb-49bc-4a10-949b-f00effba9627");
//        activitiesMedatdata.addActivity(1000L);
//        activitiesMedatdata.addActivity(2000L);
//        repository.saveActivityMetadata(activitiesMedatdata);
//    }


}
