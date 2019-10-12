package chargingstations.model;

import com.evapps.chargepoints.model.Session;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class StationInfoTest {
    Session st;

    @Before
    public void setup() {
        this.st = new Session();
    }

    @Test
    public void getLoc() {
        Long stationId = 4L;

        st.setId(stationId);
        assertEquals(stationId, st.getId());
    }

    @Test
    public void testGetLoc() {
        String loc = "Test Location";

        st.setLoc(loc);
        assertEquals(loc, st.getLoc());
    }

    @Test
    public void getType() {
        String type = "Test Type";

        st.setType(type);
        assertEquals(type, st.getType());
    }

    @Test
    public void getStatus() {
        String status = "Available";

        st.setStatus(status);
        assertEquals(status, st.getStatus());
    }

    @Test
    public void getStartedAt() {
        LocalDateTime startedAt = LocalDateTime.now();

        st.setStartedAt(startedAt);
        assertEquals(startedAt, st.getStartedAt());
    }

    @Test
    public void getElapsedMins() {
        int et = 99;

        st.setElapsedMins(et);
        assertEquals(et, st.getElapsedMins());
    }

    @Test
    public void getStoppedAt() {
        LocalDateTime stoppedAt = LocalDateTime.now();

        st.setStoppedAt(stoppedAt);
        assertEquals(stoppedAt, st.getStoppedAt());
    }
}