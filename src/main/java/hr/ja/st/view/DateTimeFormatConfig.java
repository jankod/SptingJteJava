package hr.ja.st.view;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class DateTimeFormatConfig {
    public static volatile DateTimeFormatter FORMATTER;
    public static volatile ZoneId ZONE;

    public DateTimeFormatConfig(
            @Value("${app.datetime.format:yyyy-MM-dd HH:mm:ss}") String pattern,
            @Value("${app.datetime.zone:system}") String zone) {
        ZoneId zid = "system".equalsIgnoreCase(zone) ? ZoneId.systemDefault() : ZoneId.of(zone);
        ZONE = zid;
        FORMATTER = DateTimeFormatter.ofPattern(pattern).withZone(zid);
    }
}

