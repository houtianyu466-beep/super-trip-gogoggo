package com.example.tripagent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.tripagent.domain.entity.UserAccount;

public interface UserAccountService extends IService<UserAccount> {

    UserAccount getByUsername(String username);
}