package com.oxchains.themis.repo.entity.user;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

/**
 * @author ccl
 * @time 2018-01-09 13:29
 * @name UserVO
 * @desc:
 */
@Data
public class UserVO extends User {
    private String token;
    private String newPassword;

    private Set<String> roles;

    private UserTxDetail userTxDetail;

    public UserVO() {}

    public UserVO(User user) {
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

    public User userVO2User(UserVO vo){
        if(null != vo){
            User user = new User();
            user.setId(vo.getId());
            user.setCreateTime(vo.getCreateTime());
            user.setUsername(vo.getUsername());
            user.setLoginname(vo.getLoginname());
            user.setMobilephone(vo.getMobilephone());
            user.setFirstAddress(vo.getFirstAddress());
            user.setEmail(vo.getEmail());
            user.setPassword(vo.getPassword());
            user.setEnabled(vo.getEnabled());
            user.setAvatar(vo.getAvatar());
            user.setStype(vo.getStype());
            user.setLoginStatus(vo.getLoginStatus());
            user.setDescription(vo.getDescription());
            user.setRoleId(vo.getRoleId());

            return user;
        }
        return null;
    }

    public User userVO2User(){
        if(null != this){
            User user = new User();
            user.setId(this.getId());
            user.setUsername(this.getUsername());
            user.setLoginname(this.getLoginname());
            user.setMobilephone(this.getMobilephone());
            user.setEmail(this.getEmail());
            user.setPassword(this.getPassword());
            user.setEnabled(this.getEnabled());
            user.setAvatar(this.getAvatar());
            user.setStype(this.getStype());
            user.setLoginStatus(this.getLoginStatus());
            user.setDescription(this.getDescription());
            user.setCreateTime(this.getCreateTime());
            user.setFirstAddress(this.getFirstAddress());
            user.setRoleId(this.getRoleId());
            user.setRoleId(this.getRoleId());

            return user;
        }
        return null;
    }

    private String vcode;

    private MultipartFile file;


    /**
     * Geetest
     */
    private String geetestChallenge;

    private String geetestValidate;

    private String geetestSeccode;

    private Integer gtServerStatus;

    private int qic = 0;

}
