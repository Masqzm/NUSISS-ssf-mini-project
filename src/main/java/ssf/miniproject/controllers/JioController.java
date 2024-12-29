package ssf.miniproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import ssf.miniproject.config.Constants;
import ssf.miniproject.models.Jio;
import ssf.miniproject.models.Restaurant;
import ssf.miniproject.models.User;
import ssf.miniproject.services.JioService;
import ssf.miniproject.services.SearchService;

@Controller
@RequestMapping
public class JioController {
    @Autowired
    JioService jioSvc;
    @Autowired
    SearchService searchSvc;

    @GetMapping("/jio/{id}")
    public ModelAndView getJioByID(@PathVariable String id, HttpSession sess) {
        ModelAndView mav = new ModelAndView();

        User user = (User) sess.getAttribute(Constants.SESS_ATTR_USER);

        // Restrict access if user haven't logged in & redirect to login page
        if(user == null) {
            mav.setViewName("redirect:/login?restrictedFlagRaised=true");

            return mav;
        }

        Jio jio = jioSvc.getJioByID(id);
        
        // if jio cannot be found in db
        if(jio == null) {
            mav.setStatus(HttpStatusCode.valueOf(404));
            mav.addObject("id", id);
            mav.setViewName("error404"); 

            return mav;
        }

        Restaurant rest = searchSvc.getRestaurantByID(jio.getRestaurantID());
        
        boolean allowJoin = false;

        // Let user join if he is not alrd an attendee of this jio & not poster
        if(!jio.getAttendeesNameList().contains(user.getName())
        && !jio.getPosterName().equals(user.getName()))
            allowJoin = true;

        mav.addObject("allowJoin", allowJoin);
        mav.addObject("currentUser", user);
        mav.addObject("restaurant", rest);
        mav.addObject("jio", jio);
        mav.setViewName("jio-info");
        
        return mav;
    }

    @PostMapping("/jio")
    public ModelAndView postJio(@Valid Jio jio, BindingResult bindings, HttpSession sess) {
        ModelAndView mav = new ModelAndView();

        User user = (User) sess.getAttribute(Constants.SESS_ATTR_USER);

        // Restrict access if user haven't logged in & redirect to login page
        if(user == null) {
            mav.setViewName("redirect:/login?restrictedFlagRaised=true");

            return mav;
        }
        
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
        
        //mav.addObject("jioID", id);
        mav.setViewName("redirect:/restaurant/" + rest.getId() + "?postSuccess=true");
        
        return mav;
    }
    
    @PostMapping("/updateJioAttendee")
    public ModelAndView postUpdateJio(@RequestBody MultiValueMap<String, String> form, HttpSession sess) {
        ModelAndView mav = new ModelAndView();

        User user = (User) sess.getAttribute(Constants.SESS_ATTR_USER);

        // Restrict access if user haven't logged in & redirect to login page
        if(user == null) {
            mav.setViewName("redirect:/login?restrictedFlagRaised=true");

            return mav;
        }

        Jio jio = Jio.jsonToJio(form.getFirst("jioJSON"));

        // if user is not poster of this jio
        if(!jio.getPosterName().equals(user.getName())) {
            // if there's still capacity to join this jio
            if(jio.getAttendeesNameList().size() < jio.getCapacity())
                jioSvc.updateJio(jio, form.getFirst("attendee"), Boolean.parseBoolean(form.getFirst("isJoining")));

            mav.setViewName("redirect:/jio/" + jio.getId());

            return mav;
        }

        jioSvc.deleteJioByID(jio.getId());
        
        mav.setViewName("redirect:/restaurant/" + jio.getRestaurantID());

        return mav;
    }
}
