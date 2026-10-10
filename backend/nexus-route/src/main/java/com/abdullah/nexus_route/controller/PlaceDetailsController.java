package com.abdullah.nexus_route.controller;

import com.abdullah.nexus_route.dto.response.PlaceResponse;
import com.abdullah.nexus_route.service.PlaceDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/place")
@RequiredArgsConstructor
public class PlaceDetailsController {
    private final PlaceDetailsService placeDetailsService;

    @GetMapping
    public ResponseEntity<PlaceResponse> getPlaceDetails(@RequestParam Double lon, @RequestParam Double lat){
        return ResponseEntity.ok(
                placeDetailsService.getPlaceDetailsByLonAndLat(lon, lat)
        );
    }
}
