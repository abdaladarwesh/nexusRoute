package com.abdullah.nexus_route.repository;

import com.abdullah.nexus_route.entity.CityEdge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityEdgeRepository extends JpaRepository<CityEdge , Long> {
}
