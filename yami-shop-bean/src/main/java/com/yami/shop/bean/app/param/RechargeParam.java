package com.yami.shop.bean.app.param;


import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value= "充值信息")
public class RechargeParam {

    private Integer amount;

}
