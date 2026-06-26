package com.example.tripagent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.tripagent.domain.entity.HotelOption;
import com.example.tripagent.domain.entity.TransportOption;

import java.util.List;

public interface TransportOptionService extends IService<TransportOption> {
    List<TransportOption> getVaildOptions(Long requestId);
}
