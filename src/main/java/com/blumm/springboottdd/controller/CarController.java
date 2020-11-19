package com.blumm.springboottdd.controller;

import com.blumm.springboottdd.exception.ResourceNotFoundException;
import com.blumm.springboottdd.model.Car;
import com.blumm.springboottdd.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class CarController {

    @Autowired
    private CarRepository carRepository;

    @GetMapping("/cars")
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    @GetMapping("/car/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) throws ResourceNotFoundException {
        Car car = carRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found on :: " + id));
        return ResponseEntity.ok().body(car);
    }

    @PostMapping("/cars")
    public Car createCar(@Valid @RequestBody Car car) {
        return carRepository.save(car);
    }

    @PutMapping("/cars/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable Long id,
                                         @Valid @RequestBody Car carNew) throws ResourceNotFoundException {
        Car car = carRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Car " + id + "not found"));
        car.setCarName(carNew.getCarName());
        car.setDoors(carNew.getDoors());
        final Car updatedCar = carRepository.save(car);
        return ResponseEntity.ok().body(updatedCar);
    }

    @DeleteMapping("/car/{id}")
    public Map<String, Boolean> deleteCar(@PathVariable Long id) throws ResourceNotFoundException {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car " + id + " not found"));
        carRepository.delete(car);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

}
