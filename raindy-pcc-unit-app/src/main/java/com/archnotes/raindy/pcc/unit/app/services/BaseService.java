package com.archnotes.raindy.pcc.unit.app.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by zhangyouce on 2016/12/24.
 */
public abstract class BaseService {

    protected Logger logger = Logger.getLogger(getClass());

    @Autowired
    protected JedisPool pool;
    @Autowired
    protected JdbcTemplate template;

    protected String prefix;
    public BaseService(String prefix) {
        this.prefix = prefix + ":";
    }

    protected void fillRedis(String targetId) {
        synchronized (targetId) {
            boolean isExist = checkExist(targetId);
            if (!isExist) {
                getFromDb(targetId);
            }
        }
    }

    protected boolean checkExist(String target_id) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.exists(prefix + target_id);
        }
    }

    protected abstract void getFromDb(String target_id);
}
