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
@RequestMapping("user")
public class UserController {

    @Autowired
    private DispatchService dispatchService;


    @RequestMapping(value = "/new", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "添加用户")
    public Result userAdd(@RequestParam(value = "user_name") String userName){
        return new Result(true, dispatchService.addUser(userName));
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取用户")
    public Result userGet(@RequestParam(value = "user_id") String userId){
        return new Result(true, dispatchService.getUser(userId));
    }

    @RequestMapping(value = "/follow", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "follow用户")
    public Result userFollow(@RequestParam(value = "source_uid") String sourceUid,
                             @RequestParam(value = "follow_uid") String followUid){
        return new Result(true, dispatchService.followUser(sourceUid, followUid));
    }

}
