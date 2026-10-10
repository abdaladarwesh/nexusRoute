package com.abdullah.nexus_route.repository;

import com.abdullah.nexus_route.entity.CityEdge;
import com.abdullah.nexus_route.entity.CityNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityNodeRepository extends JpaRepository<CityNode, Long> {
}
