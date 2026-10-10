package com.abdullah.nexus_route.service;

import com.abdullah.nexus_route.dto.response.PlaceResponse;

public interface PlaceDetailsService {
    PlaceResponse getPlaceDetailsByLonAndLat(Double longitude, Double latitude);
}
