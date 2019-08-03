package com.lrrhut.meteo;

import com.lrrhut.meteo.model.Station;
import com.lrrhut.meteo.model.StationData;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

@RequestScoped
@Consumes("*/*")
@Path("weatherstation")
public class WeatherStationResource {

    @Inject
    private Station station;


    /**
     * Gets station results paginated
     * @param page the requested page for the data. If no value is provided, will default to page 1
     * @param size the size of the page. If no value is provided will default to 10
     * @return JSON array of the results for the current page
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPaginated(@QueryParam("page") @DefaultValue("1") int page, @DefaultValue("10") @QueryParam("size") int size) {
        List<StationData> results = StationData.find(StationData.FIND_ALL_BY_DATE_DESC)
                .page(page-1, size)
                .list();

        return Response.ok(results)
                .build();
    }

    /**
     * Fetches the lastest weather data recorded to the database
     * @return if there is any content in the StationData table, will return the first element by descending date order
     * otherwise nothing will be returned
     */
    @GET
    @Path("current")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCurrent() {
        return Response.ok(StationData.find(StationData.FIND_ALL_BY_DATE_DESC)
                .firstResult()).build();
    }

    /**
     * Persists data received from the station matching the HTTP GET request with the related parameters
     * @param stationId binds 'ID' query parameter of the weather station
     * @param password binds 'PASSWORD' query parameter of the weather station
     * @param dateutc binds 'dateutc' query parameter of the station
     * @param stationData binds all other query parameters of the station
     * @return If the authentication is successful will return 200 OK otherwise will return 401 UNAUTHORIZED
     */
    @GET
    @Transactional
    @Path("updateweatherstation")
    public Response updateWeatherStationData(@QueryParam("ID") String stationId, @QueryParam("PASSWORD") String password, @QueryParam("dateutc") String dateutc, @BeanParam StationData stationData) {
        if(station.authenticate(stationId, password)) {
            stationData.setDateutc(LocalDateTime.parse(dateutc, DateTimeFormatter.ofPattern("yyyy-M-d HH:mm:ss")));
            stationData.persist();
            return Response.ok().build();

        } else {
            return Response.status(UNAUTHORIZED).build();
        }
    }

}
