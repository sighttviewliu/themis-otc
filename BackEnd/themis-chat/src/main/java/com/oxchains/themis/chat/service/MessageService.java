package com.oxchains.themis.chat.service;

import com.oxchains.themis.chat.entity.ChatContent;
import com.oxchains.themis.chat.websocket.SessionManager;
import com.oxchains.themis.common.constant.message.MessageReadStatus;
import com.oxchains.themis.common.constant.message.MessageType;
import com.oxchains.themis.common.util.DateUtil;
import com.oxchains.themis.repo.dao.MessageRepo;
import com.oxchains.themis.repo.dao.MessageTextRepo;
import com.oxchains.themis.repo.entity.Message;
import com.oxchains.themis.repo.entity.MessageText;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuqi on 2017/11/9.
 */
@Service
public class MessageService {
    @Resource
    private MessageRepo messageRepo;
    @Resource
    private MessageTextRepo messageTextRepo;

    public void postPriChatMessage(ChatContent chatContent) {
        MessageText messageText = new MessageText(chatContent.getSenderId().longValue(), chatContent.getChatContent(), MessageType.PRIVATE_LETTET, null, DateUtil.getPresentDate(), chatContent.getOrderId());
        MessageText save = messageTextRepo.save(messageText);
        Message message = new Message(chatContent.getReceiverId().longValue(), save.getId(), MessageReadStatus.UN_READ, MessageType.PRIVATE_LETTET);
        Message save1 = messageRepo.save(message);
    }

    /**
     * 根据接收者id和消息类型查找该用户为读的消息列表
     *
     * @param receiverId
     * @param msgType
     * @return
     */
    public List<ChatContent> postUnreadChatMessage(Long receiverId, Integer msgType) {

        List<ChatContent> chatContentList = new ArrayList<>();

        List<Message> messageList = messageRepo.findByReceiverIdAndMessageTypeAndReadStatus(receiverId, msgType, MessageReadStatus.UN_READ);

        if (null != messageList && messageList.size() > 0) {
            for (Message message : messageList) {
                //查找对应的message并封装到chatcontent中
                MessageText messageText = messageTextRepo.findOne(message.getMessageTextId());
                ChatContent chatContent = new ChatContent();
                chatContent.setSenderId(messageText.getSenderId());
                chatContent.setChatContent(messageText.getMessage());
                chatContent.setCreateTime(messageText.getPostDate());
                chatContent.setReceiverId(receiverId);
                chatContent.setChatId(SessionManager.getIDS(messageText.getSenderId().toString(), receiverId.toString()));
                chatContent.setMsgType(messageText.getMessageType());
                chatContent.setOrderId(messageText.getOrderId());
                chatContent.setStatus(MessageReadStatus.UN_READ.toString());
                chatContentList.add(chatContent);

                //将消息状态改为已读并更新到数据库
                message.setReadStatus(MessageReadStatus.READ);
                messageRepo.save(message);
            }
        }
        return chatContentList;
    }
}
