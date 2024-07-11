package com.sakthi.tracker.repository;

import com.sakthi.tracker.model.offlinecache.TrackerOfflineCache;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackerCacheRepository extends MongoRepository<TrackerOfflineCache,String> {
    @Query("{'trackerId':?0,'setting':?1}")
    TrackerOfflineCache findByTrackerAndSetting(String trackerId, String setting);

    @Query("{'trackerId':?0}")
    List<TrackerOfflineCache> findByTracker(String trackerId);
}
