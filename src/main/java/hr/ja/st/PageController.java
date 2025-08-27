package hr.ja.st;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    public static final String HOME = "/";
    public static final String ABOUT = "/about";

    @GetMapping(HOME)
    public String home(Model model, HttpServletRequest req) {
        model.addAttribute("username", "Marko");
        return "pages/home";
    }


    @GetMapping(ABOUT)
    public String showAbout() {
        return "pages/about";
    }

}
