package com.yami.shop.api.controller;

import com.yami.shop.common.util.Json;
import com.yami.shop.security.api.util.SecurityUtils;
import com.yami.shop.service.RechargeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/recharge/")
@Api(tags = "用户充值金额接口")
public class RechargeController {

    @Autowired
    private RechargeService rechargeService;

    @PostMapping("/submit")
    @ApiOperation(value = "提交充值订单")
    public ResponseEntity<String> submit(@RequestParam Long amount){
        String userId = SecurityUtils.getUser().getUserId();
        return ResponseEntity.ok(rechargeService.submit(userId,amount));
    }


    @PostMapping("/callback")
    public String callback(@RequestBody HashMap<String, Object> params){
        log.info("充值回调开始========{}", Json.toJsonString(params));
        rechargeService.callback(params);
        return "success";
    }

}
