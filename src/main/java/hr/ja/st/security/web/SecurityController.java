package hr.ja.st.security.web;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SecurityController {

    @GetMapping("/login")
    public String loginPage(Model model,
                            @RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            CsrfToken csrfToken) {
        model.addAttribute("error", error != null);
        model.addAttribute("logout", logout != null);
        if (csrfToken != null) {
            model.addAttribute("csrfParameterName", csrfToken.getParameterName());
            model.addAttribute("csrfToken", csrfToken.getToken());
        }
        return "pages/security/login.jte";
    }

        // GET /logout -> stranica koja automatski šalje POST /logout (s CSRF)
    @GetMapping("/logout")
    public String confirmOrAutoLogout() {
        return "pages/security/logout_redirect.jte";
    }
}
