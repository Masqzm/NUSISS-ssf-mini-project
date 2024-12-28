package ssf.miniproject.config;

import java.util.List;

import jakarta.json.JsonObject;

public class Constants {
    public static final String REDIS_TEMPLATE = "redis-0";
    public static final String REDIS_KEY_USER = "USERS";      
    public static final String REDIS_KEY_JIOS = "JIOS";    
    public static final String REDIS_KEY_SEARCH_CACHE = "RESULTS";      // search results cache key
    public static final String REDIS_KEY_RESTAURANTS = "RESTAURANTS";   // viewed restaurants stored key  
    public static final long REDIS_CACHE_TTL_HRS = 10;                  // how long to cache results in hrs TODO: change back to 1h or btr soln: clear on session close

    public static final String SESS_ATTR_USER = "currentUser";
    public static final String SESS_ATTR_JIO_RESTAURANT = "storedRestInfo";    // temp storage of restaurant info
    public static final String SESS_ATTR_REDIR_REQ = "redirectRequest";         // session attr key to hold request URI + query (if any) to redirect to upon successful registration/login
    
    public static final String GOOGLE_PLACES_TEXTSEARCH_URL = "https://places.googleapis.com/v1/places:searchText";
    public static final String GOOGLE_PLACES_PHOTO_URI = "https://places.googleapis.com/v1/NAME/media?key=API_KEY&PARAMETERS";

    // "Constants" to be loaded from file    
    public static String GOOGLE_PLACES_TEXTSEARCH_FIELDS;
    public static JsonObject GOOGLE_PLACES_TEXTSEARCH_LOC_JSON;    
    public static List<String> JIO_TOPICS_LIST;
}
