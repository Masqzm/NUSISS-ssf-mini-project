package ssf.miniproject.repo;

import java.util.List;
import java.util.stream.Collectors;

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

    // HEXISTS JIOS
    // HGET JIOS <id>
    public String getJioByID(String id) {
        if(!template.opsForHash().hasKey(Constants.REDIS_KEY_JIOS, id))
            return null;
            
        return template.opsForHash().get(Constants.REDIS_KEY_JIOS, id).toString();
    }

    // HLEN JIOS
    // HVALS JIOS
    public List<String> getAllJioJSONs() {
        if(template.opsForHash().size(Constants.REDIS_KEY_JIOS) > 0)
            return template.opsForHash().values(Constants.REDIS_KEY_JIOS)
                            .stream()
                            .map(Object::toString)
                            .collect(Collectors.toList());

        return null;
    }

    // HDEL JIOS <id>
    public void delete(String id) {
        template.opsForHash().delete(Constants.REDIS_KEY_JIOS, id);
    }
}
