package com.archnotes.raindy.pcc.unit.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Created by zhangyouce on 2016/12/24.
 */
@Service
public class FollowService extends BaseService {

    public FollowService() {
        super("follow");
    }

    public boolean addFollow(String sourceUid, String followUid) {
        fillRedis(sourceUid);
        try (Jedis jedis = pool.getResource()) {
            if (jedis.sismember(this.prefix + sourceUid, followUid)) {
                return false;
            }
            jedis.lpush("follow_new", sourceUid + ":" + followUid);
            jedis.sadd(this.prefix + sourceUid, followUid);
        }
        return true;

    }

    public boolean checkFollow(String sourceUid, String followUid) {
        fillRedis(sourceUid);
        try (Jedis jedis = pool.getResource()) {
            return jedis.sismember(this.prefix + sourceUid, followUid);
        }
    }

    @Override
    protected void getFromDb(String sourceUid) {
        List<Integer> follows = getFollowFromDb(sourceUid);

        try (Jedis jedis = pool.getResource()) {
            //TODO batch add
            for (int id : follows) {
                jedis.sadd(this.prefix + sourceUid, id+"");
            }
        }
    }
    private List<Integer> getFollowFromDb(String userId){
        return this.template.queryForList("select follow_uid from follow where source_uid=?",Integer.class,userId);
    }
}
