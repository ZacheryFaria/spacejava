package xyz.faria.space.spaceapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.DayOfWeek;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;

class UtilsTest {

    @Test
    void getCurrentResetDate() {
        OffsetDateTime zdt = OffsetDateTime.of(2025, 12, 26, 0, 0, 0, 0,
            ZoneOffset.of("-8"));
        OffsetDateTime resetDate = Utils.getResetDate(zdt);

        assertNotNull(resetDate);
        assertEquals(2025, resetDate.getYear());
        assertEquals(Month.DECEMBER, resetDate.getMonth());
        assertEquals(DayOfWeek.SUNDAY, resetDate.getDayOfWeek());
        assertEquals(21, resetDate.getDayOfMonth());
    }
}