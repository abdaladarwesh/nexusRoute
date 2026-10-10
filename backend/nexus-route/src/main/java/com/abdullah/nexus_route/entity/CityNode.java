package com.abdullah.nexus_route.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@Builder
public class CityNode {
    @Id
    private Long id;

    private Double longitude;

    private Double latitude;
}
