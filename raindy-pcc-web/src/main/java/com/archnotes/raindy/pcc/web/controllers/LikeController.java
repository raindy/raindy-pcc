package com.archnotes.raindy.pcc.web.controllers;

import com.archnotes.raindy.pcc.common.Result;
import com.archnotes.raindy.pcc.web.services.DispatchService;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by zhangyouce on 2016/12/26.
 */
@RestController
@RequestMapping("like")
public class LikeController {

    @Autowired
    private DispatchService dispatchService;


    @RequestMapping(value = "/new", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "点赞")
    public Result like(@RequestParam(value = "target_id") String targetId, @RequestParam(value = "own_id") String ownId, @RequestParam("user_id") String userId){
        return new Result(true, dispatchService.like(targetId, ownId, userId));
    }

    @RequestMapping(value = "/is_liked", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "是否点赞")
    public Result isLiked(@RequestParam(value = "target_id") String targetId, @RequestParam("user_id") String userId){
        return new Result(true, dispatchService.isLike(targetId, userId));
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "点赞数")
    public Result likeCount(@RequestParam(value = "target_id") String targetId){
        return new Result(true, dispatchService.getLikesCount(targetId));
    }

    @RequestMapping(value = "/user_list", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "点赞用户列表")
    public Result userList(@RequestParam(value = "target_id") String targetId
//                           @RequestParam(value = "page") int page,@RequestParam(value = "count") int count
    ){
        return new Result(true, dispatchService.getLikes(targetId));
    }
}
