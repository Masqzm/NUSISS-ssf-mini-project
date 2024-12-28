package ssf.miniproject.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import ssf.miniproject.config.Constants;

@Repository
public class JioRepo {
    @Autowired @Qualifier(Constants.REDIS_TEMPLATE)
    private RedisTemplate<String, String> template;

    // HSET JIOS <id> {jio (json str)}
    public void saveJio(String id, String json) {
        template.opsForHash().put(Constants.REDIS_KEY_JIOS, id, json);
    }

    // HGET JIOS <id>
    public String getJioByID(String id) {
        if(!template.opsForHash().hasKey(Constants.REDIS_KEY_JIOS, id))
            return null;
            
        return template.opsForHash().get(Constants.REDIS_KEY_JIOS, id).toString();
    }
}
