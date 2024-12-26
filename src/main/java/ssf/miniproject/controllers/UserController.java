package ssf.miniproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import ssf.miniproject.config.Constants;
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
        
        mav.setViewName("register-success");

        return mav;
    }

    @GetMapping("/login")
    public ModelAndView getLogin(@RequestParam(required = false, defaultValue = "false") boolean restrictedFlagRaised, HttpServletRequest request, HttpSession sess) {
        ModelAndView mav = new ModelAndView();

        String referer = request.getHeader("Referer");

        // Store current page before clicking "Login" 
        if(referer != null && !referer.contains("login")) {
            String redirReq = referer;

            // Set redirect to index if user is from registration page
            if(referer.contains("register"))
                redirReq = "/";

            sess.setAttribute(Constants.SESS_ATTR_REDIR_REQ, "redirect:" + redirReq);
        }

        mav.addObject("user", new User());
        mav.addObject("restrictedFlagRaised", restrictedFlagRaised);
        mav.setViewName("login");
            
        return mav;
    }

    @PostMapping("/login")
    public ModelAndView postLogin(@RequestBody MultiValueMap<String, String> form, HttpSession sess) {
        ModelAndView mav = new ModelAndView();

        String name = form.getFirst("name");
        String password = form.getFirst("password");

        boolean hasBlankName = (name == null || name.isBlank());
        boolean hasBlankPassword = (password == null || password.isBlank());

        if(hasBlankName || hasBlankPassword) {
            if(hasBlankName) 
                mav.addObject("nameBlankMsg", "Please enter username");

            if(hasBlankPassword) 
                mav.addObject("pwBlankMsg", "Please enter password");

            mav.setViewName("login");

            return mav;
        }

        // Check login credentials
        User user = userSvc.checkUserCredentials(name, password);
        if(user == null) {
            mav.addObject("credentialsErrorMsg", "Invalid username or password entered!");
            mav.setViewName("login");

            return mav;
        }

        // Add user to current session
        sess.setAttribute(Constants.SESS_ATTR_USER, user);
        
        // Redirect to previous page before login, if avail
        if(sess.getAttribute(Constants.SESS_ATTR_REDIR_REQ) != null)
            mav.setViewName((String) sess.getAttribute(Constants.SESS_ATTR_REDIR_REQ));
        else
            mav.setViewName("redirect:/");

        return mav;
    }
}
