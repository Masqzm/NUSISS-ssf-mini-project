package ssf.miniproject.repo;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import ssf.miniproject.config.Constants;

@Repository
public class SearchRepo {
    @Autowired @Qualifier(Constants.REDIS_TEMPLATE)
    private RedisTemplate<String, String> template;

    // HGET RESULTS <keyword>
    public String getResultsFromCache(String keyword) {
        if(!template.opsForHash().hasKey(Constants.REDIS_KEY_SEARCH_CACHE, keyword))
            return null;
            
        return template.opsForHash().get(Constants.REDIS_KEY_SEARCH_CACHE, keyword).toString();
    }
    // HSET RESULTS <keyword> {results of search - restaurants list (json str)}
    // EXPIRE RESULTS 3600
    public void cacheSearchResults(String keyword, String json) {
        template.opsForHash().put(Constants.REDIS_KEY_SEARCH_CACHE, keyword, json);
        template.expire(keyword, Constants.REDIS_CACHE_TTL_HRS, TimeUnit.HOURS);
    }

    // HGET RESTAURANTS <id>
    public String getRestaurantByID(String id) {
        if(!template.opsForHash().hasKey(Constants.REDIS_KEY_RESTAURANTS, id))
            return null;
            
        return template.opsForHash().get(Constants.REDIS_KEY_RESTAURANTS, id).toString();
    }
    // HSET RESTAURANTS <id> {restaurant (json str)}
    public void saveRestaurant(String id, String json) {
        template.opsForHash().put(Constants.REDIS_KEY_RESTAURANTS, id, json);
    }
}