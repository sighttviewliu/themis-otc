package com.oxchains.themis.repo.entity.user;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

/**
 * @author ccl
 * @time 2017-10-12 17:13
 * @name User
 * @desc:
 */
@Data
@Entity
@Table(name = "tbl_sys_user")
public class User {

    public User(){}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long createTime;

    @Column(length = 32)
    private String username;

    @Column(length = 32,unique = true)
    private String loginname;

    @Column(length = 32,unique = true)
    private String email;

    @Column(length = 11,unique = true)
    private String mobilephone;

    //@JsonIgnore
    @Column(length = 64)
    private String password;


    @Column(length = 64)
    private String avatar;

    @Column(length = 64)
    private String firstAddress;

    private Integer stype;

    private Integer loginStatus;

    private Long roleId;

    @Column
    private Integer enabled;

    @Column(length = 256)
    private String description;


    //@JsonIgnore
//    @ElementCollection(fetch = FetchType.EAGER)
//    private Set<String> authorities = new HashSet<>();
//
//    public Set<String> getAuthorities() {
//        return authorities;
//    }
//
//    public void setAuthorities(Set<String> authorities) {
//        this.authorities = authorities;
//    }


    public User(User user){
        setEmail(user.getEmail());
        setLoginname(user.getLoginname());
        setUsername(user.getUsername());
        setPassword(user.getPassword());
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
