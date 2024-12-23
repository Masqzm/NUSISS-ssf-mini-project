package ssf.miniproject.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ssf.miniproject.models.Restaurant;

@Service
public class SearchService {
    RestTemplate restTemplate = new RestTemplate();

    @Value("${server.port}")
    private int port;

    public List<Restaurant> getRestaurantsList(String keyword) throws Exception {
        // To invoke SearchRestController
        String url = "http://localhost:" + port + "/api/search?keyword=" + keyword;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if(response.getStatusCode().is2xxSuccessful())
        {
            //System.out.println("In SearchService: " + response.getBody());

            // Process response and save it as list of Restaurants

            // Handle null case (i.e. keyword returns no result)
        }
        else
        {
            // might need to throw proper exception
            throw new Exception(response.getBody());            
        }
        
        return null;
    }
}
