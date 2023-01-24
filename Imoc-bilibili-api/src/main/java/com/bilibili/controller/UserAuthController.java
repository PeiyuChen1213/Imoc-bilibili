package com.bilibili.controller;

import com.bilibili.controller.support.UserSupport;
import com.bilibili.domain.JsonResponse;
import com.bilibili.domain.auth.UserAuthorities;
import com.bilibili.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Chen Peiyu
 * @version 1.0
 * @description TODO
 * @date 1/23/2023 10:48 PM
 */
@RestController
public class UserAuthController {

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private UserAuthService userAuthService;


    @GetMapping("/user-authorities")
    public JsonResponse<UserAuthorities> getUserAuthorities(){
        Long userId = userSupport.getCurrentUserId();
        UserAuthorities userAuthorities =  userAuthService.getUserAuthorities(userId);
        return new JsonResponse<>(userAuthorities);
    }
}
