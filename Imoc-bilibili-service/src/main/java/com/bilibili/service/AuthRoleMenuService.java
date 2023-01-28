package com.bilibili.service;

import com.bilibili.dao.AuthRoleMenuMapper;
import com.bilibili.domain.auth.AuthRoleMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author Chen Peiyu
 * @version 1.0
 * @description TODO
 * @date 1/24/2023 11:01 AM
 */

@Service
public class AuthRoleMenuService {

    @Autowired
    private AuthRoleMenuMapper authRoleMenuMapper;

    public List<AuthRoleMenu> getRoleAuthRoleMenuByRoleIds(Set<Long> roleIds) {
        return authRoleMenuMapper.getAuthRoleMenusByRoleIds(roleIds);
    }
}
