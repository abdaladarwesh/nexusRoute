package com.abdullah.nexus_route.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Getter
public enum OrderStatus {
    QUEUED("QUEUED"), PROCESSING("PROCESSING"), DISPATCHED("DISPATCHED"), FAILED("FAILED");

    private final String text;




    @JsonCreator
    public static OrderStatus fromText(String text) {
        for (OrderStatus orderStatus : OrderStatus.values()){
            if (orderStatus.getText()
                    .equals(text)) {
                return orderStatus;
            }
        }
        throw new IllegalArgumentException("Unknown OrderStatus value: " + text);
    }

    @Override
    public String toString() {
        return text;
    }

    }
