package com.sakthi.tracker.services.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.sakthi.tracker.model.client.SettingsRequest;
import com.sakthi.tracker.model.emqx.ReconnectWebHookRequest;
import com.sakthi.tracker.model.emqx.UpdateTrackerStatusRequest;
import com.sakthi.tracker.model.emqxPublish.EmqxPublishRequest;
import com.sakthi.tracker.model.offlinecache.TrackerOfflineCache;
import com.sakthi.tracker.model.tracker.Trackers;
import com.sakthi.tracker.repository.TrackerCacheRepository;
import com.sakthi.tracker.repository.TrackerRepository;
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

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class MqttPublishService {

    @Autowired
    CommonServices commonServices;

    @Autowired
    AuthEmqxService authEmqxService;

    @Autowired
    TrackerCacheRepository trackerCache;

    @Autowired
    TrackerRepository trackerRepository;

    RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<String> updateSettings(SettingsRequest settings) throws JsonProcessingException {

        JsonNode emqxPublishResponseJSON= commonServices.convertToJSON(publishApiCall(settings,settings.getClientId(),"/tracker/%s/settings").getBody());
        if(emqxPublishResponseJSON.has("id")){
            return new ResponseEntity<>("", HttpStatus.OK);
        }else{
            TrackerOfflineCache cache=trackerCache.findByTrackerAndSetting(settings.getClientId(),settings.getSetting());
            if(cache==null){
                cache=TrackerOfflineCache.builder().trackerId(settings.getClientId()).setting(settings.getSetting()).trackerData(commonServices.convertToString(settings)).build();
            }else{
                cache.setTrackerData(commonServices.convertToString(settings));
            }
            trackerCache.save(cache);
            return new ResponseEntity<>("", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    public ResponseEntity<String> trackerCacheCheck(ReconnectWebHookRequest connect){
        List<TrackerOfflineCache> cache=trackerCache.findByTracker(connect.getTrackerId());
        cache.forEach(x->{
            try {
                JsonNode emqxPublishResponseJSON= commonServices.convertToJSON(publishApiCall(commonServices.convertToJSON(x.getTrackerData()),x.getTrackerId(),"/tracker/%s/settings").getBody());
                if(emqxPublishResponseJSON.has("id")){
                    trackerCache.delete(x);
                }
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                throw new RuntimeException(e);
            }
        });
        return ResponseEntity.ok("");
    }

    public HttpEntity<String> publishApiCall(Object settings,String clientId,String publishTopic) throws JsonProcessingException {
        String content=commonServices.convertToString(settings);
        String topic=String.format(publishTopic,clientId);
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
        return restTemplate.exchange(authEmqxService.getUri("/api/v5/publish"), HttpMethod.POST,entity, String.class);
    }

    public ResponseEntity<String> updateStatus(UpdateTrackerStatusRequest status) throws JsonProcessingException {
        Trackers trackers=trackerRepository.findByTrackerid(status.getTrackerId());
        if(trackers!=null && Objects.equals(status.getStatus(), "client.connected")){
            trackers.setOnline(true);
            trackerRepository.save(trackers);
            return new ResponseEntity<>("",HttpStatus.OK);
        } else if (trackers!=null && Objects.equals(status.getStatus(), "client.disconnected")) {
            trackers.setOnline(false);
            trackerRepository.save(trackers);
            return new ResponseEntity<>("",HttpStatus.OK);
        }else {
            return new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
