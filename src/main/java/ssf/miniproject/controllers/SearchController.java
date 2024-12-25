package ssf.miniproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;
import ssf.miniproject.services.SearchService;

@Controller
@RequestMapping
public class SearchController {
    @Autowired 
    SearchService searchSvc;

    @RequestMapping(path={"/", "index.html"})
    public String getIndex(HttpSession sess, Model model) {
        
        return "index";
    }

    @GetMapping("/search")
    //public ModelAndView Search(@RequestParam MultiValueMap<String, String> form) {
    public ModelAndView Search(@RequestParam String placeKeyword) {
        ModelAndView mav = new ModelAndView();

        try {
            mav.addObject("restaurantsList", searchSvc.getRestaurantsList(placeKeyword));
            mav.setViewName("search-results"); 
        } catch(Exception ex) {
            ex.printStackTrace();
            mav.addObject("errorMsg", ex.getMessage());
            mav.setViewName("search-error");    
        }

        return mav;
    }
}