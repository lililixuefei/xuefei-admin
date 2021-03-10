package com.xuefei.eladminsystem;

import com.alibaba.druid.filter.config.ConfigTools;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EladminSystemApplicationTests {

    @Test
    void contextLoads() {

    }

    public static void main(String[] args) {
        String passWord = "Li123456";
        try {
            ConfigTools.main(new String[]{passWord});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
