package hr.ja.st;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    public static final String HOME = "/";

    @GetMapping(HOME)
    public String home(Model model, HttpServletRequest req) {
        model.addAttribute("username", "Marko");
        return "pages/home.jte";
    }


    public static final String ABOUT = "/about";

    @GetMapping(ABOUT)
    public String showAbout() {
        return "pages/about.jte";
    }

}
