package com.oxchains.themis.repo.entity.order;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author ccl
 * @time 2018-06-06 10:45
 * @name OrderTranStatus
 * @desc:
 */

@Entity
@Table(name = "order_tran_status")
@Data
public class OrderTranStatus implements Serializable{


//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;

    @Id
    @Column(length = 21)
    private String orderId;
    private Long createId;// 创建订单的userId
    private Long createTime;
    private Long updateTime;
    private Integer tversion = 0;
    @Column(length = 66)
    private String hashScript;
    /**
     * 订单状态: (1)创建订单 10, 11; createNewTradeOrder
     *           (2)确认/取消订单
     *                  确认订单 20, 21 confirmTradeOrder
     *                  取消订单 22, 23  cancelTrade
     *           (3)上传碎片
     *                  上传买家碎片 30, 31 uploadSecret
     *                  上传卖家碎片 32,33  uploadSecret
     *           (4)完成/仲裁
     *                  完成订单 40, 41 finishOrder
     *                  发起仲裁 42 43 arbitrate
     *                      提交仲裁结果     44 45 judge
     */
    private Integer tstatus;
    private boolean createPush = false;   // 当订单创建之后（链上状态）是否推送了消息，true：推送，false
    private boolean uploadPush = false;   // 当秘钥碎片上传了之后（链上状态）是否推送了消息
    private boolean confirmedPush = false;// 当订单确认了之后（链上状态）是否推送了消息
    private boolean arbitratePush = false;// 当仲裁完成之后，是否推送了托管节点解密私钥

    public OrderTranStatus() {
    }

    public OrderTranStatus(String orderId, Long createId, Long createTime, Long updateTime, Integer tversion, String hashScript, Integer tstatus) {
        this.orderId = orderId;
        this.createId = createId;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.tversion = tversion;
        this.hashScript = hashScript;
        this.tstatus = tstatus;
    }
}
