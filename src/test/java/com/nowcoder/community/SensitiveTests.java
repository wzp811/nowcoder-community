package com.nowcoder.community;

import com.nowcoder.community.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class SensitiveTests {

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void testSensitiveFilter(){
        String text = "赌博、嫖娼、吸毒和开票都是违法行为！！！";
        text = sensitiveFilter.filter(text);
        System.out.println(text);

        text = "@赌@博@、#嫖#娼#、a吸a毒a和⭐开⭐票⭐都是违法行为！！！";
        text = sensitiveFilter.filter(text);
        System.out.println(text);

        text = "@赌@博@";
        text = sensitiveFilter.filter(text);
        System.out.println(text);

        text = "@赌@博";
        text = sensitiveFilter.filter(text);
        System.out.println(text);
    }

}
