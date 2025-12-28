package xyz.faria.space.spaceapi;

import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UtilsTest {

    @Test
    void getCurrentResetDate() {
        ZonedDateTime zdt = ZonedDateTime.of(2025, 12, 26, 0, 0, 0, 0, ZoneId.of("America/Los_Angeles"));
        LocalDate resetDate = Utils.getResetDate(zdt).toLocalDate();

        assertNotNull(resetDate);
        assertEquals(2025, resetDate.getYear());
        assertEquals(Month.DECEMBER, resetDate.getMonth());
        assertEquals(DayOfWeek.SUNDAY, resetDate.getDayOfWeek());
        assertEquals(21, resetDate.getDayOfMonth());
    }
}