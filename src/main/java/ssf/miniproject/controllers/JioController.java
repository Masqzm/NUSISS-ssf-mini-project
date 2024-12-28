package ssf.miniproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import ssf.miniproject.config.Constants;
import ssf.miniproject.models.Jio;
import ssf.miniproject.services.JioService;

@Controller
@RequestMapping
public class JioController {
    @Autowired
    JioService jioSvc;

    // @GetMapping("/jio/{id}")
    // public ModelAndView getJioByID(@PathVariable String id) {

    // }

    @PostMapping("/jio")
    public ModelAndView postJio(@Valid Jio jio, BindingResult bindings, HttpSession sess) {
        ModelAndView mav = new ModelAndView();

        // Restrict access if user haven't logged in & redirect to login page
        if(sess.getAttribute(Constants.SESS_ATTR_USER) == null) {
            mav.setViewName("redirect:/login?restrictedFlagRaised=true");

            return mav;
        }


        System.out.println(jio);

        
        mav.setViewName("index");

        return mav;
    }
}
