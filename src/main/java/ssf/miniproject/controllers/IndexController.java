package ssf.miniproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import ssf.miniproject.config.Constants;

@Controller
@RequestMapping
public class IndexController {
    @GetMapping(path={"/", "index.html"})
    public String getIndex(HttpSession sess, Model model) {
        model.addAttribute("currentUser", sess.getAttribute(Constants.SESS_ATTR_USER));

        return "index";
    }
    @GetMapping("/about")
    public String getAbout(HttpSession sess, Model model) {
        model.addAttribute("currentUser", sess.getAttribute(Constants.SESS_ATTR_USER));

        return "about";
    }
}
