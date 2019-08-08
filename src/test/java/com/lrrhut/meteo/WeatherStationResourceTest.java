package com.lrrhut.meteo;

import com.lrrhut.meteo.dto.StationDataDTO;
import io.quarkus.test.junit.QuarkusTest;
import org.hibernate.AssertionFailure;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class WeatherStationResourceTest {

    private static final String WEATHER_STATION_RESOURCE = "/api/weatherstation";

    private static String stationId;
    private static String stationPassword;

    private static Map<String, Object> testData1;
    private static Map<String, Object> testData2;

    @BeforeAll
    public static void defineProperties() {
        try (InputStream is = WeatherStationResourceTest.class.getClassLoader().getResourceAsStream("application.properties")) {
            Properties properties = new Properties();
            properties.load(is);

            stationId = properties.getProperty("station.id");
            stationPassword = properties.getProperty("station.password");

        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionFailure(e.getMessage());
        }

        testData1 = generateQueryParams(65.3, 11.0, 9.5, 65.3, 287.0, 4.03,
                7.61, 0.00, 0.00, 0.40, 0.00, 20.69, 0.00, 67.6, 0.0,
                72.0, 24.97, 0.0, LocalDateTime.now(), "Weather logger V2.2.2", "updateraw",
                1L, 5L);

        testData2 = generateQueryParams(48.3, 12.3, 8.5, 22.3, 134.5, 4.06,
                7.63, 11.00, 45.00, 34.40, 45.00, 22.69, 98.00, 23.6, 13.5,
                71.5, 22.87, 12.0, LocalDateTime.now().plusHours(2), "Weather logger V2.2.2", "updateraw",
                1L, 5L);
    }

    @Test
    public void testUpdateWeatherStationData() {

        testData1.put("ID", stationId);
        testData1.put("PASSWORD", stationPassword);

        testData2.put("ID", stationId);
        testData2.put("PASSWORD", stationPassword);

        given().queryParams(testData1)
                .when().get(WEATHER_STATION_RESOURCE + "/updateweatherstation")
                .then().statusCode(200);

        given().queryParams(testData2)
                .when().get(WEATHER_STATION_RESOURCE + "/updateweatherstation")
                .then().statusCode(200);
    }

    @Test
    public void testUnauthorizedUpdate() {
        testData1.put("ID", "notThatStation");
        given().queryParams(testData1)
                .when().get(WEATHER_STATION_RESOURCE + "/updateweatherstation")
                .then().statusCode(401);

        testData1.put("ID", stationId);
        testData1.put("PASSWORD", "wrongPassword");
        given().queryParams(testData1)
                .when().get(WEATHER_STATION_RESOURCE + "/updateweatherstation")
                .then().statusCode(401);

        testData1.remove("ID");
        testData1.remove("PASSWORD");
        given().queryParams(testData1)
                .when().get(WEATHER_STATION_RESOURCE + "/updateweatherstation")
                .then().statusCode(401);
    }

    @Test
    public void testFetchCurrentStationData() {
        StationDataDTO stationData = given().when()
                .get(WEATHER_STATION_RESOURCE + "/current")
                .then().statusCode(200)
                .extract().body().as(StationDataDTO[].class)[0];

        assertStationData(stationData, testData2);
    }

    @Test
    public void tesFetchAllData() {
        Map<String, Object> testData3 = generateQueryParams(45.3, 13.3, 1.5, 25.3, 145.5, 8.06,
                7.33, 11.02, 45.50, 34.46, 45.40, 27.69, 98.50, 23.65, 14.5,
                72.5, 21.87, 11.0, LocalDateTime.now().plusHours(3), "Weather logger V2.2.2", "updateraw",
                1L, 5L);

        testData3.put("ID", stationId);
        testData3.put("PASSWORD", stationPassword);
        given().queryParams(testData3)
                .when().get(WEATHER_STATION_RESOURCE + "/updateweatherstation")
                .then().statusCode(200);

        StationDataDTO[] dataDefault = given().when()
                .get(WEATHER_STATION_RESOURCE)
                .then().statusCode(200)
                .extract().body().as(StationDataDTO[].class);

        StationDataDTO[] dataPage1 = given().when()
                .queryParam("page", 1)
                .queryParam("size", 2)
                .when().get(WEATHER_STATION_RESOURCE)
                .then().statusCode(200)
                .extract().body().as(StationDataDTO[].class);

        StationDataDTO[] dataPage2 = given().when()
                .queryParam("page", 2)
                .queryParam("size", 2)
                .when().get(WEATHER_STATION_RESOURCE)
                .then().statusCode(200)
                .extract().body().as(StationDataDTO[].class);

        assertEquals(3, dataDefault.length);
        assertStationData(dataDefault[2], testData1);
        assertStationData(dataDefault[1], testData2);
        assertStationData(dataDefault[0], testData3);

        assertEquals(2, dataPage1.length);
        assertStationData(dataPage1[1], testData2);
        assertStationData(dataPage1[0], testData3);

        assertEquals(1, dataPage2.length);
        assertStationData(dataPage2[0], testData1);
    }

    private static Map<String, Object> generateQueryParams(Double tempf, Double humidity, Double dewptf, Double windchillf,
                                                           Double winddir, Double windspeedmph, Double windgustmph, Double rainin,
                                                           Double dailyrainin, Double weeklyrainin, Double monthlyrainin,
                                                           Double yearlyrainin, Double solarradiation, Double indoortempf,
                                                           Double uv, Double indoorhumidity, Double baromin, Double lowbatt,
                                                           LocalDateTime dateutc, String softwaretype, String action, Long realtime, Long rtfreq) {
        Map<String, Object> output = new HashMap<>();
        output.put("tempf", tempf);
        output.put("humidity", humidity);
        output.put("dewptf", dewptf);
        output.put("windchillf", windchillf);
        output.put("winddir", winddir);
        output.put("windspeedmph", windspeedmph);
        output.put("windgustmph", windgustmph);
        output.put("rainin", rainin);
        output.put("dailyrainin", dailyrainin);
        output.put("weeklyrainin", weeklyrainin);
        output.put("monthlyrainin", monthlyrainin);
        output.put("yearlyrainin", yearlyrainin);
        output.put("solarradiation", solarradiation);
        output.put("indoortempf", indoortempf);
        output.put("UV", uv);
        output.put("indoorhumidity", indoorhumidity);
        output.put("baromin", baromin);
        output.put("lowbatt", lowbatt);
        output.put("dateutc", dateutc.format(DateTimeFormatter.ofPattern("yyyy-M-d HH:mm:ss")));
        output.put("softwaretype", softwaretype);
        output.put("action", action);
        output.put("realtime", realtime);
        output.put("rtfreq", rtfreq);

        return output;
    }

    private void assertStationData(StationDataDTO stationData, Map<String, Object> stationParameters) {
        assertEquals(stationParameters.get("tempf"), stationData.getTempf());
        assertEquals(stationParameters.get("humidity"), stationData.getHumidity());
        assertEquals(stationParameters.get("dewptf"), stationData.getDewptf());
        assertEquals(stationParameters.get("windchillf"), stationData.getWindchillf());
        assertEquals(stationParameters.get("winddir"), stationData.getWinddir());
        assertEquals(stationParameters.get("windspeedmph"), stationData.getWindspeedmph());
        assertEquals(stationParameters.get("windgustmph"), stationData.getWindgustmph());
        assertEquals(stationParameters.get("rainin"), stationData.getRainin());
        assertEquals(stationParameters.get("dailyrainin"), stationData.getDailyrainin());
        assertEquals(stationParameters.get("weeklyrainin"), stationData.getWeeklyrainin());
        assertEquals(stationParameters.get("monthlyrainin"), stationData.getMonthlyrainin());
        assertEquals(stationParameters.get("yearlyrainin"), stationData.getYearlyrainin());
        assertEquals(stationParameters.get("solarradiation"), stationData.getSolarradiation());
        assertEquals(stationParameters.get("indoortempf"), stationData.getIndoortempf());
        assertEquals(stationParameters.get("UV"), stationData.getUv());
        assertEquals(stationParameters.get("indoorhumidity"), stationData.getIndoorhumidity());
        assertEquals(stationParameters.get("baromin"), stationData.getBaromin());
        assertEquals(stationParameters.get("lowbatt"), stationData.getLowbatt());
        assertEquals(stationParameters.get("dateutc"), LocalDateTime.parse(stationData.getDateutc()).format(DateTimeFormatter.ofPattern("yyyy-M-d HH:mm:ss")));
        assertEquals(stationParameters.get("softwaretype"), stationData.getSoftwaretype());
        assertEquals(stationParameters.get("action"), stationData.getAction());
        assertEquals(stationParameters.get("realtime"), stationData.getRealtime());
        assertEquals(stationParameters.get("rtfreq"), stationData.getRtfreq());
    }
}
