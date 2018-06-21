package com.spss.smarthome.common.config;

import com.spss.smarthome.jms.MqttBroker;
import com.spss.smarthome.jms.MqttConsumer;
import com.spss.smarthome.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jms.listener.AbstractMessageListenerContainer;

/**
 * Created by gxy on 18/06/15
 * 用于启动mqtt服务
 */
@Configuration
@Slf4j
public class MqttConfiguration {

    @Bean
    @DependsOn({"brokerService"})
    @ConfigurationProperties("mqtt.consumer")
    public AbstractMessageListenerContainer consumerService(
            MessageService messageService) {
        try {
            return new MqttConsumer(messageService);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    @ConfigurationProperties("mqtt.broker")
    public MqttBroker brokerService() {
        try {
            return new MqttBroker();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
