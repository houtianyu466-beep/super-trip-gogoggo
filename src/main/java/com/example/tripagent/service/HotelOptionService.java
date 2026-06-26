package com.example.tripagent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.tripagent.domain.entity.HotelOption;

import java.util.List;

public interface HotelOptionService extends IService<HotelOption> {

    List<HotelOption> getVaildOptions(Long requestId);
}
