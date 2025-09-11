package hr.ja.st.user.domain;

import java.util.List;

public final class Roles {
    private Roles() {}

    public static final String USER = "ROLE_USER";
    public static final String ADMIN = "ROLE_ADMIN";

    public static final List<String> ALL = List.of(USER, ADMIN);

    public static String label(String role) {
        if (role == null) return "";
        return switch (role) {
            case ADMIN -> "Admin";
            case USER -> "User";
            default -> role;
        };
    }
}
