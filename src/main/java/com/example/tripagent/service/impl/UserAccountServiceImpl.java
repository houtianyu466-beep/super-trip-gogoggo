package com.example.tripagent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.tripagent.domain.entity.UserAccount;
import com.example.tripagent.mapper.UserAccountMapper;
import com.example.tripagent.service.UserAccountService;
import org.springframework.stereotype.Service;

@Service
public class UserAccountServiceImpl
        extends ServiceImpl<UserAccountMapper, UserAccount>
        implements UserAccountService {

    @Override
    public UserAccount getByUsername(String username) {
        return getOne(
                new QueryWrapper<UserAccount>()
                        .eq("username", username)
                        .last("limit 1")
        );
    }
}