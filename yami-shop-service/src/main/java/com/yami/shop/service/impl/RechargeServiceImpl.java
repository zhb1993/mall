package com.yami.shop.service.impl;

import cn.hutool.json.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yami.shop.bean.model.Recharge;
import com.yami.shop.bean.model.User;
import com.yami.shop.common.util.Json;
import com.yami.shop.common.util.OrderNoWorker;
import com.yami.shop.dao.RechargeMapper;
import com.yami.shop.service.RechargeService;
import com.yami.shop.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class RechargeServiceImpl extends ServiceImpl<RechargeMapper, Recharge> implements RechargeService{

    @Autowired
    private OrderNoWorker orderNoWorker;

    @Autowired
    private UserService userService;

    @Override
    public String submit(String userId, Long amount){
        log.info("用户id======={}充值======{}",userId, amount);
        String orderNo = orderNoWorker.nextOrderNo();
        String url = "http://topwin.fun/api/createOrder";
        Long time = System.currentTimeMillis()/1000;
        System.out.println("time==========================="+time);
        Map<String,Object> param = new HashMap<>();
        String sign = DigestUtils.md5Hex("80000047"+time+"9e860080354dadd3");
        param.put("merchantid","80000047");
        param.put("sign",sign);
        param.put("timestamp",time);
        param.put("merchantplatformid","1000000342");
        param.put("merchantorderid",orderNo);
        param.put("applyamount",amount);
        param.put("userName","bin");
        param.put("emailAddress","123456@qq.com");
        param.put("phoneNum","123456789");
        param.put("callbackurl","http://dulikai.com/p/recharge/callback");
        param.put("notifyurl","http://dulikai.com");
        Recharge recharge = new Recharge();
        recharge.setAmount(amount);
        recharge.setStatus(0);
        recharge.setOrderNo(orderNo);
        recharge.setUserId(userId);
        recharge.setCreateTime(LocalDateTime.now());
        save(recharge);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> httpEntity = new HttpEntity<>(Json.toJsonString(param), httpHeaders);
        String s = restTemplate.postForObject(url, httpEntity, String.class);
        HashMap hashMap = Json.parseObject(s, HashMap.class);
        if (hashMap.containsKey("code")){
            if (hashMap.get("code").toString().equals("0")){
                Object data = hashMap.get("data");
                return Json.parseObject(data.toString(),HashMap.class).get("payurl").toString();
            }
        }
        return null;
    }


    @Override
    public void callback(HashMap<String, Object> params) {
        QueryWrapper<Recharge> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", params.get("merchantorderid").toString());
        Recharge recharge = getOne(queryWrapper);
        recharge.setStatus(1);
        recharge.setSuccessTime(LocalDateTime.now());
        updateById(recharge);
        User user = userService.getById(recharge.getUserId());
        String string = params.get("applyAmount").toString();
        BigDecimal amount = new BigDecimal(string);
        user.setAccountBalance(user.getAccountBalance().add(amount));
        userService.updateById(user);
    }

    public static void main(String[] args) {
        String url = "http://topwin.fun/api/createOrder";
        Long time = System.currentTimeMillis()/1000;
        System.out.println("time==========================="+time);
        Map<String,Object> param = new HashMap<>();
        String sign = DigestUtils.md5Hex("80000047"+time+"9e860080354dadd3");
        Double amoutn = 100000d;
        param.put("merchantid","80000047");
        param.put("sign",sign);
        param.put("timestamp",time);
        param.put("merchantplatformid","1000000342");
        param.put("merchantorderid","20221212121200101");
        param.put("applyamount",amoutn);
        param.put("userName","bin");
        param.put("emailAddress","123456@qq.com");
        param.put("phoneNum","123456789");
        param.put("callbackurl","https://google.com");
        param.put("notifyurl","https://google.com");
        System.out.println(Json.toJsonString(param));
    }
}
