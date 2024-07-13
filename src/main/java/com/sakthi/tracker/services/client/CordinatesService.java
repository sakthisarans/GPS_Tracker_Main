package com.sakthi.tracker.services.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.sakthi.tracker.exceptionHandler.AdminNotFound;
import com.sakthi.tracker.model.emqx.*;
import com.sakthi.tracker.repository.CordinatesRepository;
import com.sakthi.tracker.repository.FallRepository;
import com.sakthi.tracker.repository.SpeedRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class CordinatesService {

    @Autowired
    CordinatesRepository cordinatesRepository;

    @Autowired
    CommonServices commonServices;

    @Autowired
    FallRepository fallRepository;

    @Autowired
    SpeedRepository speedRepository;

    @Autowired
    MqttPublishService mqttPublishService;

    public ResponseEntity<String> saveCordinates(CordinateWebHookRequest cordinateWebHookRequest) throws JsonProcessingException, AdminNotFound {
        List<CoordinateDocument> list=new ArrayList<>();

        cordinateWebHookRequest.getPayload().forEach(x->{
            try {
                CoordinateDocument cordinateDocument=CoordinateDocument.builder().
                        trackerId(cordinateWebHookRequest.getClientId()).
                        dateFormat("UTC").
                        date(x.getDatetime()).
                        cordinates(new Cordinates(x.getLat(),x.getLang())).
                        userId(commonServices.getAdminID(cordinateWebHookRequest.getClientId()))
                        .build();
                list.add(cordinateDocument);
            } catch (JsonProcessingException | AdminNotFound e) {
                throw new RuntimeException(e);
            }
        });

        cordinatesRepository.saveAll(list);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    public ResponseEntity<String> overSpeed(OverSpeedWebHookRequest speed){
        List<SpeedDocument> list=new ArrayList<>();
        speed.getPayload().forEach(x->{
            try {
                list.add(
                        SpeedDocument.builder().
                                Speed(x.getSpeed()).
                                date(x.getDatetime())
                                .dateFormat("UTC").
                                userId(commonServices.getAdminID(speed.getClientId())).
                                trackerId(speed.getClientId()).unit(x.getUnit())
                                .build()
                );
            } catch (JsonProcessingException | AdminNotFound e) {
                throw new RuntimeException(e);
            }
        });
        speedRepository.saveAll(list);
        return new ResponseEntity<>("",HttpStatus.OK);
    }

    public ResponseEntity<String> fallDetection(WebHookFallDetection fall) throws JsonProcessingException, AdminNotFound {

        FallDetectionDocument fallDetectionDocument=FallDetectionDocument.builder()
                        .trackerId(fall.getClientId())
                .userId(commonServices.getAdminID(fall.getClientId()))
                .dateFormat("UTC")
                .date(fall.getPayload().getDatetime())
                .cordinates(new Cordinates(
                        fall.getPayload().getLat(),
                        fall.getPayload().getLang()
                )).build();
        fallRepository.save(fallDetectionDocument);
        return new ResponseEntity<>("",HttpStatus.OK);
    }

    public ResponseEntity<String> getTime(JsonNode payload) throws JsonProcessingException {

        Calendar calendar= Calendar.getInstance();

        TimeResponse timeResponse= TimeResponse.builder()
                .year(calendar.get(Calendar.YEAR))
                .month(calendar.get(Calendar.MONTH))
                .day(calendar.get(Calendar.DAY_OF_MONTH))
                .weekDay(calendar.get(Calendar.DAY_OF_WEEK))
                .hour(calendar.get(Calendar.HOUR_OF_DAY))
                .minute(calendar.get(Calendar.MINUTE))
                .second(calendar.get(Calendar.SECOND))
                .millisecond(calendar.get(Calendar.MILLISECOND))
                .build();

        mqttPublishService.publishApiCall(
                commonServices.convertToJSON(
                        commonServices.convertToString(timeResponse)
                ),
                payload.get("clientId").textValue(),
                "/tracker/%s/gettime"
        );
        return new ResponseEntity<>("",HttpStatus.OK);
    }
}
