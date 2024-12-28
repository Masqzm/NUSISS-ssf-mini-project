package ssf.miniproject.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ssf.miniproject.models.Jio;
import ssf.miniproject.models.Restaurant;
import ssf.miniproject.repo.JioRepo;
import ssf.miniproject.repo.UserRepo;

@Service
public class JioService {
    @Autowired
    JioRepo jioRepo;

    @Autowired
    UserRepo userRepo;

    public String saveJio(String name, Restaurant rest, Jio jio) {
        String id = UUID.randomUUID().toString().substring(0, 8);
        List<String> attendeesNameList = new ArrayList<>();
        attendeesNameList.add(name);    // add poster as attendee too

        // Adding of additional details
        jio.setId(id);
        jio.setPosterName(name);
        jio.setRestaurant(rest);
        jio.setAttendeesNameList(attendeesNameList);
        
        jioRepo.saveJio(id, jio.toJson());

        return id;
    }    

    public Jio getJioByID(String id) {
        return Jio.jsonToJio(jioRepo.getJioByID(id));
    }
}
