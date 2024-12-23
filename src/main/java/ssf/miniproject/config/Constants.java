package ssf.miniproject.config;

import jakarta.json.JsonObject;

public class Constants {
    public static final String REDIS_TEMPLATE = "redis-0";

    
    public static final String GOOGLE_PLACES_TEXTSEARCH_URL = "https://places.googleapis.com/v1/places:searchText";


    // public static final String GOOGLE_PLACES_TEXTSEARCH_FIELDS = """
    // places.displayName,
    // places.formattedAddress,
    // places.googleMapsUri,
    // places.photos,
    // places.types,
    // places.regularOpeningHours,
    // places.priceRange,places.rating,
    // places.userRatingCount,
    // places.websiteUri,
    // places.dineIn,
    // places.reviews,
    // places.editorialSummary
    // """;

    public static String GOOGLE_PLACES_TEXTSEARCH_FIELDS;
    public static JsonObject GOOGLE_PLACES_TEXTSEARCH_LOC_JSON;     // to be loaded from file    
}
