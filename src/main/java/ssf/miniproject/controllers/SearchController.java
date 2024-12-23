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
            searchSvc.getRestaurantsList(placeKeyword);
        } catch(Exception ex) {
            //ex.printStackTrace();
            //mav.setViewName("search-error");    
        }
        
        mav.setViewName("index");

        return mav;
    }
}
