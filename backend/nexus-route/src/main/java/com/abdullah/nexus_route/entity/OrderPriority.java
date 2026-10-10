package com.abdullah.nexus_route.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OrderPriority {
    STANDARD("STANDARD"), EXPRESS("EXPRESS");

    private final String text;


    @JsonCreator
    public static OrderPriority fromText(String text) {
        for (OrderPriority orderPriority : OrderPriority.values()) {
            if (orderPriority.getText().equals(text)) {
                return orderPriority;
            }
        }
        throw new IllegalArgumentException("Unknown OrderPriority with value " + text);
    }

    @Override
    public String toString() {
        return text;
    }
}
