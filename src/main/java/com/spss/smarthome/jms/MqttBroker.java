package com.spss.smarthome.jms;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by gxy on 18/06/15
 */
@Data
@Slf4j
public class MqttBroker {

    Boolean persistent = false;
    Boolean jmx = false;
    private BrokerService brokerService;
    private String host;
    private Integer port;

    @PostConstruct
    public void start() {
        String uri = new StringBuilder()
                .append("broker:(")
                .append("vm://localhost,")
                // + "stomp://localhost:%d,"
                .append(String.format("mqtt+nio://%s:%d", host, port))
                .append(")?")
                .append(String.format("persistent=%s&useJmx=%s", persistent, jmx))
                .toString();
        //验证
//        final SimpleAuthenticationPlugin authenticationPlugin = new SimpleAuthenticationPlugin();
//        authenticationPlugin.setAnonymousAccessAllowed(false);
//        authenticationPlugin.setUsers(Arrays.asList(new AuthenticationUser(properties.getUsername(), properties.getPassword(), "")));
//        rv.setPlugins(new BrokerPlugin[]{authenticationPlugin});
        log.info(" broker service 启动在 uri: {}", uri);
        try {
            brokerService = BrokerFactory.createBroker(uri);
//            KahaDBPersistenceAdapter kahadb = new KahaDBPersistenceAdapter();
//            kahadb.setDirectory(new File("activemq-data/" + getName() + "-kahadb"));
//            kahadb.setJournalMaxFileLength(500 * 1024);
//            brokerService.setPersistenceAdapter(kahadb);
            brokerService.autoStart();
        } catch (Exception ex) {
            log.error("Mqtt服务启动失败");
            throw new RuntimeException(ex);
        }
    }

    @PreDestroy
    public void stop() {
        try {
            brokerService.stop();
        } catch (Exception ex) {
            throw new RuntimeException();
        }
    }

}
