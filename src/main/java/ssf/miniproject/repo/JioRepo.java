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
}
