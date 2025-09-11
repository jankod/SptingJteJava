package hr.ja.st.user.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class NewUserForm {
    @NotBlank
    private String username;

    private boolean enabled = true;

    // roles selected in the form (authorities strings)
    private List<String> roles = new ArrayList<>();

    @NotBlank
    @Size(min = 8, message = "Lozinka mora imati barem 8 znakova")
    private String password;

    @NotBlank
    private String confirmPassword;

    public void add(String role) {
        if (roles == null) {
            roles = new ArrayList<>();
        }
        if (role != null && !roles.contains(role)) {
            roles.add(role);
        }
    }
}
