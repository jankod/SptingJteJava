package hr.ja.st.user.web;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import hr.ja.st.user.repo.UserRepository;
import hr.ja.st.user.domain.User;
import hr.ja.st.user.domain.Roles;
import hr.ja.st.user.web.dto.NewUserForm;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
@Secured(Roles.ADMIN)
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "pages/user/list.jte";
    }

    @GetMapping("/new")
    public String newUserForm(Model model) {
        NewUserForm form = new NewUserForm();
        form.getRoles().add(Roles.USER);
        model.addAttribute("form", form);
        model.addAttribute("allRoles", hr.ja.st.user.domain.Roles.ALL.toArray(new String[0]));
        return "pages/user/new.jte";
    }

    @PostMapping
    public String createUser(@ModelAttribute("form") @jakarta.validation.Valid NewUserForm form,
                             org.springframework.validation.BindingResult binding,
                             Model model) {
        // Normalize input
        if (form.getUsername() != null) {
            form.setUsername(form.getUsername().trim());
        }
        // Username uniqueness
        userRepository.findByUsername(form.getUsername()).ifPresent(u ->
                binding.rejectValue("username", "exists", "Korisničko ime već postoji"));
        // Passwords match
        if (!binding.hasFieldErrors("password") && !form.getPassword().equals(form.getConfirmPassword())) {
            binding.rejectValue("confirmPassword", "mismatch", "Lozinke se ne podudaraju");
        }
        // Roles default
        if (form.getRoles() == null || form.getRoles().isEmpty()) {
            form.add(Roles.USER);
        }

        if (binding.hasErrors()) {
            java.util.List<String> errors = new java.util.ArrayList<>();
            for (org.springframework.validation.ObjectError e : binding.getAllErrors()) {
                errors.add(e.getDefaultMessage());
            }
            model.addAttribute("errors", errors);
            model.addAttribute("allRoles", hr.ja.st.user.domain.Roles.ALL.toArray(new String[0]));
            return "pages/user/new.jte";
        }

        User user = User.builder()
                .username(form.getUsername())
                .enabled(form.isEnabled())
                .roles(new java.util.HashSet<>(form.getRoles()))
                .password(passwordEncoder.encode(form.getPassword()))
                .build();
        userRepository.save(user);
        return "redirect:/users";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).orElseThrow();
        model.addAttribute("user", user);
        model.addAttribute("allRoles", hr.ja.st.user.domain.Roles.ALL.toArray(new String[0]));
        return "pages/user/edit.jte";
    }

    @PostMapping("/{id}/edit")
    public String updateUser(@PathVariable Long id,
                             @RequestParam @NotBlank String username,
                             @RequestParam(name = "enabled", defaultValue = "false") boolean enabled,
                             @RequestParam(name = "roles", required = false) java.util.List<String> roles,
                             @RequestParam(name = "newPassword", required = false) String newPassword) {
        User user = userRepository.findById(id).orElseThrow();
        user.setUsername(username);
        user.setEnabled(enabled);
        java.util.Set<String> newRoles = new java.util.HashSet<>();
        if (roles != null) newRoles.addAll(roles);
        if (newRoles.isEmpty()) newRoles.add(Roles.USER);
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
