package com.sakthi.tracker.repository;


import com.sakthi.tracker.model.emqx.CoordinateDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CordinatesRepository extends MongoRepository<CoordinateDocument,String> {
}
