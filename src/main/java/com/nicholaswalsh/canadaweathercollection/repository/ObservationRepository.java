package com.nicholaswalsh.canadaweathercollection.repository;

import com.nicholaswalsh.canadaweathercollection.model.Observation;
import com.nicholaswalsh.canadaweathercollection.model.ObservationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface ObservationRepository extends JpaRepository<Observation, ObservationId> {

    @Query("select max(obs.obsId.obsDateTime) from Observation obs")
    Optional<Instant> findLatestObservationTime();
}
