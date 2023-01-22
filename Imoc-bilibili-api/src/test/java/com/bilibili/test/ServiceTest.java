package com.bilibili.test;

import com.bilibili.service.util.RSAUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Chen Peiyu
 * @version 1.0
 * @description 测试类
 * @date 1/20/2023 11:42 PM
 */

@SpringBootTest
public class ServiceTest {

    @Test
    public void RSAUtiltest() throws Exception {
        String password = "12345";
        String decrypt = RSAUtil.encrypt(password);
        System.out.println(decrypt);
    }
}
