package com.archnotes.raindy.pcc.web.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by zhangyouce on 2016/12/26.
 */
@Service
public class ConfigService {
    @Autowired
    protected JedisPool pool;

    public String getNode(String id) {
        try (Jedis jedis = pool.getResource()) {
            String server = jedis.get("node_" + id);
            if (server != null) {
                String[] arr = server.split(":");
            }
        }
        return null;
    }
}
