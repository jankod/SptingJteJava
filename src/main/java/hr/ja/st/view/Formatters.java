package hr.ja.st.view;

import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

import hr.ja.st.user.Roles;
@Slf4j
public final class Formatters {
    private Formatters() {}

    public static String instant(Instant instant) {
        if (instant == null) return "";
        DateTimeFormatter f = DateTimeFormatConfig.FORMATTER;
        if (f == null) {
            log.debug("DateTimeFormatConfig.FORMATTER is null, using ISO_INSTANT");
            f = DateTimeFormatter.ISO_INSTANT;
        }
        return f.format(instant);
    }

    public static String rolesToLabel(Collection<String> roles) {
        if (roles == null || roles.isEmpty()) return "";
        return roles.stream()
                .sorted()
                .map(Formatters::roleLabel)
                .collect(Collectors.joining(", "));
    }

    public static String roleLabel(String role) {
        if (role == null) return "";
        return Roles.label(role);
    }
}
