package com.lrrhut.meteo.model;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class Station {

    @Inject
    @ConfigProperty(name = "station.id", defaultValue = "")
    private String id;

    @Inject
    @ConfigProperty(name = "station.password", defaultValue = "")
    private String password;

    public boolean authenticate(String stationId, String stationPassword) {
        return id.equals(stationId) && password.equals(stationPassword);
    }
}
