package com.bilibili.service;

import com.bilibili.dao.UserRoleMapper;
import com.bilibili.domain.Constant.AuthRoleConstant;
import com.bilibili.domain.auth.AuthRole;
import com.bilibili.domain.auth.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Chen Peiyu
 * @version 1.0
 * @description TODO
 * @date 1/23/2023 11:13 PM
 */

@Service
public class UserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private AuthRoleService authRoleService;

    public List<UserRole> getUserRoleByUserId(Long userId) {
        return userRoleMapper.getUserRoleByUserId(userId);
    }

    public void addUserDefaultRole(Long id) {
        UserRole userRole = new UserRole();
        userRole.setUserId(id);
        userRole.setCreateTime(new Date());
        //设置一个初始值
        AuthRole role = authRoleService.getAuthRoleByCode(AuthRoleConstant.ROLE_LV0);
        userRole.setRoleId(role.getId());
        userRoleMapper.addUserRole(userRole);
    }
}
