package com.yami.shop.api.controller;

import com.yami.shop.security.api.util.SecurityUtils;
import com.yami.shop.service.RechargeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recharge/")
@Api(tags = "用户充值金额接口")
public class RechargeController {

    @Autowired
    private RechargeService rechargeService;

    @PostMapping("/submit")
    @ApiOperation(value = "提交充值订单")
    public ResponseEntity<String> submit(@RequestParam Integer amount){

        String userId = SecurityUtils.getUser().getUserId();
        return ResponseEntity.ok("");
    }

}
