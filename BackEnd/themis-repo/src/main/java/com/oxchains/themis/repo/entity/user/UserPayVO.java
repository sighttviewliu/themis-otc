package com.oxchains.themis.repo.entity.user;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author ccl
 * @time 2018-05-31 11:36
 * @name UserPayVO
 * @desc:
 */
@Data
public class UserPayVO {
    private Long userId;
    private String username;
    private Map<String, String> bankCard;
    private Map<String, String> aliPay;

    public UserPayVO(Long userId) {
        this.userId = userId;
    }

    public UserPayVO() {
    }
}
