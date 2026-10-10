package com.abdullah.nexus_route.service;

import com.abdullah.nexus_route.dto.PlaceResponse;

public interface PlaceService {
    PlaceResponse getPlaceNameByLanAndLon(Double longitude, Double latitude);
}
