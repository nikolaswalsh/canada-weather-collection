package com.nicholaswalsh.canadaweathercollection;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ObservationRepository extends JpaRepository<Observation, ObservationId> {
}
