package com.oxchains.themis.user.domain;

import lombok.Data;

/**
 * @author ccl
 * @time 2018-04-18 16:51
 * @name MailVO
 * @desc:
 */

@Data
public class MailVO {
    private String fromUser;
    private String fromAddress;
    private String toAddress;
    private String toUser;
    private String subject;
    private String content;
    private String type;

    public MailVO() {}

    public MailVO(String fromUser, String fromAddress, String toUser, String toAddress, String subject, String content, String type) {
        this.fromUser = fromUser;
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.toUser = toUser;
        this.subject = subject;
        this.content = content;
        this.type = type;
    }
}
