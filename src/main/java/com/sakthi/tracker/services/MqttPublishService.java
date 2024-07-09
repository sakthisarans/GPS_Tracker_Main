package com.sakthi.tracker.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.sakthi.tracker.model.client.SettingsRequest;
import com.sakthi.tracker.model.emqxPublish.EmqxPublishRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class MqttPublishService {

    @Autowired
    CommonServices commonServices;

    @Autowired
    AuthEmqxService authEmqxService;

    RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<String> updateSettings(SettingsRequest settings) throws JsonProcessingException {
        String content=commonServices.convertToString(settings);
        String topic=String.format("/tracker/%s/settings",settings.getClientId());
        EmqxPublishRequest emqxPublishRequest=EmqxPublishRequest.builder().
                qos(0).
                topic(topic).
                payload_encoding("plain").
                payload(content).
                retain(false).
                build();
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        headers.add("Authorization", "Bearer "+authEmqxService.authEmqx());
        log.debug(commonServices.convertToString(emqxPublishRequest));
        log.debug(authEmqxService.authEmqx());
        HttpEntity<?> entity = new HttpEntity<Object>(emqxPublishRequest,headers);
        JsonNode emqxPublishResponseJSON= commonServices.convertToJSON(restTemplate.exchange(authEmqxService.getUri("/api/v5/publish"), HttpMethod.POST,entity, String.class).getBody());
        if(emqxPublishResponseJSON.has("id")){
            return new ResponseEntity<>("", HttpStatus.OK);
        }else{
            //add logic for update tracker later by initiating a webhook on subscription and push to tracker
            return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
        }
    }
}
