package com.yami.shop.bean.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("recharge")
public class Recharge implements Serializable {

    @TableId
    private Long id;

    /**
     * 充值的用户ID
     */
    private String userId;
    //金额
    private Long amount;

    private String orderNo;
    //0,未支付，1已支付
    private Integer status;

    private LocalDateTime createTime;

}
