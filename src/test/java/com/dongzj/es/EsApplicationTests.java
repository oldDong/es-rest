package com.dongzj.es;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * User: dongzj
 * Mail: dongzj@shinemo.com
 * Date: 2018/11/8
 * Time: 10:38
 */
@RunWith(SpringRunner.class)
@SpringBootTest
//WEB项目，Junit需要模拟ServletContext，因此我们需要给测试类加上该注解
@WebAppConfiguration
public class EsApplicationTests {

    @Before
    public void init() {
        System.out.println("开始测试--------------");
    }

    @After
    public void after() {
        System.out.println("测试结束---------------");
    }
}
