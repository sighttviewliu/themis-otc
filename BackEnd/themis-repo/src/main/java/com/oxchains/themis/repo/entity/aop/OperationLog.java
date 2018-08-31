package com.oxchains.themis.repo.entity.aop;

import lombok.Data;

import javax.persistence.*;

/**
 * @author anonymity
 * @create 2018-07-25 13:19
 **/
@Data
@Entity
@Table(name = "otc_operation_log")
public class OperationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userId;
    private String paramUserId;   // 参数中的userId
    private String requestType;
    private String description;
    private String serverAddress;
    private String remoteAddress;
    private String userAgent;
    private String requestUri;
    private Long time;

    public OperationLog() {
    }

    public OperationLog(Long userId, String paramUserId, String requestType, String description, String serverAddress,
                        String remoteAddress, String userAgent, String requestUri, Long time) {
        this.userId = userId;
        this.paramUserId = paramUserId;
        this.requestType = requestType;
        this.description = description;
        this.serverAddress = serverAddress;
        this.remoteAddress = remoteAddress;
        this.userAgent = userAgent;
        this.requestUri = requestUri;
        this.time = time;
    }
}
