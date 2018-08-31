package com.oxchains.themis.chat.rest;

import com.oxchains.themis.chat.entity.ChatContent;
import com.oxchains.themis.chat.entity.UploadTxIdPojo;
import com.oxchains.themis.chat.service.ChatService;
import com.oxchains.themis.common.model.RestResp;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

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
//    @GetMapping("/history/{senderId}/{receiverId}")
//    public RestResp getChatHistroy(@PathVariable Long senderId, @PathVariable Long receiverId, @RequestParam(defaultValue = "1") Integer pageNum) {
//        return RestResp.success(chatService.getChatHistroy(senderId, receiverId, pageNum));
//    }
    @GetMapping("/history/{senderId}/{receiverId}/{previousId}")
    public RestResp getChatHistoryByPreviousId(@PathVariable Long senderId, @PathVariable Long receiverId, @PathVariable String previousId) {
        List<ChatContent> chatHistoryByPreviousId = chatService.getChatHistoryByPreviousId(senderId, receiverId, previousId);
        return RestResp.success(chatHistoryByPreviousId);
    }

    @GetMapping("/allhistory/{senderId}/{receiverId}")
    public RestResp getChatHistroy(@PathVariable Long senderId, @PathVariable Long receiverId) {
        return RestResp.success(chatService.getAllChatHistory(senderId, receiverId));
    }

    /**
     * 上传交易信息
     *
     * @param pojo
     * @return
     */
   /* @PostMapping("/uploadTxInform")
    public RestResp uploadTxInform(@RequestBody UploadTxIdPojo pojo) {
        if (pojo.getId() == null) {
            return RestResp.fail();
        }
        return RestResp.success(chatService.uploadTxInform(pojo));
    }*/

}

//class PageInfo{
//
//    private
//
//}
