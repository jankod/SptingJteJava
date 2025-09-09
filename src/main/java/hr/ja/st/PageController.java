package hr.ja.st;

import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    public static final String HOME = "/";

    @GetMapping(HOME)
    // restrict only ROLE_USER can access via annotation
    @RolesAllowed("ROLE_USER")
    public String home(Model model, HttpServletRequest req) {
        String username = req.getUserPrincipal() != null ? req.getUserPrincipal().getName() : "Gost";
        model.addAttribute("username", username);
        return "pages/home.jte";
    }


    public static final String ABOUT = "/about";

    @GetMapping(ABOUT)
    public String showAbout() {
        return "pages/about.jte";
    }

}
