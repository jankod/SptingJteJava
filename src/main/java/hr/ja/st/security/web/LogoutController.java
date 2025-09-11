package hr.ja.st.security.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {

    // GET /logout -> stranica koja automatski šalje POST /logout (s CSRF)
    @GetMapping("/logout")
    public String confirmOrAutoLogout() {
        return "pages/security/logout_redirect.jte";
    }
}
