package ssf.miniproject.models;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import ssf.miniproject.config.Constants;

public class Restaurant {
    private String id;
    private String name;
    private String address;
    private String googleMapsURI;
    private String googlePhotosURI; 
    private String websiteURI;

    private String startPrice;      // lowest price
    private String endPrice;        // highest price
    private String priceRange;
    
    private double rating;
    private int userRatingCount;

    //private boolean hasDineIn;

    private List<String> openingHoursList;  // list of opening hours per day
    //private List<String> cuisinesList;      // ADD-ON FEATURE, NOT PRIORITY (compare against data/fnb_types.txt)
    //private List<Review> reviewsList;       // ADD ON FEATURE, NOT PRIORITY

    //private List<Jio> jiosList;             // stores all jios users has made

    public Restaurant() {}
    public Restaurant(String id, String name, String address, String googleMapsURI, String googlePhotosURI,
            String websiteURI, String startPrice, String endPrice, String priceRange, double rating,
            int userRatingCount, List<String> openingHoursList) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.googleMapsURI = googleMapsURI;
        this.googlePhotosURI = googlePhotosURI;
        this.websiteURI = websiteURI;
        this.startPrice = startPrice;
        this.endPrice = endPrice;
        this.priceRange = priceRange;
        this.rating = rating;
        this.userRatingCount = userRatingCount;
        this.openingHoursList = openingHoursList;
    }

    // To parse search results response
    public static List<Restaurant> jsonToRestaurantList(String json, String googleAPIkey) {
        if(json == null || json.trim().equals("{}"))
            return null;
        
        List<Restaurant> restaurantsList = new ArrayList<>();

        JsonReader reader = Json.createReader(new StringReader(json));
        JsonArray jsonRestArr = reader.readObject().getJsonArray("places");
        
        for(int i = 0; i < jsonRestArr.size(); i++) {
            JsonObject j = jsonRestArr.getJsonObject(i);
            
            // Check and discard (skip) restaurants without DineIn option available
            if(j.get("dineIn") == null || !j.getBoolean("dineIn"))
                continue;

            // Also skip restaurants not within SG (or doesn't have SG postal code)
            if(!j.getString("formattedAddress").contains("Singapore"))
                continue;
            
            Restaurant rest = new Restaurant();

            rest.setId(j.getString("googleMapsUri").split("cid=")[1]);
            rest.setName(j.getJsonObject("displayName").getString("text"));
            rest.setAddress(j.getString("formattedAddress"));

            if(j.containsKey("rating"))
                rest.setRating(j.getJsonNumber("rating").doubleValue());
            if(j.containsKey("userRatingCount"))
                rest.setUserRatingCount(j.getInt("userRatingCount"));

            rest.setGoogleMapsURI(j.getString("googleMapsUri"));

            if(j.containsKey("photos")) {
                JsonObject jFirstPhoto = j.getJsonArray("photos").getJsonObject(0);

                String parameters = "maxHeightPx=" + jFirstPhoto.getInt("heightPx") + "&maxWidthPx=" + jFirstPhoto.getInt("widthPx");
                
                String uri = Constants.GOOGLE_PLACES_PHOTO_URI.replace("NAME", jFirstPhoto.getString("name"))
                            .replace("API_KEY", googleAPIkey)
                            .replace("PARAMETERS", parameters);

                rest.setGooglePhotosURI(uri);
            }
            
            if(j.containsKey("websiteUri"))
                rest.setWebsiteURI(j.getString("websiteUri"));

            if(j.containsKey("priceRange")) {
                JsonObject jPriceRange = j.getJsonObject("priceRange");

                rest.setStartPrice(jPriceRange.getJsonObject("startPrice").getString("units"));
                rest.setEndPrice(jPriceRange.getJsonObject("endPrice").getString("units"));

                String priceRangeStr = "Cost: $" + rest.getStartPrice() + "-" + rest.getEndPrice();
                rest.setPriceRange(priceRangeStr);
            }

            if( j.containsKey("regularOpeningHours") &&
                j.getJsonObject("regularOpeningHours").containsKey("weekdayDescriptions")) {
                JsonArray jOpeningHrsArr = j.getJsonObject("regularOpeningHours").getJsonArray("weekdayDescriptions");
                List<String> openingHrs = new ArrayList<>();

                for(int k = 0; k < jOpeningHrsArr.size(); k++) {
                    // Add opening hour with a bit of reformatting
                    // String oh = jOpeningHrsArr.getString(k);
                    // openingHrs.add(oh.replace("y:", "y/n"));

                    openingHrs.add(jOpeningHrsArr.getString(k));
                }
                
                rest.setOpeningHoursList(openingHrs);
            }

            restaurantsList.add(rest);
        }

        return restaurantsList;
    }

    // To parse restaurant json saved
    public static Restaurant jsonToRestaurant(String json) {
        if(json == null)
            return null;

        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject j = reader.readObject();

        JsonArray jArr = j.getJsonArray("openingHoursList");
        
        List<String> openingHrs = new ArrayList<>();
        
        for(int i = 0; i < jArr.size(); i++) 
            openingHrs.add(jArr.getString(i));

        Restaurant rest = new Restaurant(j.getString("id"),
                                j.getString("name"),
                                j.getString("address"),
                                j.getString("googleMapsURI"),
                                j.getString("googlePhotosURI"),
                                j.getString("websiteURI"),
                                j.getString("startPrice"),
                                j.getString("endPrice"),
                                j.getString("priceRange"),
                                j.getJsonNumber("rating").doubleValue(),
                                j.getInt("userRatingCount"),
                                openingHrs);
                                
        return rest;
    }
    public String toJson() {
        JsonArrayBuilder jsonArrB = Json.createArrayBuilder();
        
        for (String openingHrs : openingHoursList)
            jsonArrB.add(openingHrs);

        if(this.id == null)
            this.id = "";
        if(this.googlePhotosURI == null)
            this.googlePhotosURI = "";
        if(this.websiteURI == null)
            this.websiteURI = "";

        JsonObject job = Json.createObjectBuilder()
                        .add("id", this.id)
                        .add("name", this.name)
                        .add("address", this.address)
                        .add("googleMapsURI", this.googleMapsURI)
                        .add("googlePhotosURI", this.googlePhotosURI)
                        .add("websiteURI", this.websiteURI)
                        .add("startPrice", this.startPrice)
                        .add("endPrice", this.endPrice)
                        .add("priceRange", this.priceRange)
                        .add("rating", this.rating)
                        .add("userRatingCount", this.userRatingCount)
                        .add("openingHoursList", jsonArrB.build())
                        .build(); 

        return job.toString();
    }
    
    @Override
    public String toString() {
        return toJson();
    }
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getGoogleMapsURI() {
        return googleMapsURI;
    }
    public void setGoogleMapsURI(String googleMapsURI) {
        this.googleMapsURI = googleMapsURI;
    }
    public String getGooglePhotosURI() {
        return googlePhotosURI;
    }
    public void setGooglePhotosURI(String googlePhotosURI) {
        this.googlePhotosURI = googlePhotosURI;
    }
    public String getWebsiteURI() {
        return websiteURI;
    }
    public void setWebsiteURI(String websiteURI) {
        this.websiteURI = websiteURI;
    }
    public String getStartPrice() {
        return startPrice;
    }
    public void setStartPrice(String startPrice) {
        this.startPrice = startPrice;
    }
    public String getEndPrice() {
        return endPrice;
    }
    public void setEndPrice(String endPrice) {
        this.endPrice = endPrice;
    }
    public String getPriceRange() {
        return priceRange;
    }
    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }
    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }
    public int getUserRatingCount() {
        return userRatingCount;
    }
    public void setUserRatingCount(int userRatingCount) {
        this.userRatingCount = userRatingCount;
    }
    public List<String> getOpeningHoursList() {
        return openingHoursList;
    }
    public void setOpeningHoursList(List<String> openingHoursList) {
        this.openingHoursList = openingHoursList;
    }
    // public List<String> getCuisinesList() {
    //     return cuisinesList;
    // }
    // public void setCuisinesList(List<String> cuisinesList) {
    //     this.cuisinesList = cuisinesList;
    // }
    // public List<Review> getReviewsList() {
    //     return reviewsList;
    // }
    // public void setReviewsList(List<Review> reviewsList) {
    //     this.reviewsList = reviewsList;
    // }
}
