package com.bilibili.controller;

import com.alibaba.fastjson.JSONObject;
import com.bilibili.controller.support.UserSupport;
import com.bilibili.domain.JsonResponse;
import com.bilibili.domain.PageResult;
import com.bilibili.domain.User;
import com.bilibili.domain.UserInfo;
import com.bilibili.service.UserFollowingService;
import com.bilibili.service.UserService;
import com.bilibili.service.util.RSAUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Chen Peiyu
 * @version 1.0
 * @description Controller表的api接口
 * @date 1/20/2023 8:21 PM
 */

@RestController
@Api(tags = "用户管理接口")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserSupport userSupport;


    @Autowired
    private UserFollowingService userFollowingService;

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
        Long currentUserId = userSupport.getCurrentUserId();
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

    @ApiOperation("更新用户")
    @PutMapping("/users")
    public JsonResponse<String> updateUsers(@RequestBody User user) throws Exception {
        Long userId = userSupport.getCurrentUserId();
        user.setId(userId);
        userService.updateUsers(user);
        return JsonResponse.success();
    }
    @ApiOperation("更新用户信息")
    @PutMapping("/user-infos")
    public JsonResponse<String> updateUserInfos(@RequestBody UserInfo userInfo) {
        Long userId = userSupport.getCurrentUserId();
        userInfo.setUserId(userId);
        userService.updateUserInfos(userInfo);
        return JsonResponse.success();
    }

    @ApiOperation("用户信息分页查询接口")
    @GetMapping("/user-infos")
    public JsonResponse<PageResult<UserInfo>> pageListUserInfos(@RequestParam Integer no, @RequestParam Integer size, String nick){
        Long userId = userSupport.getCurrentUserId();
        // 类似与map类 但是比map类有更多好用的方法
        JSONObject params = new JSONObject();
        params.put("no", no);
        params.put("size", size);
        params.put("nick", nick);
        params.put("userId", userId);
        //将参数传入到service当中 查询
        PageResult<UserInfo> result = userService.pageListUserInfos(params);
        if(result.getTotal() > 0){
            //区别已经
            List<UserInfo> checkedUserInfoList = userFollowingService.checkFollowingStatus(result.getList(), userId);
            result.setList(checkedUserInfoList);
        }
        return new JsonResponse<>(result);
    }


}
