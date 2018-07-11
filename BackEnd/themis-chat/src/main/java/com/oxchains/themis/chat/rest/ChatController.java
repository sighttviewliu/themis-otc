package com.oxchains.themis.chat.rest;

import com.oxchains.themis.chat.service.ChatService;
import com.oxchains.themis.common.model.RestResp;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * create by huohuo
 *
 * @author huohuo
 */
@RestController
@RequestMapping("/chat")
public class ChatController {
    @Resource
    private ChatService chatService;

    /**
     * 获取历史聊天记录
     *
     * @return
     */
    @GetMapping("/history/{orderId}")
    public RestResp getChatHistroy(@PathVariable String orderId, Long senderId, Long receiverId) {
        return RestResp.success(chatService.getChatHistroy(orderId, senderId, receiverId));
    }

}
