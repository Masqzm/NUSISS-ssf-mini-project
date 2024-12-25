package ssf.miniproject.models;

import java.util.Date;
import java.util.List;

public class Jio {
    private String id;
    private String posterName;
    private Restaurant restaurant;
    private Date bookingDate;               // inclusive of time
    private int capacity;
    private boolean isJioingForPromo;

    private List<String> topics;
    private List<String> attendeesNameList;   // inclusive of poster

    
    
    @Override
    public String toString() {
        return "Jio [id=" + id + ", posterName=" + posterName + ", restaurant=" + restaurant + ", bookingDate="
                + bookingDate + ", capacity=" + capacity + ", isJioingForPromo=" + isJioingForPromo + ", topics="
                + topics + ", attendeesNameList=" + attendeesNameList + "]";
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPosterName() {
        return posterName;
    }
    public void setPosterName(String posterName) {
        this.posterName = posterName;
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
    public List<String> getAttendeesNameList() {
        return attendeesNameList;
    }
    public void setAttendeesNameList(List<String> attendeesNameList) {
        this.attendeesNameList = attendeesNameList;
    }    
}
