package com.abdullah.nexus_route.exception;

public class LocationNotFoundException extends RuntimeException {
    public LocationNotFoundException(Long id , LocationType type) {
        super((type == LocationType.START ? "Start" : "Destination") +" with id " + id + " is not found");
    }
    public LocationNotFoundException(String msg) {
        super(msg);
    }

}
