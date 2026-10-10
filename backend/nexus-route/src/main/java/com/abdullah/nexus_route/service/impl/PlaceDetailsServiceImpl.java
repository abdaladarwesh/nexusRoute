package com.abdullah.nexus_route.service.impl;

import com.abdullah.nexus_route.dto.response.PlaceResponse;
import com.abdullah.nexus_route.service.PlaceDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class PlaceDetailsServiceImpl implements PlaceDetailsService {
    private final RestClient restClient;
    @Override
    public PlaceResponse getPlaceDetailsByLonAndLat(Double longitude, Double latitude) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/reverse")
                        .queryParam("lat", latitude)
                        .queryParam("lon", longitude)
                        .queryParam("format", "json")
                        .build())
                .retrieve()
                .body(PlaceResponse.class);


    }
}
