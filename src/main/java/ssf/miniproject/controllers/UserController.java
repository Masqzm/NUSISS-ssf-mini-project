package ssf.miniproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;
import ssf.miniproject.models.User;
import ssf.miniproject.services.UserService;

@Controller
@RequestMapping
public class UserController {
    @Autowired
    UserService userSvc;

    @GetMapping("/register")
    public ModelAndView getRegistration() {
        ModelAndView mav = new ModelAndView();

        mav.addObject("user", new User());
        mav.setViewName("register");
            
        return mav;
    }

    @PostMapping("/register")
    public ModelAndView postRegistration(@Valid User user, BindingResult bindings) {
        ModelAndView mav = new ModelAndView();

        boolean userExists = false;
        
        if(user.getName().length() >= 3)
            userExists = userSvc.checkUserExists(user.getName());
        
        // Go back to original page if form has invalid inputs
        if(bindings.hasErrors() || userExists)
        {
            // Check if username exists
            if(userExists) {
                FieldError err = new FieldError("user", "name", "%s is not available".formatted(user.getName()));
                bindings.addError(err);
            }

            mav.setViewName("register");
            
            return mav;
        }

        userSvc.registerUser(user);

        return mav;
    }

    @GetMapping("/login")
    public ModelAndView getLogin() {
        ModelAndView mav = new ModelAndView();

        mav.addObject("user", new User());
        mav.setViewName("login");
            
        return mav;
    }

    // @PostMapping("/login")
    // public ModelAndView postLogin(@Valid User user, BindingResult bindings) {
    //     ModelAndView mav = new ModelAndView();



    //     return mav;
    // }
}
