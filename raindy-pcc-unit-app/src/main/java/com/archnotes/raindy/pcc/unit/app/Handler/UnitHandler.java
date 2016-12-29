package com.archnotes.raindy.pcc.unit.app.Handler;

import com.archnotes.raindy.pcc.unit.api.UnitService;
import com.archnotes.raindy.pcc.unit.app.services.FollowService;
import com.archnotes.raindy.pcc.unit.app.services.LikeService;
import com.archnotes.raindy.pcc.unit.app.services.UserService;
import com.twitter.util.Future;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Created by zhangyouce on 2016/12/26.
 */
@Service
public class UnitHandler implements UnitService.FutureIface {

    @Autowired
    private LikeService likeService;

    @Autowired
    private UserService userService;

    @Autowired
    private FollowService followService;

    @Override
    public Future<Boolean> addUser(String name) {
        return Future.value(userService.add(name));
    }

    @Override
    public Future<String> getUser(String userId) {
        return Future.value(userService.get(userId));
    }

    @Override
    public Future<Boolean> followUser(String sourceUid, String followUid) {

        return  Future.value(followService.addFollow(sourceUid, followUid));
    }

    @Override
    public Future<Boolean> like(String targetId, String ownId, String userId) {
        return Future.value(likeService.like(targetId, ownId, userId));
    }

    @Override
    public Future<Set<String>> getLikes(String targetId) {
        return Future.value(likeService.getLikes(targetId));
    }

    @Override
    public Future<Long> getLikesCount(String targetId) {
        return Future.value(likeService.getLikesCount(targetId));
    }

    @Override
    public Future<Boolean> isLike(String targetId, String userId) {
        return Future.value(likeService.isLike(targetId, userId));
    }
}
