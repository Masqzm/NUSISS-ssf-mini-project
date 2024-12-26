package ssf.miniproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import ssf.miniproject.services.SearchService;

@Controller
@RequestMapping
public class SearchController {
    @Autowired 
    SearchService searchSvc;

    @GetMapping(path={"/", "index.html"})
    public String getIndex(HttpSession sess, Model model) {
        return "index";
    }

    @GetMapping("/search")
    public ModelAndView Search(@RequestParam String placeKeyword, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();

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
}