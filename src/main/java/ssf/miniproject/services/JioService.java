package ssf.miniproject.services;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import ssf.miniproject.models.Jio;
import ssf.miniproject.models.Restaurant;
import ssf.miniproject.repo.JioRepo;
import ssf.miniproject.repo.SearchRepo;

@Service
public class JioService {
    @Autowired
    JioRepo jioRepo;
    
    //@Autowired @Lazy        // to break the circular dependency 
    //SearchService searchSvc;
    @Autowired
    SearchRepo searchRepo;

    public String saveJio(String name, Restaurant rest, Jio jio) {
        String id = UUID.randomUUID().toString().substring(0, 8);
        List<String> attendeesNameList = new ArrayList<>();
        attendeesNameList.add(name);    // add poster as attendee too

        // Adding of additional details
        jio.setId(id);
        jio.setPosterName(name);
        jio.setRestaurantID(rest.getId());
        jio.setAttendeesNameList(attendeesNameList);
        
        jioRepo.saveJio(id, jio.toJson());

        // Binding jio to restaurant & saving to db
        rest.getJioIDList().add(id);
        //searchSvc.saveRestaurant(rest, true);
        searchRepo.saveRestaurant(rest.getId(), rest.toJson());

        return id;
    }    

    public void updateJio(Jio jio, String attendee, boolean isJoining) {
        if(isJoining && !jio.getAttendeesNameList().contains(attendee))
            jio.getAttendeesNameList().add(attendee);

        if(!isJoining && jio.getAttendeesNameList().contains(attendee))
            jio.getAttendeesNameList().remove(attendee);

        jioRepo.saveJio(jio.getId(), jio.toJson());
    }

    public void deleteJioByID(String id) {
        jioRepo.delete(id);
    }

    public Jio getJioByID(String id) {
        return Jio.jsonToJio(jioRepo.getJioByID(id));
    }

    // Updates all jios of a restaurant (removes them if its in the past)
    public Restaurant updateRestaurantJios(Restaurant rest) {
        // Safe check
        if(!rest.getJioIDList().isEmpty()) 
            return rest;

        List<String> upcomingJiosList = new ArrayList<>();

        for(String jioID : rest.getJioIDList()) {
            // Get jio from repo
            Jio jio = getJioByID(jioID);

            // Get jio's datetime & current datetime
            long jioUnixTimestamp = Jio.convertToUnixTimestamp(jio.getDate(), jio.getTime());
            long unixTimestampNow = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

            // Add jio event to restaurant if its not over (in the future)
            if(unixTimestampNow < jioUnixTimestamp) 
                upcomingJiosList.add(jio.getId());

            // Remove from db if its over
            else
                jioRepo.delete(jio.getId());
        }            

        // Update jioIDs of this restaurant
        rest.setJioIDList(upcomingJiosList);

        return rest;
    }
}
