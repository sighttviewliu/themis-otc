package com.oxchains.themis.chat.websocket;

import com.oxchains.themis.chat.service.ChatService;
import com.oxchains.themis.chat.service.KafkaService;
import com.oxchains.themis.chat.service.MessageService;
import com.oxchains.themis.chat.service.SensitiveWordFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * create by huohuo
 *
 * @author huohuo
 */
@Service
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {
    @Value("${websocket.port}")
    private Integer port;
    @Resource
    private KafkaService kafkaService;
    @Resource
    private MessageService messageService;
    @Resource
    private SensitiveWordFilter sensitiveWordFilter;
    @Resource
    private ChatService chatService;

    public StartupListener() {

    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        new Thread(new WebSocketServer(kafkaService, port, messageService, chatService)).start();
        //初始化敏感字库
        sensitiveWordFilter.initKeyWord();
    }
}
