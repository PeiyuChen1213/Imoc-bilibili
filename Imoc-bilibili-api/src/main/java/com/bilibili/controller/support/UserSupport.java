package com.bilibili.controller.support;

import com.bilibili.domain.exception.ConditionException;
import com.bilibili.service.UserService;
import com.bilibili.service.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Chen Peiyu
 * @version 1.0
 * @description 运行所需要的支持工具类
 * @date 1/22/2023 4:07 PM
 */
@Component
public class UserSupport {

    /**
     * 从请求参数当中获取token然后再解析获取用户id
     * @return 用户id
     */
    public  Long getCurrentUserId()  {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String token = request.getHeader("token");
        Long userId = TokenUtil.verifyToken(token);
        if(userId < 0) {
            throw new ConditionException("非法用户");
        }
//        this.verifyRefreshToken(userId);
        return userId;
    }
}
