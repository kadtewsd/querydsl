package com.kasakaid.jpaandquerydsl;

import com.kasakaid.jpaandquerydsl.spring.MyResource;
import com.kasakaid.jpaandquerydsl.spring.TestConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {Application.class, TestConfig.class})
@ActiveProfiles("test")
@Slf4j
public abstract class AbstractBaseTest {

    @Autowired
    protected ApplicationContext context;

    // 実行順序がよくわからないのでサブクラスからコールする
//    @Before
    public void setup() {
        myResource = context.getBean(MyResource.class);
    }
    @Rule
    //public にしないと怒られる
    public MyResource myResource;
}

