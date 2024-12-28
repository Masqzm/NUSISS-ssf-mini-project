package ssf.miniproject.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Jio {
    private String id;   
    private String posterName;
    private Restaurant restaurant;

    // @NotNull(message = "Please select a date")
    // @DateTimeFormat(pattern="yyyy-MM-dd")
    // @Future(message = "Please enter a valid date")
    // private Date bookingDate;               // inclusive of time


    @NotNull(message = "Please select a date")
    @Future(message = "Please enter a valid date")
    private LocalDate date;

    @NotNull(message = "Please select a time")
    private LocalTime time;

    private int capacity;
    private boolean jioingForPromo;

    @Size(min = 1, message = "Please select at least one topic")
    private List<String> topics;
    private List<String> attendeesNameList;   // inclusive of poster
    
    @Override
    public String toString() {
        return "Jio [id=" + id + ", posterName=" + posterName + ", restaurant=" + restaurant + ", date="
                + date + ", time=" + time + ", capacity=" + capacity + ", jioingForPromo=" + jioingForPromo + ", topics="
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
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public LocalTime getTime() {
        return time;
    }
    public void setTime(LocalTime time) {
        this.time = time;
    }
    public int getCapacity() {
        return capacity;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    public boolean isJioingForPromo() {
        return jioingForPromo;
    }
    public void setJioingForPromo(boolean jioingForPromo) {
        this.jioingForPromo = jioingForPromo;
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
