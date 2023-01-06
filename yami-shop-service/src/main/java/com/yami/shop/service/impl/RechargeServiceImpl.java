package com.yami.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yami.shop.bean.model.Recharge;
import com.yami.shop.dao.RechargeMapper;
import com.yami.shop.service.RechargeService;
import org.springframework.stereotype.Service;

@Service
public class RechargeServiceImpl  extends ServiceImpl<RechargeMapper, Recharge> implements RechargeService{
}
