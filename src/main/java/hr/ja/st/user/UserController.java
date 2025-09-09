package hr.ja.st.user;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "pages/users.jte";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).orElseThrow();
        model.addAttribute("user", user);
        return "pages/user_edit.jte";
    }

    @PostMapping("/{id}/edit")
    public String updateUser(@PathVariable Long id,
                             @RequestParam @NotBlank String username,
                             @RequestParam(name = "enabled", defaultValue = "false") boolean enabled,
                             @RequestParam(name = "roles", required = false) java.util.List<Role> roles,
                             @RequestParam(name = "newPassword", required = false) String newPassword) {
        User user = userRepository.findById(id).orElseThrow();
        user.setUsername(username);
        user.setEnabled(enabled);
        java.util.Set<Role> newRoles = new java.util.HashSet<>();
        if (roles != null) newRoles.addAll(roles);
        if (newRoles.isEmpty()) newRoles.add(Role.ROLE_USER);
        user.setRoles(newRoles);
        if (newPassword != null && !newPassword.isBlank()) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }
        userRepository.save(user);
        return "redirect:/users";
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/users";
    }
}
