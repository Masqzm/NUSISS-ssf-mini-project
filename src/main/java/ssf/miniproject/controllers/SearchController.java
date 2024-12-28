package ssf.miniproject.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import ssf.miniproject.config.Constants;
import ssf.miniproject.models.Jio;
import ssf.miniproject.models.Restaurant;
import ssf.miniproject.services.SearchService;

@Controller
@RequestMapping
public class SearchController {
    @Autowired 
    SearchService searchSvc;

    @GetMapping("/search")
    public ModelAndView Search(@RequestParam String placeKeyword, HttpServletRequest request, HttpSession sess) {
        ModelAndView mav = new ModelAndView();

        mav.addObject("currentUser", sess.getAttribute(Constants.SESS_ATTR_USER));

        if(placeKeyword.isBlank())
        {
            // Get the referer header to determine the previous page
            String referer = request.getHeader("Referer");

            // Redirect to referer if avail, else fallback to index
            if(referer != null) 
                mav.setViewName("redirect:" + referer); 
            else 
                mav.setViewName("index"); 
            
            return mav;
        }

        try {
            mav.addObject("restaurantsList", searchSvc.getRestaurantsList(placeKeyword));
            mav.addObject("placeKeyword", placeKeyword);
            mav.setViewName("search-results"); 
        } catch(Exception ex) {
            ex.printStackTrace();
            mav.addObject("errorMsg", ex.getMessage());
            mav.setViewName("search-error");    
        }

        return mav;
    }

    @PostMapping("/restaurant")
    public ModelAndView postRestaurantInfo(@RequestParam String restaurantJSON, HttpSession sess) {
        ModelAndView mav = new ModelAndView();
        
        // store restaurant from search results into redis permanently so we can use it later
        // - also looksup if restaurant has already been saved
        String id = searchSvc.saveRestaurant(Restaurant.jsonToRestaurant(restaurantJSON));

        mav.setViewName("redirect:restaurant/" + id); 

        return mav;
    }

    @GetMapping("/restaurant/{id}")
    public ModelAndView getRestaurantInfo(@PathVariable String id, HttpSession sess) {
        ModelAndView mav = new ModelAndView();

        mav.addObject("currentUser", sess.getAttribute(Constants.SESS_ATTR_USER));

        Restaurant rest = searchSvc.getRestaurantByID(id);     

        // To pass restaurant info if users post a Jio
        sess.setAttribute(Constants.SESS_ATTR_JIO_RESTAURANT, rest);
        
        mav.addObject("restaurant", searchSvc.getRestaurantByID(id));
        mav.addObject("jio", new Jio());
        mav.addObject("topicSuggestions", Constants.JIO_TOPICS_LIST);
        mav.setViewName("restaurant-info"); 

        return mav;
    }
}