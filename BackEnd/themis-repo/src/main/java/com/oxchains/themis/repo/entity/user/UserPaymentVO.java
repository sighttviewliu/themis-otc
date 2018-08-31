package com.oxchains.themis.repo.entity.user;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

/**
 * @author ccl
 * @time 2018-05-31 11:36
 * @name UserPayment
 * @desc:
 */
@Data
public class UserPaymentVO{
    private Long userId;
    private String username;
    private String bankCard;
    private String bankName;
    private String aliPay;
    private String aliPayQr;

    private MultipartFile file;
    private Integer enabled;

    public UserPaymentVO(Long userId) {
        this.userId = userId;
    }

    public UserPaymentVO() {
    }
}
