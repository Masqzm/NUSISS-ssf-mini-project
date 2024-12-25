package ssf.miniproject.config;

import jakarta.json.JsonObject;

public class Constants {
    public static final String REDIS_TEMPLATE = "redis-0";
    public static final String REDIS_KEY_SEARCH_CACHE = "RESULTS";      // search results cache
    public static final long REDIS_CACHE_TTL_HRS = 10;                  // how long to cache results in hrs TODO: change back to 1h or btr soln: clear on session close

    
    public static final String GOOGLE_PLACES_TEXTSEARCH_URL = "https://places.googleapis.com/v1/places:searchText";
    public static final String GOOGLE_PLACES_PHOTO_URI = "https://places.googleapis.com/v1/NAME/media?key=API_KEY&PARAMETERS";

    public static String GOOGLE_PLACES_TEXTSEARCH_FIELDS;
    public static JsonObject GOOGLE_PLACES_TEXTSEARCH_LOC_JSON;     // to be loaded from file    
}
