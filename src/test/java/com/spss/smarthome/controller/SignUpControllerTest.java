package com.spss.smarthome.controller;

import com.spss.smarthome.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SignUpControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;        //自动注入需要测试的服务类

    @Before
    public void setUp() {      //初始化WebApplicationContext
//        mockMvc = MockMvcBuilders.standaloneSetup(new SignUpController()).build();
    }

    @Test
    public void ST_signUp() {      //测试服务层的方法
//        User user = new User("111111111", "123123", "1539255feaj0160");
//        User dbUser = userService.signUp(user);
//        assertEquals(user, dbUser);
    }

    //    curl -i  -H "content-Type:application/json" -X POST -d  '{"userName":"testUser4","password":"123123","phone":"14234556489"}' http://123.206.15.63:8887/SmartHome/user/signUp
    //控制层方法测试未知
    @Test
    public void CT_signUp() {

//        JSONObject obj = new JSONObject();
//        obj.put("userName", "testUser4");
//        obj.put("password", "123123");
//        obj.put("phone", "15342123907");
//        ResultActions mvcResult = mockMvc.perform(
//                MockMvcRequestBuilders.post("/user/signUp")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(obj.toString()));
//        mvcResult.andReturn().getResponse().setCharacterEncoding("UTF-8");
//        mvcResult.andDo(print()).andExpect(status().isOk());
    }


}