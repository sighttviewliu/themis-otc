package com.oxchains.themis.chat.repo;

import com.oxchains.themis.chat.entity.ChatContent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * create by huohuo
 *
 * @author huohuo
 */
@Repository
public interface MongoRepo extends MongoRepository<ChatContent, String> {

    List<ChatContent> findChatContentByChatIdAndOrderId(String chatId, String orderId);

    List<ChatContent> findChatContentByOrderId(String orderId);

    List<ChatContent> findChatContentByChatId(String chatId);

    Page<ChatContent> findChatContentByChatId(String chatId, Pageable pageable);

    Page<ChatContent> findChatContentByChatIdAndCreateTimeBefore(String chatId, String createTime, Pageable pageable);

}
