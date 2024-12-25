package ssf.miniproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ssf.miniproject.services.SearchRestService;

@RestController
@RequestMapping
public class SearchRestController {
    @Autowired
    SearchRestService searchRestSvc;

    @GetMapping(path="/api/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> searchRestaurants(@RequestParam String keyword) {
        try {
            String result = searchRestSvc.searchRestaurants(keyword);
            
            return ResponseEntity.ok(result); 
            
        } catch (Exception ex) {
            // prob some server (endpoint) error 
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)       
                    .body("ERROR: " + ex.getMessage());
        }
    }
}
