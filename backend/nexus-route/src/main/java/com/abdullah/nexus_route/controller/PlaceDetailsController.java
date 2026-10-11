package com.abdullah.nexus_route.controller;

import com.abdullah.nexus_route.dto.response.ApiResponse;
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
    public ResponseEntity<ApiResponse<PlaceResponse>> getPlaceDetails(@RequestParam Double lon, @RequestParam Double lat) {
        return ResponseEntity.ok(
                ApiResponse.<PlaceResponse>builder()
                        .data(placeDetailsService.getPlaceDetailsByLonAndLat(lon, lat))
                        .message("Place details retrieved successfully")
                        .metadata(null)
                        .status("success")
                        .build()
        );
    }
}
