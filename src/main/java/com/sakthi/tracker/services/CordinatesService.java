package com.sakthi.tracker.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sakthi.tracker.exceptionHandler.AdminNotFound;
import com.sakthi.tracker.model.client.SettingsRequest;
import com.sakthi.tracker.model.emqx.CoordinateDocument;
import com.sakthi.tracker.model.emqx.CordinateWebHookRequest;
import com.sakthi.tracker.model.emqx.Cordinates;
import com.sakthi.tracker.model.emqx.TrackerCordinates;
import com.sakthi.tracker.repository.CordinatesRepository;
import com.sakthi.tracker.services.emqx.MqttPublishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class CordinatesService {

    @Autowired
    CordinatesRepository cordinatesRepository;

    @Autowired
    CommonServices commonServices;

    @Autowired
    MqttPublishService mqttPublishService;

    public ResponseEntity<String> saveCordinates(CordinateWebHookRequest cordinateWebHookRequest) throws JsonProcessingException, AdminNotFound {
        CoordinateDocument cordinateDocument=CoordinateDocument.builder().
                trackerId(cordinateWebHookRequest.getClientId()).
                dateFormat("UTC").
                date(cordinateWebHookRequest.getPayload().getDatetime()).
                cordinates(new Cordinates(cordinateWebHookRequest.getPayload().getLat(),cordinateWebHookRequest.getPayload().getLang())).
                userId(commonServices.getAdminID(cordinateWebHookRequest.getClientId()))
                .build();

        log.debug(cordinateDocument.getCordinates().getLang());
        log.debug(cordinateDocument.getUserId());
        log.debug(cordinateDocument.getDate().toString());
        cordinatesRepository.save(cordinateDocument);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    public ResponseEntity<String> sendMeaasge(SettingsRequest settings){
        try {
            mqttPublishService.senToMqtt(commonServices.convertToString(settings), String.format("/tracker/%s/settings",settings.getClientId()));
            log.debug(String.format("/tracker/%s/settings",settings.getClientId()));
        }catch (Exception ex){
            log.debug(ex.toString());
            return new ResponseEntity<>("",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("",HttpStatus.OK);
    }

}
