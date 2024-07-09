package com.sakthi.tracker.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sakthi.tracker.exceptionHandler.AdminNotFound;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class CommonServices {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MongoTemplate mongoTemplate;

    private final String REDIS_PREFIX="TRACKER_ID:";
    public String getAdminID(String clientId) throws JsonProcessingException, AdminNotFound {
        String queryForRedis = REDIS_PREFIX + clientId;
        String adminID = "";
        if (redisTemplate.opsForValue().get(queryForRedis) != null) {
            adminID = redisTemplate.opsForValue().get(queryForRedis);
            return adminID;
        } else {
            Query query = new Query();
            query.addCriteria(Criteria.where("trackerID").is(clientId));
            JsonNode user=null;
            try {
                user= objectMapper.readTree(mongoTemplate.find(query, String.class, "trackers").get(0));
            }catch (IndexOutOfBoundsException ex){
                throw new AdminNotFound(ex.getMessage());
            }
            List<String> temp = new ArrayList<>();
            if (user.has("users")) {
                user.get("users").forEach(x -> {
                    if (Objects.equals(x.get("role").textValue(), "ADMIN")) {
                        temp.add(x.get("email").textValue());
                    }
                });
                if (temp.isEmpty()) {
                    throw new AdminNotFound("");
                }
            } else {
                throw new AdminNotFound("");
            }
            redisTemplate.opsForValue().set(queryForRedis, temp.get(0), 60, TimeUnit.SECONDS);
            return temp.get(0);
        }
    }
    public String convertToString(Object data) throws JsonProcessingException {
        return objectMapper.writeValueAsString(data);
    }
    public JsonNode convertToJSON(String data) throws JsonProcessingException {
        return objectMapper.readTree(data);
    }
}
