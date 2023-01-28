package com.bilibili.controller.aspect;

import com.bilibili.controller.support.UserSupport;
import com.bilibili.domain.annotation.ApiLimitedRole;
import com.bilibili.domain.auth.UserRole;
import com.bilibili.domain.exception.ConditionException;
import com.bilibili.service.UserRoleService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Order(1)
@Component
@Aspect
public class ApiLimitedRoleAspect {

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private UserRoleService userRoleService;

    @Pointcut("@annotation(com.bilibili.domain.annotation.ApiLimitedRole)")
    public void check() {
    }

    /**
     * @param joinPoint
     * @param apiLimitedRole 需要被限制的角色
     */
    @Before("check() && @annotation(apiLimitedRole)")
    public void doBefore(JoinPoint joinPoint, ApiLimitedRole apiLimitedRole) {
        Long userId = userSupport.getCurrentUserId();
        //根据当前的用户id得到当前的角色列表
        List<UserRole> userRoleList = userRoleService.getUserRoleByUserId(userId);
        //根据传入的注解中的参数得到需要限制的角色
        String[] limitedRoleCodeList = apiLimitedRole.limitedRoleCodeList();
        //使用stream流将 限制的角色转化成一个set集合
        Set<String> limitedRoleCodeSet = Arrays.stream(limitedRoleCodeList).collect(Collectors.toSet());
        //将查询的该用户的角色传入得到
        Set<String> roleCodeSet = userRoleList.stream().map(UserRole::getRoleCode).collect(Collectors.toSet());
        roleCodeSet.retainAll(limitedRoleCodeSet);
        if (roleCodeSet.size() > 0) {
            throw new ConditionException("权限不足！");
        }
    }
}
