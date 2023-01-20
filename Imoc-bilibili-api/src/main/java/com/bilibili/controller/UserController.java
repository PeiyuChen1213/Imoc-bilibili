package com.bilibili.controller;

import com.bilibili.domain.JsonResponse;
import com.bilibili.domain.User;
import com.bilibili.service.UserService;
import com.bilibili.service.util.RSAUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Chen Peiyu
 * @version 1.0
 * @description Controller表的api接口
 * @date 1/20/2023 8:21 PM
 */

@RestController
@Api(tags = "用户管理接口")
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation("生成public key")
    @GetMapping("/rsa-pks")
    public JsonResponse<String> getRacPublicKey(){
        String publicKeyStr = RSAUtil.getPublicKeyStr();
        return new JsonResponse<>(publicKeyStr);
    }

    @ApiOperation("创建新用户")
    @PostMapping("/users")
    public JsonResponse<User>  adduser(@RequestBody User user){
        //创建新建用户的方法
        userService.addUser(user);
        return new JsonResponse<>(user);
    }



}
