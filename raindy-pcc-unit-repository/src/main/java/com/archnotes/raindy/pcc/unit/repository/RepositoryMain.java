package com.archnotes.raindy.pcc.unit.repository;

import com.archnotes.raindy.pcc.unit.repository.services.FollowService;
import com.archnotes.raindy.pcc.unit.repository.services.LikeService;
import com.archnotes.raindy.pcc.unit.repository.services.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Properties;

/**
 * Created by zhangyouce on 2016/12/27.
 */
public class RepositoryMain {
    public static void main(String[] args) {
        String node = "";
        if (args.length > 0) {
            node = args[0];

            Properties pp = System.getProperties();
            pp.put("node", node);
        }
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);

        ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) ctx.getBean("taskExecutor");

        UserService userService = (UserService)ctx.getBean("userService");
        LikeService likeService = (LikeService)ctx.getBean("likeService");
        FollowService followService = (FollowService)ctx.getBean("followService");

        taskExecutor.execute(userService);
        taskExecutor.execute(likeService);
        taskExecutor.execute(followService);

        while(true) {

            userService.optimize();
            likeService.optimize();
            followService.optimize();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
