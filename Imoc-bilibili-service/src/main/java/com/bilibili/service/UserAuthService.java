package com.bilibili.service;

import com.bilibili.domain.auth.AuthRoleElementOperation;
import com.bilibili.domain.auth.AuthRoleMenu;
import com.bilibili.domain.auth.UserAuthorities;
import com.bilibili.domain.auth.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Chen Peiyu
 * @version 1.0
 * @description TODO
 * @date 1/23/2023 10:56 PM
 */
@Service
public class UserAuthService {

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private AuthRoleService authRoleService;

    /**
     * 根据用户id获取当前的用户的权限
     *
     * @param userId
     * @return
     */
    public UserAuthorities getUserAuthorities(Long userId) {
        List<UserRole> userRoleList = userRoleService.getUserRoleByUserId(userId);

        //获取角色的id

        Set<Long> roleIds = userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toSet());

        //查询元素操作权限
        List<AuthRoleElementOperation> authRoleElementOperationList = authRoleService.getRoleElenmetOperationsByRoleIds(roleIds);

        //查询菜单操作权限
        List<AuthRoleMenu> authRoleMenuList = authRoleService.getRoleAuthRoleMenuByRoleIds(roleIds);

        UserAuthorities userAuthorities = new UserAuthorities(authRoleElementOperationList, authRoleMenuList);

        return userAuthorities;
    }

    public void addUserDefaultRole(Long id) {
        userRoleService.addUserDefaultRole(id);
    }
}
