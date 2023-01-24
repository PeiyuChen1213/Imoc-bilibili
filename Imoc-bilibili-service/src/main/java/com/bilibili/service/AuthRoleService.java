package com.bilibili.service;

import com.bilibili.dao.AuthRoleElementOperationMapper;
import com.bilibili.domain.auth.AuthRoleElementOperation;
import com.bilibili.domain.auth.AuthRoleMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author Chen Peiyu
 * @version 1.0
 * @description TODO
 * @date 1/23/2023 11:14 PM
 */

@Service
public class AuthRoleService {

    @Autowired
    private AuthRoleElementOperationService authRoleElementOperationService;


    @Autowired
    private AuthRoleMenuService authRoleMenuService;

    public List<AuthRoleElementOperation> getRoleElenmetOperationsByRoleIds(Set<Long> roleIds) {
        return authRoleElementOperationService.getRoleElementOperationsByRoleIds(roleIds);
    }


    public List<AuthRoleMenu> getRoleAuthRoleMenuByRoleIds(Set<Long> roleIds) {
        return authRoleMenuService.getRoleAuthRoleMenuByRoleIds(roleIds);
    }
}
