package com.yami.shop.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recharge/")
@Api(tags = "用户充值金额接口")
public class RechargeController {

    @PostMapping("/submit")
    @ApiOperation(value = "提交充值订单")
    public ResponseEntity<String> submit(@RequestParam Integer amount){


        return ResponseEntity.ok("");
    }

}
