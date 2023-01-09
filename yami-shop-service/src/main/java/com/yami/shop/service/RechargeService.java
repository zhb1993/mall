package com.yami.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yami.shop.bean.model.Recharge;

import java.util.HashMap;

public interface RechargeService extends IService<Recharge> {

    String submit(String userId, Long amount);

    void callback(HashMap<String, Object> params);
}
