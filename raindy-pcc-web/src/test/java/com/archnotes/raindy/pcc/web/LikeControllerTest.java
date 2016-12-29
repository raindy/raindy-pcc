package com.archnotes.raindy.pcc.web;

import com.alibaba.fastjson.JSON;
import com.archnotes.raindy.pcc.common.Result;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertEquals;

/**
 * Created by zhangyouce on 2016/12/28.
 */
@RunWith(SpringRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(classes = AppConfig.class)
@TestPropertySource(value = "classpath:application.properties")
public class LikeControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilters().build();
    }

    @Test
    public void testLike() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/like/new")
                        .param("target_id","1_1")
                        .param("own_id", "1_2")
                        .param("user_id", "1_3")
                )
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("result=" + result.getResponse().getContentAsString());

        Result response= JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(true, response.value);

        result = mockMvc.perform(MockMvcRequestBuilders.post("/like/new")
                .param("target_id","1_1")
                .param("own_id", "1_2")
                .param("user_id", "1_4")
        )
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("result=" + result.getResponse().getContentAsString());

        response= JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
        assertEquals(true, response.value);
    }

    @Test
    public void testGetLikes() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/like/user_list")
                .param("target_id","1_1")
        )
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("result=" + result.getResponse().getContentAsString());

        Result response= JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
//        assertEquals(true, response.value);
    }

    @Test
    public void testGetLikesCount() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/like/count")
                .param("target_id","1_1")
        )
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("result=" + result.getResponse().getContentAsString());

        Result response= JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
//        assertEquals(true, response.value);
    }

    @Test
    public void testIsLike() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/like/is_liked")
                .param("target_id","1_1").param("user_id", "1_3")
        )
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        System.out.println("result=" + result.getResponse().getContentAsString());

        Result response= JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
//        assertEquals(true, response.value);
    }
}
