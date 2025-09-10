package hr.ja.st.view;

import hr.ja.st.user.Roles;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

class FormattersTests {

    private final DateTimeFormatter origFormatter = DateTimeFormatConfig.FORMATTER;
    private final ZoneId origZone = DateTimeFormatConfig.ZONE;

    @AfterEach
    void restore() {
        DateTimeFormatConfig.FORMATTER = origFormatter;
        DateTimeFormatConfig.ZONE = origZone;
    }

    @Test
    @DisplayName("instant formats using configured DateTimeFormatConfig")
    void instant_formats() {
        new DateTimeFormatConfig("dd.MM.yyyy HH:mm:ss", "UTC");
        Instant i = Instant.parse("2024-01-02T03:04:05Z");
        assertThat(Formatters.instant(i)).isEqualTo("02.01.2024 03:04:05");
    }

    @Test
    @DisplayName("instant falls back to ISO when formatter null")
    void instant_fallback() {
        DateTimeFormatConfig.FORMATTER = null;
        Instant i = Instant.parse("2024-01-02T03:04:05Z");
        assertThat(Formatters.instant(i)).isEqualTo(DateTimeFormatter.ISO_INSTANT.format(i));
    }

    @Test
    @DisplayName("rolesToLabel sorts and maps labels")
    void roles_to_label() {
        String s = Formatters.rolesToLabel(new HashSet<>(Arrays.asList(Roles.ADMIN, Roles.USER)));
        assertThat(s).isEqualTo("Admin, User");
        assertThat(Formatters.roleLabel(Roles.ADMIN)).isEqualTo("Admin");
        assertThat(Formatters.roleLabel(Roles.USER)).isEqualTo("User");
    }
}
