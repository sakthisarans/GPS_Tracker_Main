package com.sakthi.tracker.services.emqx;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MqttPublishService {
    void senToMqtt(String data, @Header(MqttHeaders.TOPIC) String topic);
}
