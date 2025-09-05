package timetests;

import org.jammor9.worldsim.time.Simulatorclock;
import org.junit.jupiter.api.Test;
public class TimeTest {


    @Test
    void testTimePassage() {
        Simulatorclock simulatorclock = Simulatorclock.getInstance();
        for (int i = 0; i < 500; i++) {
            simulatorclock.passDay();
        }

        System.out.println(simulatorclock.toString());
        assert "21-5-2 AC".equals(simulatorclock.toString());
    }

    @Test
    void testGetDay() {
        Simulatorclock simulatorclock = Simulatorclock.getInstance();
        int completionDay = simulatorclock.getDay() + 400;

        for (int i =0; i < 400; i++) {
            simulatorclock.passDay();
        }

        assert completionDay == simulatorclock.getDay();
    }
}
