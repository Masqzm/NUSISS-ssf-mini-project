package ssf.miniproject.models;

import java.util.List;

public class Restaurant {
    private String name;
    private String address;
    private String summary;         // restaurant's description, if any (NOTE: DO NOT USE AS IT IS A KNOWN ISSUE ON GOOGLE SIDE (HAVEN'T BEEN FIXED YET))
    private String googleMapsURI;
    private String googlePhotosID;
    private String websiteURI;

    private String priceRange;
    private double startPrice;
    private double endPrice;
    
    private double rating;
    private int userRatingCount;

    //private boolean hasDineIn;

    private List<String> openingHoursList;  // list of opening hours per day
    private List<String> cuisinesList;
    private List<Review> reviewsList;       // ADD ON FEATURE, NOT PRIORITY

    // need some extra fns to convert to display ready string (esp for types->cuisines)
    
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
    public String getGooglePhotosID() {
        return googlePhotosID;
    }
    public void setGooglePhotosID(String googlePhotosID) {
        this.googlePhotosID = googlePhotosID;
    }
    public String getWebsiteURI() {
        return websiteURI;
    }
    public void setWebsiteURI(String websiteURI) {
        this.websiteURI = websiteURI;
    }
    public String getPriceRange() {
        return priceRange;
    }
    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }
    public double getStartPrice() {
        return startPrice;
    }
    public void setStartPrice(double startPrice) {
        this.startPrice = startPrice;
    }
    public double getEndPrice() {
        return endPrice;
    }
    public void setEndPrice(double endPrice) {
        this.endPrice = endPrice;
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
