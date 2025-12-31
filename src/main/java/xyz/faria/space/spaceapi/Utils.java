package xyz.faria.space.spaceapi;

import java.time.DayOfWeek;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;

public class Utils {

    public static OffsetDateTime getResetDate(OffsetDateTime date) {
        OffsetDateTime resetThisWeek = date
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
            .withHour(5)
            .withMinute(0)
            .withSecond(0)
            .withNano(0);

        if (date.isBefore(resetThisWeek)) {
            resetThisWeek = resetThisWeek.minusWeeks(1);
        }

        return resetThisWeek;
    }

    public static OffsetDateTime getCurrentResetDate() {
        ZoneId pstZone = ZoneId.of("America/Los_Angeles");
        OffsetDateTime nowPst = OffsetDateTime.now(pstZone);
        return getResetDate(nowPst);
    }

}
