package ssf.miniproject.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import ssf.miniproject.config.Constants;
import ssf.miniproject.models.User;

@Repository
public class UserRepo {
    @Autowired @Qualifier(Constants.REDIS_TEMPLATE)
    private RedisTemplate<String, String> template;

    // HEXISTS USERS <name>
    public boolean checkUserExists(String name) {
        return template.opsForHash().hasKey(Constants.REDIS_KEY_USER, name);
    }

    // HSET USERS <name> {userJSON}
    public void createUser(User user) {
        template.opsForHash().put(Constants.REDIS_KEY_USER, user.getName(), user.toJson());
    }
}
