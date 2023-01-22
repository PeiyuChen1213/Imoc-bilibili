package com.bilibili.controller;

import com.bilibili.controller.support.UserSupport;
import com.bilibili.domain.JsonResponse;
import com.bilibili.domain.User;
import com.bilibili.domain.UserInfo;
import com.bilibili.service.UserService;
import com.bilibili.service.util.RSAUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 获取rsa公钥后续使用
     *
     * @return
     */
    @ApiOperation("生成public key")
    @GetMapping("/rsa-pks")
    public JsonResponse<String> getRacPublicKey() {
        String publicKeyStr = RSAUtil.getPublicKeyStr();
        return new JsonResponse<>(publicKeyStr);
    }

    @ApiOperation("创建新用户")
    @PostMapping("/users")
    public JsonResponse<User> adduser(@RequestBody User user) {
        //创建新建用户的方法
        userService.addUser(user);
        return new JsonResponse<>(user);
    }

    @ApiOperation("查询用户信息")
    @GetMapping("/users")
    public JsonResponse<User> getUserInfo() {
        //获取当前请求的用户id
        Long currentUserId = UserSupport.getCurrentUserId();
        User user = userService.getUserInfo(currentUserId);
        return new JsonResponse<>(user);
    }

    /**
     * 登录校验
     *
     * @param user 前端传入的一个用户参数
     * @return
     */
    @ApiOperation("登录校验的方法")
    @PostMapping("/user-tokens")
    public JsonResponse<String> login(@RequestBody User user) throws Exception {
        String token = userService.login(user);
        return new JsonResponse<>(token);
    }


    @PutMapping("/users")
    public JsonResponse<String> updateUsers(@RequestBody User user) throws Exception {
        Long userId = UserSupport.getCurrentUserId();
        user.setId(userId);
        userService.updateUsers(user);
        return JsonResponse.success();
    }

    @PutMapping("/user-infos")
    public JsonResponse<String> updateUserInfos(@RequestBody UserInfo userInfo) {
        Long userId = UserSupport.getCurrentUserId();
        userInfo.setUserId(userId);
        userService.updateUserInfos(userInfo);
        return JsonResponse.success();

    }


}
