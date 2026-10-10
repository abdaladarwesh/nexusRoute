package com.abdullah.nexus_route.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class PlaceResponse {

    @JsonProperty("place_id")
    private Long placeId;

    private String licence;

    @JsonProperty("osm_type")
    private String osmType;

    @JsonProperty("osm_id")
    private Long osmId;

    private String lat;
    private String lon;

    @JsonProperty("class")
    private String placeClass; // 'class' is a reserved keyword in Java

    private String type;

    @JsonProperty("place_rank")
    private Integer placeRank;

    private Double importance;
    private String addresstype;
    private String name;

    @JsonProperty("display_name")
    private String displayName;

    private Address address;
    private List<String> boundingbox;
}