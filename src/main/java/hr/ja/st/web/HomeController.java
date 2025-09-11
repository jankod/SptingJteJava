package hr.ja.st.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static hr.ja.st.user.domain.Roles.USER;

@Controller
public class HomeController {

    @GetMapping(Routes.HOME)
    @Secured(USER)
    public String home(Model model, HttpServletRequest req) {
        String username = req.getUserPrincipal() != null ? req.getUserPrincipal().getName() : "Gost";
        model.addAttribute("username", username);
        return "pages/home/index.jte";
    }

    @GetMapping(Routes.ABOUT)
    public String showAbout() {
        return "pages/about/index.jte";
    }

}
