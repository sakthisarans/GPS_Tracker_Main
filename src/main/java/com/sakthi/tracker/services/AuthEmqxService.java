package com.sakthi.tracker.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.sakthi.tracker.model.emqxauth.EmqxAuthRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Objects;

@Slf4j
@Service
public class AuthEmqxService {
    @Autowired
    CommonServices commonServices;

    RestTemplate restTemplate = new RestTemplate();

    @Value("${emqx.mqtt.username}")
    protected String emqxUsername;

    @Value("${emqx.mqtt.password}")
    protected String emqxPassword;

    @Value("${emqx.mqtt.server}")
    protected String emqxUrl;

    @Value("${emqx.mqtt.port}")
    protected String emqxPort;

    public String authEmqx() throws JsonProcessingException {
            EmqxAuthRequest emqxAuthRequest = EmqxAuthRequest.builder().username(emqxUsername).password(emqxPassword).build();
            URI emqxServer = UriComponentsBuilder.fromHttpUrl(emqxUrl).port(emqxPort).path("/api/v5/login").build().toUri();
            log.debug(commonServices.convertToString(emqxAuthRequest));
            ResponseEntity<String> authResponse = restTemplate.postForEntity(emqxServer, emqxAuthRequest, String.class);
            JsonNode authResponseObject = commonServices.convertToJSON(authResponse.getBody());
            if (authResponseObject.has("code")) {
                if (Objects.equals(authResponseObject.get("code").textValue(), "BAD_USERNAME_OR_PWD")) {
                    log.error("Login Failed", commonServices.convertToString(emqxAuthRequest));
                    return null;
                }
            } else if (authResponseObject.has("token")) {
                return authResponseObject.get("token").textValue();
            }
            return null;
    }

    public URI getUri(String path){
        return UriComponentsBuilder.fromHttpUrl(emqxUrl).port(emqxPort).path(path).build().toUri();
    }
}
