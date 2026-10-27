package com.nicholaswalsh.canadaweathercollection.repository;

import com.nicholaswalsh.canadaweathercollection.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StationRepository extends JpaRepository<Station, String> {
}
