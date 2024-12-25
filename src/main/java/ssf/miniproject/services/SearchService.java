package ssf.miniproject.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ssf.miniproject.models.Restaurant;
import ssf.miniproject.repo.SearchRepo;

@Service
public class SearchService {
    @Autowired
    SearchRepo searchRepo;

    @Value("${google.api.key}") 
    private String googleAPIkey;

    @Value("${server.port}")
    private int port;

    public List<Restaurant> getRestaurantsList(String keyword) throws Exception {
        // Get from cache if there are any results
        String cacheResult = searchRepo.getResultsFromCache(keyword);

        if(cacheResult != null)
            return Restaurant.jsonToRestaurant(cacheResult, googleAPIkey);
        
        // Make API call thru SearchRestController
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:" + port + "/api/search?keyword=" + keyword;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            
        if(response.getStatusCode().is2xxSuccessful()) {
            // Cache results
            searchRepo.cacheSearchResults(keyword, response.getBody());
            
            // Convert response to list of Restaurant objs
            return Restaurant.jsonToRestaurant(response.getBody(), googleAPIkey);
        } else {
            // might need to throw proper exception
            throw new Exception(response.getBody());            
        }
        //return null;
    }
}
