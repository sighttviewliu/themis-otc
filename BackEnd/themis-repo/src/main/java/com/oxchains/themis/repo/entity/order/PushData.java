package com.oxchains.themis.repo.entity.order;

import lombok.Data;

/**
 * @author anonymity
 * @create 2018-08-17 14:06
 **/
@Data
public class PushData {
    private String status;
    private String message;
    // 1：托管节点公钥的推送，2：秘钥碎片交换的推送,3：双方上传秘钥碎片都成功之后的推送
    private String dataType;
    private String orderId;
    private Object data;

    public PushData() {
    }

    public PushData(String status, String message, String dataType, String orderId, Object data) {
        this.status = status;
        this.message = message;
        this.dataType = dataType;
        this.orderId = orderId;
        this.data = data;
    }
}
