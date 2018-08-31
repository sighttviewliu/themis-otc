package com.oxchains.themis.repo.entity.order;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * @author ccl
 * @time 2018-08-20 14:22
 * @name OrderExcel
 * @desc:
 */
@Data
public class OrderExcel {

    @Excel(name = "订单编号")
    private String orderId;

    @Excel(name = "创建时间", width = 20)
    private String createTime;

    @Excel(name = "买家", width = 15)
    private String buyer;

    @Excel(name = "卖家", width = 15)
    private String seller;

    @Excel(name = "货币种类")
    private String coin;

    @Excel(name = "交易价格", width = 20)
    private String price;

    @Excel(name = "订单金额", width = 20)
    private String money;

    @Excel(name = "数量", width = 20)
    private String amount;

    @Excel(name = "订单状态", width = 20)
    private String status;

    public OrderExcel() {
    }

    public OrderExcel(String orderId, String createTime, String buyer, String seller, String coin, String price, String money, String amount, String status) {
        this.orderId = orderId;
        this.createTime = createTime;
        this.buyer = buyer;
        this.seller = seller;
        this.coin = coin;
        this.price = price;
        this.money = money;
        this.amount = amount;
        this.status = status;
    }
}
