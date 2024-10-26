package local.leporidaeyellow.infrastructure.simplehostwatcher.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;
import lombok.val;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Value
@RequiredArgsConstructor
public class Event {

    public static final long DELAY = 15;
    public static final long ALARM_DELAY = 10 * 60;

    String name;

    Instant createdTimestamp = Instant.now();

    @NonFinal
    Instant nextAlarmTimestamp;

    public boolean sendAlarm() {
        val now = Instant.now();

        if (nextAlarmTimestamp != null && now.isBefore(nextAlarmTimestamp)) {
            return false;
        }

        nextAlarmTimestamp = now.plus(ALARM_DELAY, ChronoUnit.SECONDS);
        return true;
    }

    public long getDuration() {
        return createdTimestamp.until(Instant.now(), ChronoUnit.SECONDS);
    }
}