package com.yami.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yami.shop.bean.model.Recharge;

public interface RechargeService extends IService<Recharge> {

    String submit(String userId, Long amount);
}
