package com.blumm.springboottdd.test;

import com.blumm.springboottdd.SpringBootTddApplication;
import com.blumm.springboottdd.model.Car;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootTddApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CarsApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String getRootUrl() {
        return "http://localhost:" + port + "/api/v1";
    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void testGetAllCars() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/cars",
                HttpMethod.GET, entity, String.class);

        Assert.assertNotNull(response.getBody());
    }


    @Test
    public void testGetCarById() {
        Car car = restTemplate.getForObject(getRootUrl() + "/cars/1", Car.class);
        log.info(car.getCarName());
        Assert.assertNotNull(car);
    }

    @Test
    public void testCreateCar() {
        Car car = new Car();
        car.setCarName("Prius");
        car.setDoors(4);

        ResponseEntity<Car> postResponse = restTemplate.postForEntity(getRootUrl() + "/cars", car, Car.class);
        Assert.assertNotNull(postResponse);
        Assert.assertNotNull(postResponse.getBody());
    }

    @Test
    public void testUpdateCar() {
        int id = 1;
        Car car = restTemplate.getForObject(getRootUrl() + "/cars/" + id, Car.class);
        car.setCarName("Tesla");
        car.setDoors(2);

        restTemplate.put(getRootUrl() + "/cars/" + id, car);
        Car updatedCar = restTemplate.getForObject(getRootUrl() + "/cars/" + id, Car.class);
        Assert.assertNotNull(updatedCar);
    }
}
