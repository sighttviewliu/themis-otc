package com.oxchains.themis.chat.kafka;

import com.oxchains.themis.chat.entity.ChatContent;
import com.oxchains.themis.chat.repo.MongoRepo;
import com.oxchains.themis.common.util.JsonUtil;
import com.oxchains.themis.common.util.RandomUtil;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * create by huohuo
 *
 * @author huohuo
 */
@Component
public class KafkaConsumerListener {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    @Resource
    private MongoRepo mongoRepo;

    public KafkaConsumerListener() {
    }


    @KafkaListener(topics = {"chatContent"})
    public void listen(ConsumerRecord<?, ?> record) {
        try {
            Optional<?> kafkaMessage = Optional.ofNullable(record.value());
            if (kafkaMessage.isPresent()) {
                Object message = kafkaMessage.get();
                ChatContent chatContent = (ChatContent) JsonUtil.fromJson((String) message, ChatContent.class);

                String combination = RandomUtil.getCombination(24);
                chatContent.setId(combination);
                ChatContent save = mongoRepo.save(chatContent);

                LOG.info("chatContent ---> " + ReflectionToStringBuilder.toString(save, ToStringStyle.MULTI_LINE_STYLE));
            }
        } catch (Exception e) {
            LOG.error("faild to save chatContent to mongo : {}", e);
            e.printStackTrace();
        }

    }

}
