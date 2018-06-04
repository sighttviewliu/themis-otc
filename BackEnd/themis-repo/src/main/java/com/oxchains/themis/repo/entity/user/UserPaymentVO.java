package com.oxchains.themis.repo.entity.user;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author ccl
 * @time 2018-05-31 11:36
 * @name UserPayment
 * @desc:
 */
@Data
public class UserPaymentVO extends UserPayment {
    private MultipartFile file;

    public UserPaymentVO(UserPayment payment){
        setId(payment.getId());
        setUserId(payment.getUserId());
        setBankCard(payment.getBankCard());
        setAliPay(payment.getAliPay());
        setAliPayQr(payment.getAliPayQr());
    }

    public UserPayment userPaymentVO2UserPayment(){
        UserPayment payment = new UserPayment();
        payment.setId(this.getId());
        payment.setUserId(this.getUserId());
        payment.setAliPay(this.getAliPay());
        payment.setAliPayQr(this.getAliPayQr());
        return payment;
    }
}
