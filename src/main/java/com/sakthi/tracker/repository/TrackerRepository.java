package com.sakthi.tracker.repository;

import com.sakthi.tracker.model.tracker.Trackers;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackerRepository extends MongoRepository<Trackers,String> {
    @Query("{'trackerID':?0}")
    public Trackers findByTrackerid(String Trackerid);
    @Query("{'trackerID': ?0, 'users.email': ?1}")
    public Trackers findByTrackeridAndEmail(String Trackerid,String email);
}
