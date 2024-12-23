package ssf.miniproject.models;

// ADD ON FEATURE, NOT PRIORITY
public class Review {
    private String name;
    private String userPhotoURI;    
    private String relativePublishTimeDescription;
    private String text;
    private double rating;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUserPhotoURI() {
        return userPhotoURI;
    }
    public void setUserPhotoURI(String userPhotoURI) {
        this.userPhotoURI = userPhotoURI;
    }
    public String getRelativePublishTimeDescription() {
        return relativePublishTimeDescription;
    }
    public void setRelativePublishTimeDescription(String relativePublishTimeDescription) {
        this.relativePublishTimeDescription = relativePublishTimeDescription;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }
}
