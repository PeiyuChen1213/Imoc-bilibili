package com.bilibili.service;

import com.bilibili.dao.AuthRoleElementOperationMapper;
import com.bilibili.dao.AuthRoleMapper;
import com.bilibili.domain.auth.AuthRoleElementOperation;
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
public class AuthRoleElementOperationService {
    @Autowired
    private AuthRoleElementOperationMapper authRoleElementOperationMapper;

    public List<AuthRoleElementOperation> getRoleElementOperationsByRoleIds(Set<Long> roleIdSet) {
        return authRoleElementOperationMapper.getRoleElementOperationsByRoleIds(roleIdSet);
    }
}
