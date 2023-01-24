package com.bilibili.service;

import com.bilibili.dao.UserRoleMapper;
import com.bilibili.domain.auth.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<UserRole> getUserRoleByUserId(Long userId) {
        return userRoleMapper.getUserRoleByUserId(userId);
    }
}
