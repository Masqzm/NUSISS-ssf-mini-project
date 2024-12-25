package ssf.miniproject.models;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import ssf.miniproject.config.Constants;

public class Restaurant {
    private String name;
    private String address;
    private String summary;         // restaurant's description, if any (NOTE: DO NOT USE AS IT IS A KNOWN ISSUE ON GOOGLE SIDE (HAVEN'T BEEN FIXED YET))
    private String googleMapsURI;
    private String websiteURI;
    
    // private String googlePhotosID;
    // private int googlePhotosMaxHtPx;
    // private int googlePhotosMaxWidthPx;
    private String googlePhotosURI; 

    private String startPrice;      // lowest price
    private String endPrice;        // highest price
    private String priceRange;
    
    private double rating;
    private int userRatingCount;

    //private boolean hasDineIn;

    private List<String> openingHoursList;  // list of opening hours per day
    private List<String> cuisinesList;      // ADD-ON FEATURE, NOT PRIORITY (compare against data/fnb_types.txt)
    private List<Review> reviewsList;       // ADD ON FEATURE, NOT PRIORITY

    private List<Jio> jiosList;             // stores all jios users has made

    public static List<Restaurant> jsonToRestaurant(String json, String googleAPIkey) {
        if(json == null || json.trim().equals("{}"))
            return null;
        
        List<Restaurant> restaurantsList = new ArrayList<>();

        JsonReader reader = Json.createReader(new StringReader(json));
        JsonArray jsonRestArr = reader.readObject().getJsonArray("places");
        
        for(int i = 0; i < jsonRestArr.size(); i++) {
            JsonObject j = jsonRestArr.getJsonObject(i);
            
            // Check and discard (skip) restaurants without DineIn option available
            if(!j.getBoolean("dineIn"))
                continue;
            
            Restaurant rest = new Restaurant();

            rest.setName(j.getJsonObject("displayName").getString("text"));
            rest.setAddress(j.getString("formattedAddress"));
            rest.setRating(j.getJsonNumber("rating").doubleValue());
            rest.setUserRatingCount(j.getInt("userRatingCount"));
            rest.setGoogleMapsURI(j.getString("googleMapsUri"));
            
            if(j.containsKey("websiteUri"))
                rest.setWebsiteURI(j.getString("websiteUri"));

            if(j.containsKey("photos")) {
                JsonObject jFirstPhoto = j.getJsonArray("photos").getJsonObject(0);

                String parameters = "maxHeightPx=" + jFirstPhoto.getInt("heightPx") + "&maxWidthPx=" + jFirstPhoto.getInt("widthPx");
                
                String uri = Constants.GOOGLE_PLACES_PHOTO_URI.replace("NAME", jFirstPhoto.getString("name"))
                            .replace("API_KEY", googleAPIkey)
                            .replace("PARAMETERS", parameters);

                rest.setGooglePhotosURI(uri);
            }

            if(j.containsKey("priceRange")) {
                JsonObject jPriceRange = j.getJsonObject("priceRange");

                rest.setStartPrice(jPriceRange.getJsonObject("startPrice").getString("units"));
                rest.setEndPrice(jPriceRange.getJsonObject("endPrice").getString("units"));

                String priceRangeStr = "Price range: $" + rest.getStartPrice() + "-" + rest.getEndPrice();
                rest.setPriceRange(priceRangeStr);
            }

            if( j.containsKey("regularOpeningHours") &&
                j.getJsonObject("regularOpeningHours").containsKey("weekdayDescriptions")) {
                JsonArray jOpeningHrsArr = j.getJsonObject("regularOpeningHours").getJsonArray("weekdayDescriptions");
                List<String> openingHrs = new ArrayList<>();

                for(int k = 0; k < jOpeningHrsArr.size(); k++) {
                    openingHrs.add(jOpeningHrsArr.getString(k));
                }

                rest.setOpeningHoursList(openingHrs);
            }

            restaurantsList.add(rest);
        }

        return restaurantsList;
    }
    
    @Override
    public String toString() {
        return "Restaurant: \nname=" + name + "\naddress=" + address + "\ngoogleMapsURI=" + googleMapsURI
                + "\ngooglePhotosURI=" + googlePhotosURI + "\nwebsiteURI=" + websiteURI + "\nstartPrice=" 
                + startPrice + "\nendPrice=" + endPrice + "\npriceRange=" + priceRange + "\nrating=" 
                + rating + "\nuserRatingCount=" + userRatingCount + "\nopeningHoursList=" + openingHoursList 
                + "\n\n";
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
    public String getSummary() {
        return summary;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }
    public String getGoogleMapsURI() {
        return googleMapsURI;
    }
    public void setGoogleMapsURI(String googleMapsURI) {
        this.googleMapsURI = googleMapsURI;
    }
    public String getWebsiteURI() {
        return websiteURI;
    }
    public void setWebsiteURI(String websiteURI) {
        this.websiteURI = websiteURI;
    }
    public String getGooglePhotosURI() {
        return googlePhotosURI;
    }
    public void setGooglePhotosURI(String googlePhotosURI) {
        this.googlePhotosURI = googlePhotosURI;
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
    public List<String> getCuisinesList() {
        return cuisinesList;
    }
    public void setCuisinesList(List<String> cuisinesList) {
        this.cuisinesList = cuisinesList;
    }
    public List<Review> getReviewsList() {
        return reviewsList;
    }
    public void setReviewsList(List<Review> reviewsList) {
        this.reviewsList = reviewsList;
    }
}
