package xyz.faria.space.spaceapi;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;

public class Utils {
    public static Date getResetDate(ZonedDateTime date) {
        ZonedDateTime resetThisWeek = date
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
                .withHour(5)
                .withMinute(0)
                .withSecond(0);

        if (date.isBefore(resetThisWeek)) {
            resetThisWeek = resetThisWeek.minusWeeks(1);
        }

        return new Date(resetThisWeek.toInstant().toEpochMilli());
    }

    public static Date getCurrentResetDate() {
        ZoneId pstZone = ZoneId.of("America/Los_Angeles");
        ZonedDateTime nowPst = ZonedDateTime.now(pstZone);
        return getResetDate(nowPst);
    }
}
