package com.oxchains.themis.repo.entity.aop;

import lombok.Data;

import javax.persistence.*;

/**
 * @author anonymity
 * @create 2018-07-25 13:26
 **/
@Data
@Entity
@Table(name = "otc_exception_log")
public class ExceptionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userId;
    private String method;
    private String description;
    private String exceptionName;
    private String exceptionMessage;
    private Long time;

    public ExceptionLog() {
    }

    public ExceptionLog(Long userId, String method, String description, String exceptionName, String exceptionMessage, Long time) {
        this.userId = userId;
        this.method = method;
        this.description = description;
        this.exceptionName = exceptionName;
        this.exceptionMessage = exceptionMessage;
        this.time = time;
    }
}
