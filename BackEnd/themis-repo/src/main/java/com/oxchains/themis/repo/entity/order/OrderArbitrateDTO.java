package com.oxchains.themis.repo.entity.order;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * @author anonymity
 * @create 2018-08-17 17:52
 **/
@Data
public class OrderArbitrateDTO {

    @Excel(name = "订单编号")
    private String orderId;

    @Excel(name = "申请时间", width = 20)
    private String createTime;

    @Excel(name = "用户名", width = 15)
    private String username;

    @Excel(name = "用户身份")
    private String identity;
    @Excel(name = "原因", width = 60)
    private String reason;

    @Excel(name = "仲裁状态", width = 20)
    private String status;

    public OrderArbitrateDTO() {
    }

    public OrderArbitrateDTO(String orderId, String createTime, String username, String identity, String reason, String status) {
        this.orderId = orderId;
        this.createTime = createTime;
        this.username = username;
        this.identity = identity;
        this.reason = reason;
        this.status = status;
    }
}
