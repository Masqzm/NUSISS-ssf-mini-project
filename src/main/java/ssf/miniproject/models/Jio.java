package ssf.miniproject.models;

import java.util.Date;
import java.util.List;

public class Jio {
    private String posterID;
    private Restaurant restaurant;
    private Date bookingDate;               // inclusive of time
    private int capacity;
    private boolean isJioingForPromo;

    private List<String> topics;
    private List<String> attendeesIDList;   // inclusive of poster

    
    public String getPosterID() {
        return posterID;
    }
    public void setPosterID(String posterID) {
        this.posterID = posterID;
    }
    public Restaurant getRestaurant() {
        return restaurant;
    }
    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
    public Date getBookingDate() {
        return bookingDate;
    }
    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }
    public int getCapacity() {
        return capacity;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    public boolean isJioingForPromo() {
        return isJioingForPromo;
    }
    public void setJioingForPromo(boolean isJioingForPromo) {
        this.isJioingForPromo = isJioingForPromo;
    }
    public List<String> getTopics() {
        return topics;
    }
    public void setTopics(List<String> topics) {
        this.topics = topics;
    }
    public List<String> getAttendeesIDList() {
        return attendeesIDList;
    }
    public void setAttendeesIDList(List<String> attendeesIDList) {
        this.attendeesIDList = attendeesIDList;
    }

    
}
