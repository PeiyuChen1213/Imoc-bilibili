package com.bilibili.controller;

import com.bilibili.controller.support.UserSupport;
import com.bilibili.domain.Constant.AuthRoleConstant;
import com.bilibili.domain.JsonResponse;
import com.bilibili.domain.UserMoment;
import com.bilibili.domain.annotation.ApiLimitedRole;
import com.bilibili.domain.annotation.DataLimited;
import com.bilibili.service.UserMomentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Chen Peiyu
 * @version 1.0
 * @description 用户动态的接口
 * @date 1/23/2023 8:53 PM
 */
@RestController
public class UserMomentController {

    @Autowired
    private UserMomentService userMomentService;

    @Autowired
    private UserSupport userSupport;

    @ApiLimitedRole(limitedRoleCodeList = {AuthRoleConstant.ROLE_LV0}) //意思是LV0的用户是没有发动态的权限的
    @DataLimited  //这个注解用来判断传入的参数字段是否正确 也就是userMoment的数据
    @PostMapping("/user-moments")
    public JsonResponse<String> addUserMoments(@RequestBody UserMoment userMoment) throws Exception {
        Long userId = userSupport.getCurrentUserId();
        userMoment.setUserId(userId);
        userMomentService.addUserMoments(userMoment);
        return JsonResponse.success();
    }

    @GetMapping("/user-subscribed-moments")
    public JsonResponse<List<UserMoment>> getUserSubscribedMoments(){
        Long userId = userSupport.getCurrentUserId();
        List<UserMoment> list = userMomentService.getUserSubscribedMoments(userId);
        return new JsonResponse<>(list);
    }

}
