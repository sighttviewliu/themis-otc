package com.oxchains.themis.chat.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * create by huohuo
 *
 * @author huohuo
 */
@Entity
@Data
public class ChatContent {
    @Id
    private String id;
    private Long senderId;
    private String chatContent;
    private Long receiverId;
    private String userName;
    private String userAvatar; //接受者头像
    private String chatId;
    private String orderId;
    private String createTime;
    private Integer msgType;
    private String status;
    @Transient
    private Integer healthType;
}
