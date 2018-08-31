package com.oxchains.themis.chat.entity;

import lombok.Data;

/**
 * @author ccl
 * @time 2017-10-12 17:13
 * @name User
 * @desc:
 */
@Data
public class User {

    public User(){}

    private Long id;

    private Long createTime;

    private String username;

    private String loginname;

    private String email;

    private String mobilephone;

    private String password;

    private String avatar;

    private String firstAddress;

    private Integer stype;

    private Integer loginStatus;

    private Long roleId;

    private Integer enabled;

    private String description;

    public User(User user){
        setEmail(user.getEmail());
        setLoginname(user.getLoginname());
        setUsername(user.getUsername());
        setId(user.getId());
        setMobilephone(user.getMobilephone());
        setLoginStatus(user.getLoginStatus());
        setCreateTime(user.getCreateTime());
        setStype(user.getStype());
        setEnabled(user.getEnabled());
        setAvatar(user.getAvatar());
        setRoleId(user.getRoleId());
        setFirstAddress(user.getFirstAddress());
        setDescription(user.getDescription());
    }
}
