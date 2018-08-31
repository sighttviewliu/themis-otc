package com.oxchains.themis.repo.entity.order;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by huohuo on 2017/10/31.
 *
 * @author huohuo
 */
@Entity
@Table(name = "order_arbitrate_upload_evidence")
@Data
public class OrderEvidence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderId;
    private String buyerContent;
    private String buyerFiles;
    private String sellerContent;
    private String sellerFiles;
}
