package com.oxchains.themis.repo.entity.order;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by huohuo on 2017/10/23.
 *
 * @author huohuo
 */

@Entity
@Table(name = "order_trustee")
@Data
public class OrderTrustee implements Serializable {
     @Id
     private String orderId;
     private String endpoint;
     private String priKey;
     private String pubKey;
     private String encodeSecrect;
     private String decodeSecrect;

     private String trusteeAddress;

     private int tstatus = 0;

    public OrderTrustee() {
    }

    public OrderTrustee(String orderId) {
        this.orderId = orderId;
    }

    public OrderTrustee(String orderId, String trusteeAddress) {
        this.orderId = orderId;
        this.trusteeAddress = trusteeAddress;
    }
}
