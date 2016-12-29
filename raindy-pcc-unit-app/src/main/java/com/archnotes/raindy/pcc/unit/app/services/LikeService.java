package com.archnotes.raindy.pcc.unit.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.List;
import java.util.Set;

/**
 * Created by zhangyouce on 2016/12/23.
 */
@Service
public class LikeService extends BaseService{

    @Autowired
    private FollowService followService;

    public LikeService() {
        super("like");
    }

    public boolean isLike(String targetId, String userId) {
        logger.info("isLike:" + targetId);
        fillRedis(targetId);
        try (Jedis jedis = pool.getResource()) {
            double score = jedis.zscore(this.prefix + targetId, userId);
            logger.info("isLike:" + score);

            return true;

        } catch (Exception e) {
            logger.error("isLike error:" + e.getMessage());
        }
        logger.info("isLike false");
        return false;
    }


    public boolean like(String targetId, String ownId, String userId) {
        fillRedis(targetId);
        if (isLike(targetId, userId)) {
            return false;
        }
        boolean isFollow = followService.checkFollow(ownId, userId);
        try (Jedis jedis = pool.getResource()) {
            double score;
            if (isFollow) {
                Set<Tuple> sets = jedis.zrevrangeByScoreWithScores(this.prefix + targetId, 2000000, 1000000, 0, 1);
                if (sets.size() > 0) {
                    Tuple tuple = sets.toArray(new Tuple[]{})[0];
                    score = tuple.getScore() + 1;
                } else {
                    score = 1000001;
                }
            } else {
                Set<Tuple> sets = jedis.zrevrangeByScoreWithScores(this.prefix + targetId, 1000000, 0, 0, 1);
                if (sets.size() > 0) {
                    Tuple tuple = sets.toArray(new Tuple[]{})[0];
                    score = tuple.getScore() + 1;
                } else {
                    score = 1;
                }
            }
            jedis.lpush("like", targetId + ":" + ownId + ":"+ userId + ":" + (isFollow?1:0));
            jedis.zadd(this.prefix + targetId, score, userId);

        }
        return true;
    }


    public Set<String> getLikes(String target_id) {
        logger.info("getLikes:" + target_id);
        fillRedis(target_id);
        return getLikesFromRedis(target_id);

    }

    public long getLikesCount(String target_id) {
        fillRedis(target_id);
        return getLikesCountFromRedis(target_id);
    }


    private Set<String> getLikesFromRedis(String target_id) {
        logger.info("getLikesFromRedis:" + target_id);
        try (Jedis jedis = pool.getResource()) {
            return jedis.zrevrange(this.prefix + target_id, 0,20);
        }
    }

    private long getLikesCountFromRedis(String target_id) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.zcard(this.prefix + target_id);
        }
    }

    @Override
    protected void getFromDb(String target_id) {
        synchronized (target_id) {
            //1 get follow likes
            List<Integer> followLikes = getLikesFromDb(target_id, 1);

            List<Integer> otherLikes = getLikesFromDb(target_id, 0);
            try (Jedis jedis = pool.getResource()) {
                for (int id : followLikes) {
                    int i = 0;
                    jedis.zadd(this.prefix + target_id, 1000000 + i++, id+"");
                }

                for (int id : otherLikes) {
                    int i = 0;
                    jedis.zadd(this.prefix + target_id, i++, id+"");
                }
            }
        }
    }

    private List<Integer> getLikesFromDb(String common_id, int type) {
        return this.template.queryForList("select user_id from target_like where target_id=? and type=? order by id asc ",Integer.class,common_id,type);
    }

}
