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
import ssf.miniproject.models.Restaurant;
import ssf.miniproject.models.User;
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

        User user = (User) sess.getAttribute(Constants.SESS_ATTR_USER);

        // Restrict access if user haven't logged in & redirect to login page
        // if(user == null) {
        //     mav.setViewName("redirect:/login?restrictedFlagRaised=true");

        //     return mav;
        // }
        
        Restaurant rest = (Restaurant) sess.getAttribute(Constants.SESS_ATTR_JIO_RESTAURANT);
        
        // Redirect back
        if(bindings.hasErrors())
        {
            sess.setAttribute(Constants.SESS_ATTR_JIO_FORM_ERR, bindings);
            sess.setAttribute(Constants.SESS_ATTR_JIO_FORM, jio);
            mav.setViewName("redirect:/restaurant/" + rest.getId());
            
            return mav;
        }

        String id = jioSvc.saveJio(user.getName(), rest, jio);
        
        mav.addObject("jioID", id);
        mav.setViewName("redirect:/restaurant/" + rest.getId() + "?postSuccess=true");
        
        return mav;
    }
}
