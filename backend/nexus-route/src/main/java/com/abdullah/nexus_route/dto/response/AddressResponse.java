package com.abdullah.nexus_route.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AddressResponse {

    private String road;
    private String neighbourhood;
    private String suburb;

    @JsonProperty("city_district")
    private String cityDistrict;

    private String city;
    private String state;

    @JsonProperty("ISO3166-2-lvl4")
    private String iso31662Lvl4;

    private String postcode;
    private String country;

    @JsonProperty("country_code")
    private String countryCode;
}