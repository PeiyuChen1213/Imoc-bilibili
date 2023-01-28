package com.bilibili.test;

import com.bilibili.service.util.RSAUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Chen Peiyu
 * @version 1.0
 * @description 测试类
 * @date 1/20/2023 11:42 PM
 */

@SpringBootTest
public class ServiceTest {
    @Value("${rocketmq.name.server.address}")
    private String nameServerAddr;

    @Test
    public void RSAUtiltest() throws Exception {
        String password = "12345";
        String decrypt = RSAUtil.encrypt(password);
        System.out.println(decrypt);
    }


    @Test
    public void ValueTest() {
        System.out.println(nameServerAddr);
    }
}
