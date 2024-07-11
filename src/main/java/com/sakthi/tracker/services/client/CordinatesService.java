package com.sakthi.tracker.services.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sakthi.tracker.exceptionHandler.AdminNotFound;
import com.sakthi.tracker.model.emqx.CoordinateDocument;
import com.sakthi.tracker.model.emqx.CordinateWebHookRequest;
import com.sakthi.tracker.model.emqx.Cordinates;
import com.sakthi.tracker.repository.CordinatesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CordinatesService {

    @Autowired
    CordinatesRepository cordinatesRepository;

    @Autowired
    CommonServices commonServices;

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
}
