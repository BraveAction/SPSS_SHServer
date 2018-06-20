package com.spss.smarthome.jms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spss.smarthome.service.MessageService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.activemq.filter.AnyDestination;
import org.springframework.jms.listener.SimpleMessageListenerContainer;
import org.springframework.util.StringUtils;

import javax.jms.*;

/**
 * Created by sfrensley on 3/29/15.
 */
@Data
@Slf4j
public class MqttConsumer extends SimpleMessageListenerContainer {

    private String host;
    private Long timeout;
    //Activity window before a new session is established
    private Long sessionWindowSeconds;
    //将报文内容格式化为JsonObject
    private ObjectMapper mapper = new ObjectMapper();
    private MessageService messageService;

    private String topicPattern;
    private int topicLevel;

    public MqttConsumer(MessageService messageService) {

        this.messageService = messageService;
    }

    /**
     * topic格式
     *
     * @return
     */
    private ActiveMQDestination[] getDestinations() {
        ActiveMQTopic[] activeMQTopics = new ActiveMQTopic[topicLevel];
        for (int i = 0; i < topicLevel; i++) {
            StringBuffer destinationName = new StringBuffer(topicPattern);
            for (int j = 0; j < i; j++) {
                destinationName.append(topicPattern);
            }

            activeMQTopics[i] = new ActiveMQTopic(destinationName.toString());
        }
        return activeMQTopics;
    }

    @Override
    public void afterPropertiesSet() {
        this.setupMessageListener(new Listener());
        this.setConnectionFactory(new ActiveMQConnectionFactory(host));
        this.setPubSubDomain(true);
        //this.setDestinationName("*");

        /*
         拦截所有topic
          */
        this.setDestination(new AnyDestination(getDestinations()) {

            /**
             * @see org.apache.activemq.command.ActiveMQTopic
             * @return topic类型
             */
            @Override
            public byte getDestinationType() {
                return 2;
            }

            /**
             * @see org.apache.activemq.command.ActiveMQTopic
             * @return
             */
            @Override
            public byte getDataStructureType() {
                return ActiveMQTopic.DATA_STRUCTURE_TYPE;
            }
        });

        super.afterPropertiesSet();
    }

    private class Listener implements MessageListener {
        @Override
        public void onMessage(Message message) {
            String text = null;
            Destination destination = null;
            try {
                destination = message.getJMSDestination();
                if (message instanceof TextMessage) {
                    text = ((TextMessage) message).getText();
                } else if (message instanceof BytesMessage) {
                    final BytesMessage bytesMessage = (BytesMessage) message;
                    byte[] bytes = new byte[(int) bytesMessage.getBodyLength()];
                    bytesMessage.readBytes(bytes);
                    text = new String(bytes);
                }

            } catch (JMSException ex) {
                log.warn("报文内容异常: ", ex);
            }

            if (text == null) {
                return;
            }
            if (destination instanceof ActiveMQTopic) {
                //name without topic://
                String topic = ((ActiveMQTopic) destination).getPhysicalName();
                //topic names have periods in them. We use slashes.
                topic = StringUtils.replace(topic, ".", "/");
                //发布消息
                log.info("发布了一个报文为: {} Topic为 : {} 的消息", text, topic);
//                try {
//                    persistMessage(topic, mapper.readValue(text, OwnMessage.class));
//                } catch (IOException e) {
//                    log.error("Error processing message: ", e);
//                }
            }
        }


        //持久化消息
        private void persistMessage(String topic, Object msg) {

//            try {
//                log.info("Entity: {}", msg);
//                try {
//                    if (trackService.exists(topic,msg)) {
//                        log.info("Message exists.");
//                    }
//
//                    mqtt.domain.Session session = sessionService.findOrCreateSession(topic,sessionWindowSeconds * 1000);
//                    if (session == null) {
//                        log.error("Unable to process message because track session is null.");
//                        return;
//                    }
//                    //Don't merge another point if it's inside our point tolerance radius for this session because it clutters the map
//                    //This will be a problem for circular routes as the final route point will not appear on the map
//                    //perhaps calculate distance and time (or just time)
//                    if (trackService.isWithinDistanceForSession(msg, proximityWindow,session)) {
//                        log.info("Message inside radius");
//                    } else {
//                        //Update session date to keep alive
//                        session.setDate(System.currentTimeMillis());
//                        session = sessionService.save(session);
//                        msg.setSession(session);
//                        msg = trackService.save(msg);
//                        log.info("Saved Entity: {} Session: {}", msg,session);
//                    }
//                } catch (Exception e) {
//                    log.error("Saved Exception:", e);
//                }
//            } catch (Exception e) {
//                //must catch everything - client will exit if and exception is thrown.
//                log.error("Something bad happened.",e);
//            }
        }
    }
}
