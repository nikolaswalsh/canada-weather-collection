package com.nicholaswalsh.canadaweathercollection.repository;

import com.nicholaswalsh.canadaweathercollection.model.Observation;
import com.nicholaswalsh.canadaweathercollection.model.ObservationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObservationRepository extends JpaRepository<Observation, ObservationId> {
}
