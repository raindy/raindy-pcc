package com.archnotes.raindy.pcc.unit.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Created by zhangyouce on 2016/12/24.
 */
@Service
public class UserService extends BaseService {


    @Autowired
    private IDGeneratorService idGeneratorService;

    public UserService() {
        super("user");
    }

    public boolean add(String userName) {
        int userId = idGeneratorService.getNext();
        try (Jedis jedis = pool.getResource()) {
            jedis.lpush("user_new", userId + ":" + userName);
            jedis.set(this.prefix + userId, userName);
//            jedis.publish("user_new", user_id + ":" + user_name);
        }
        return true;
    }

    public String get(String userId) {
        logger.info("get:" + userId);
        fillRedis(userId);
        try (Jedis jedis = pool.getResource()) {
            return jedis.get(this.prefix + userId);
        }
    }

    @Override
    protected void getFromDb(String target_id) {
        String user_name = getUserFromDb(target_id);
        try (Jedis jedis = pool.getResource()) {
            jedis.set("user:" + target_id, user_name);
        }
    }
    private String getUserFromDb(String userId){
        logger.info("getUserFromDb:" + userId);
        return this.template.queryForObject("select user_name from account where user_id=?", String.class, userId);
    }
}
