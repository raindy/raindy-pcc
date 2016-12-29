package com.archnotes.raindy.pcc.unit.app;

import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

/**
 * Created by zhangyouce on 2016/12/27.
 */

@Configuration
public class TestConfig {

    public @Bean
    Server h2WebServer() throws SQLException {
        return Server.createWebServer("-web", "-webAllowOthers", "-webDaemon", "-webPort", "8082").start();
    }
}
