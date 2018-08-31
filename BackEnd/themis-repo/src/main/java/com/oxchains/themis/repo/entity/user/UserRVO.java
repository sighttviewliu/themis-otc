package com.oxchains.themis.repo.entity.user;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

/**
 * @author ccl
 * @time 2018-01-09 13:29
 * @name UserVO
 * @desc:
 */
@Data
public class UserRVO extends UserVO {
    private UserQIC userQIC;
    private List<UserAddress> addresses;
    private List<UserPayment> payments;
    public UserRVO() {}

    public UserRVO(User user) {
        setId(user.getId());
        setUsername(user.getUsername());
        setLoginname(user.getLoginname());
        setMobilephone(user.getMobilephone());
        setEmail(user.getEmail());
        setCreateTime(user.getCreateTime());
        setAvatar(user.getAvatar());
        setEnabled(user.getEnabled());
        setStype(user.getStype());
        setFirstAddress(user.getFirstAddress());
        setLoginStatus(user.getLoginStatus());
        setDescription(user.getDescription());
        setRoleId(user.getRoleId());
    }
}
