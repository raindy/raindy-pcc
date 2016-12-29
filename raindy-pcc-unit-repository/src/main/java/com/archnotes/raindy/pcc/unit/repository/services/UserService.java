package com.archnotes.raindy.pcc.unit.repository.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyouce on 2016/12/24.
 */
@Service
public class UserService extends BaseService{

    public UserService() {
        this(50, "user_new");
    }

    public UserService(int batchMax, String queueKey) {
        super(batchMax, queueKey);
    }

    protected void addBatch(List<Object[]> values) {
        this.template.batchUpdate("insert into account(user_id, user_name) values(?, ?)", values);
    }

    protected int add(String user_id, String user_name) {
        return this.template.update("insert into account(user_id, user_name) values(?, ?)", user_id, user_name);
    }


}
