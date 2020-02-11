package chargingstations.controller;


import com.evapps.chargepoints.controller.ApiController;
import com.evapps.chargepoints.model.Session;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class APIcontrollerTest {
    ApiController api;
    Session st;
    Long id;
    String loc;
    String status;
    String type;
    LocalDateTime startedAt;
    LocalDateTime stoppedAt;
    int time;

    @Before
    public void setUp() throws Exception {
        id = 999999L;
        loc = "My Test Location";
        status = "Not Available";
        type = "My Home Charger";
        startedAt = LocalDateTime.now();
        stoppedAt = LocalDateTime.now();
        time = 9999;
        api = new ApiController();
        st = new Session();

        //st.setId(id);
        st.setLoc(loc);
        st.setStatus(status);
        st.setType(type);
        st.setStartedAt(startedAt);
        st.setStoppedAt(stoppedAt);
        st.setElapsedMins(time);
    }

//    @Test
//    public void testSetsAndGets() {
//       api.saveSessionInfo(this.st);
//
//       assertEquals(st, api.getSessionById(this.id));
//    }
}