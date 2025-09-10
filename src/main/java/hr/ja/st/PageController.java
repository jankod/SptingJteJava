package hr.ja.st;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static hr.ja.st.user.Roles.USER;

@Controller
public class PageController {

    public static final String HOME = "/";
    public static final String ABOUT = "/about";

    @GetMapping(HOME)
    @Secured(USER)
    public String home(Model model, HttpServletRequest req) {
        String username = req.getUserPrincipal() != null ? req.getUserPrincipal().getName() : "Gost";
        model.addAttribute("username", username);
        return "pages/home.jte";
    }

    @GetMapping(ABOUT)
    public String showAbout() {
        return "pages/about.jte";
    }

}
