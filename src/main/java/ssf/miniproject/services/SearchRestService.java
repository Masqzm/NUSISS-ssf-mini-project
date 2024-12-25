package ssf.miniproject.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import ssf.miniproject.config.Constants;

@Service
public class SearchRestService {
    @Value("${google.api.key}") 
    private String googleAPIkey;

    public String searchRestaurants(String keyword) throws Exception {
        // Create Json payload
        JsonObject json = Json.createObjectBuilder()
                    .add("textQuery", keyword)
                    .add("includedType", "restaurant")
                    .add("locationRestriction", Constants.GOOGLE_PLACES_TEXTSEARCH_LOC_JSON)
                    .build();

        // Create req
        RequestEntity<String> req = RequestEntity
                    .post(Constants.GOOGLE_PLACES_TEXTSEARCH_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)  
                    .header("X-Goog-Api-Key", googleAPIkey)  
                    .header("X-Goog-FieldMask", Constants.GOOGLE_PLACES_TEXTSEARCH_FIELDS)  
                    .body(json.toString(), String.class);

        RestTemplate template = new RestTemplate();

        try {
            ResponseEntity<String> resp = template.exchange(req, String.class);
            String payload = resp.getBody();

            return payload;
        } catch(Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
}
