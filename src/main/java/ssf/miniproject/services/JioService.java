package ssf.miniproject.services;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import ssf.miniproject.models.Jio;
import ssf.miniproject.models.Restaurant;
import ssf.miniproject.models.User;
import ssf.miniproject.repo.JioRepo;
import ssf.miniproject.repo.SearchRepo;
import ssf.miniproject.repo.UserRepo;

@Service
public class JioService {
    @Autowired
    JioRepo jioRepo;
    
    //@Autowired @Lazy        // to break the circular dependency 
    //SearchService searchSvc;
    @Autowired
    SearchRepo searchRepo;

    @Autowired
    UserRepo userRepo;

    public String saveJio(User user, Restaurant rest, Jio jio) {
        String id = UUID.randomUUID().toString().substring(0, 8);
        List<String> attendeesNameList = new ArrayList<>();
        attendeesNameList.add(user.getName());    // add poster as attendee too

        // Adding of additional details
        jio.setId(id);
        jio.setPosterName(user.getName());
        jio.setRestaurantID(rest.getId());
        jio.setAttendeesNameList(attendeesNameList);
        
        jioRepo.saveJio(id, jio.toJson());

        // Binding jioID to user & saving to db
        if(user.getJioIDsList() == null)
            user.setJioIDsList(new ArrayList<String>());
        user.getJioIDsList().add(id);
        userRepo.saveUser(user);

        // Binding jioID to restaurant & saving to db
        rest.getJioIDList().add(id);
        searchRepo.saveRestaurant(rest.getId(), rest.toJson());

        return id;
    }    

    public void updateJio(Jio jio, User newAttendee, boolean isJoining) {
        // Update user joining
        if(isJoining && !jio.getAttendeesNameList().contains(newAttendee.getName())) 
            jio.getAttendeesNameList().add(newAttendee.getName());
        
        // Update user cancelling
        if(!isJoining && jio.getAttendeesNameList().contains(newAttendee.getName())) 
            jio.getAttendeesNameList().remove(newAttendee.getName());

        // Binding jioID to user & saving to db
        if(newAttendee.getJioIDsList() == null)
            newAttendee.setJioIDsList(new ArrayList<String>());
        
        if(isJoining) {
            newAttendee.getJioIDsList().add(jio.getId());

            System.out.println(newAttendee.getJioIDsList());
        }
        else {
            if(!newAttendee.getJioIDsList().isEmpty())
                newAttendee.getJioIDsList().remove(jio.getId());

            System.out.println(newAttendee.getJioIDsList());
        }
            
        userRepo.saveUser(newAttendee);

        jioRepo.saveJio(jio.getId(), jio.toJson());
    }

    public void deleteJioByID(String id) {
        jioRepo.delete(id);
    }
    public void deleteJioByIDUpdateUser(String id, User user) {
        jioRepo.delete(id);

        // Binding jioID to user & saving to db
        if(user.getJioIDsList() == null)
            user.setJioIDsList(new ArrayList<String>());
            user.getJioIDsList().add(id);
        userRepo.saveUser(user);
    }

    public Jio getJioByID(String id) {
        return Jio.jsonToJio(jioRepo.getJioByID(id));
    }

    // Updates all jios of a restaurant (removes them if its in the past)
    public Restaurant updateRestaurantJios(Restaurant rest) {
        // Safety catch
        if(!rest.getJioIDList().isEmpty()) 
            return rest;

        List<String> upcomingJiosList = checkJioValidity(rest.getJioIDList());      

        // Update jioIDs of this restaurant
        rest.setJioIDList(upcomingJiosList);

        return rest;
    }

    // Retrieves list of jios tied to user
    public List<Jio> getUserJios(User user) {
        if(user.getJioIDsList() == null || user.getJioIDsList().isEmpty())
            return null;

        System.out.println("bef: "+user.getJioIDsList());
        List<String> upcomingJiosList = checkJioValidity(user.getJioIDsList());
        System.out.println("after: "+upcomingJiosList);
        // Update user's vaild jio list & update user db
        user.setJioIDsList(upcomingJiosList);
        userRepo.saveUser(user);

        List<Jio> jiosList = new ArrayList<>();

        for(String jioID : upcomingJiosList) 
            jiosList.add(Jio.jsonToJio(jioRepo.getJioByID(jioID)));

        // Sort list by each Jio's unix timestamp
        jiosList.sort((jio1, jio2) -> {
            long unix1 = Jio.convertToUnixTimestamp(jio1.getDate(), jio1.getTime());
            long unix2 = Jio.convertToUnixTimestamp(jio2.getDate(), jio2.getTime());
            
            return Long.compare(unix1, unix2);  
        });
        System.out.println("return no prob");
        return jiosList;
    }

    public List<Jio> getAllJios() {
        List<String> jioJSONs = jioRepo.getAllJioJSONs();

        if(jioJSONs == null)
            return null;

        List<Jio> jiosList = new ArrayList<>();

        for(String json : jioJSONs) {
            JsonReader reader = Json.createReader(new StringReader(json));
            JsonObject j = reader.readObject();

            // Get jio's datetime & current datetime
            long jioUnixTimestamp = j.getJsonNumber("unixTimestamp").longValue();
            long unixTimestampNow = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

            // Add jio event to jiosList if its not over (in the future)
            if(unixTimestampNow < jioUnixTimestamp) 
                jiosList.add(Jio.jsonToJio(json));
            // Remove from db if its over
            else
                jioRepo.delete(j.getString("id"));
        }

        // Sort list by each Jio's unix timestamp
        jiosList.sort((jio1, jio2) -> {
            long unix1 = Jio.convertToUnixTimestamp(jio1.getDate(), jio1.getTime());
            long unix2 = Jio.convertToUnixTimestamp(jio2.getDate(), jio2.getTime());
            
            return Long.compare(unix1, unix2);  
        });

        return jiosList;
    }

    private List<String> checkJioValidity(List<String> jioIDList) {
        List<String> upcomingJiosList = new ArrayList<>();

        for(String jioID : jioIDList) {
            // Get jio from repo
            Jio jio = getJioByID(jioID);
            
            // Safety catch
            if(jio == null)
                continue;

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
        
        return upcomingJiosList;
    }
}
