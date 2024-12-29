package ssf.miniproject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ssf.miniproject.models.User;
import ssf.miniproject.repo.UserRepo;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;

    public boolean checkUserExists(String name) {
        return userRepo.checkUserExists(name);
    }

    public void registerUser(User user) {
        userRepo.saveUser(user);
    }

    public User checkUserCredentials(String name, String password) {
        User user = userRepo.getUser(name);

        // User credentials check passes if 
        // user can be found AND password entered matches user's password
        if(user != null && user.getPassword().equals(password))
            return user;
        
        return null;
    }   
}
