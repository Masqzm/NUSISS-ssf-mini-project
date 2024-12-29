package ssf.miniproject.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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
import ssf.miniproject.services.JioService;
import ssf.miniproject.services.SearchService;

@Controller
@RequestMapping
public class SearchController {
    @Autowired 
    SearchService searchSvc;
    @Autowired
    JioService jioSvc;

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
        String id = searchSvc.saveRestaurant(Restaurant.jsonToRestaurant(restaurantJSON), false);

        mav.setViewName("redirect:restaurant/" + id); 

        return mav;
    }

    @GetMapping("/restaurant/{id}")
    public ModelAndView getRestaurantInfo(
        @RequestParam(required=false, defaultValue="false") boolean postSuccess, 
        @PathVariable String id, HttpSession sess) {
            
        ModelAndView mav = new ModelAndView();

        mav.addObject("currentUser", sess.getAttribute(Constants.SESS_ATTR_USER));

        Restaurant rest = searchSvc.getRestaurantByID(id); 
        
        // if restaurant cannot be found in db
        if(rest == null) {
            mav.setStatus(HttpStatusCode.valueOf(404));
            mav.addObject("id", id);
            mav.setViewName("error404"); 

            return mav;
        }

        // To pass restaurant info if users post a Jio
        sess.setAttribute(Constants.SESS_ATTR_JIO_RESTAURANT, rest);
        
        // Form error handling (passed thru sessions as users are redirected back here)
        BindingResult bindings = (BindingResult) sess.getAttribute(Constants.SESS_ATTR_JIO_FORM_ERR);
        Jio jio = (Jio) sess.getAttribute(Constants.SESS_ATTR_JIO_FORM);
        
        if(bindings != null && bindings.hasErrors()) {
            if(bindings.getFieldError("date") != null)
                mav.addObject("dateErrors", bindings.getFieldError("date").getDefaultMessage());
            if(bindings.getFieldError("time") != null)
                mav.addObject("timeErrors", bindings.getFieldError("time").getDefaultMessage());
            if(bindings.getFieldError("topics") != null)
                mav.addObject("topicsErrors", bindings.getFieldError("topics").getDefaultMessage());

            // Remove errors from sess
            sess.removeAttribute(Constants.SESS_ATTR_JIO_FORM_ERR);
        }

        // Bind jios to restaurant-info, if available
        if(!rest.getJioIDList().isEmpty()) {
            List<Jio> jios = new ArrayList<>();
            for (String jioID : rest.getJioIDList()) {
                Jio checkJio = jioSvc.getJioByID(jioID);

                // null jios are from deletion of jios
                if(checkJio != null)
                    jios.add(checkJio);
            }
            
            // Sort list by each Jio's unix timestamp
            jios.sort((jio1, jio2) -> {
                long unix1 = Jio.convertToUnixTimestamp(jio1.getDate(), jio1.getTime());
                long unix2 = Jio.convertToUnixTimestamp(jio2.getDate(), jio2.getTime());
                
                return Long.compare(unix1, unix2);  
            });

            mav.addObject("jioList", jios);
        }
                
        mav.addObject("restaurant", searchSvc.getRestaurantByID(id));
        mav.addObject("jio", (jio == null ? new Jio() : jio));
        mav.addObject("topicSuggestions", Constants.JIO_TOPICS_LIST);
        mav.addObject("postSuccess", postSuccess);
        mav.setViewName("restaurant-info"); 

        return mav;
    }
}