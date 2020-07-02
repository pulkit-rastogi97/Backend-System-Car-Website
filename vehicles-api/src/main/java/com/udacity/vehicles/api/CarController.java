package com.udacity.vehicles.api;


import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.service.CarService;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Implements a REST-based controller for the Vehicles API.
 */
@RestController
@RequestMapping("/cars")
class CarController {

    private final CarService carService;
    private final CarResourceAssembler assembler;

    CarController(CarService carService, CarResourceAssembler assembler) {
        this.carService = carService;
        this.assembler = assembler;
    }

    /**
     * Creates a list to store any vehicles.
     * @return list of vehicles
     */
    @GetMapping
    Resources<Resource<Car>> list() {
        List<Resource<Car>> resources = carService.list().stream().map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(resources,
                linkTo(methodOn(CarController.class).list()).withSelfRel());
    }

    /**
     * Gets information of a specific car by ID.
     * @param id the id number of the given vehicle
     * @return all information for the requested vehicle
     */
    @GetMapping("/{id}")
    Resource<Car> get(@PathVariable Long id) {
        Car car = carService.findById(id);
        return assembler.toResource(car);
    }

    /**
     * Posts information to create a new vehicle in the system.
     * @param car A new vehicle to add to the system.
     * @return response that the new vehicle was added to the system
     * @throws URISyntaxException if the request contains invalid fields or syntax
     *
     * sample request
     * {
     *    "condition":"USED",
     *    "details":{
     *       "body":"sedan",
     *       "model":"Impala",
     *       "manufacturer":{
     *          "code":101,
     *          "name":"Chevrolet"
     *       },
     *       "numberOfDoors":4,
     *       "fuelType":"Gasoline",
     *       "engine":"3.6L V6",
     *       "mileage":32280,
     *       "modelYear":2018,
     *       "productionYear":2018,
     *       "externalColor":"white"
     *    },
     *    "location":{
     *       "lat":40.73061,
     *       "lon":-73.935242
     *    }
     * }
     */
    @PostMapping
    ResponseEntity<?> post(@Valid @RequestBody Car car) throws URISyntaxException {


        //Done
        Car carSaved = carService.save(car);
        Resource<Car> resource = assembler.toResource(carSaved);
        return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
    }

    /**
     * Updates the information of a vehicle in the system.
     * @param id The ID number for which to update vehicle information.
     * @param car The updated information about the related vehicle.
     * @return response that the vehicle was updated in the system
     *
     * sample request
     * {
     *    "condition":"OLD",
     *    "details":{
     *       "body":"sedan",
     *       "model":"Impala",
     *       "manufacturer":{
     *          "code":101,
     *          "name":"Chevrolet"
     *       },
     *       "numberOfDoors":4,
     *       "fuelType":"Gasoline",
     *       "engine":"3.6L V6",
     *       "mileage":32280,
     *       "modelYear":2018,
     *       "productionYear":2018,
     *       "externalColor":"white"
     *    },
     *    "location":{
     *       "lat":40.73061,
     *       "lon":-73.935242
     *    }
     * }
     */
    @PutMapping("/{id}")
    ResponseEntity<?> put(@PathVariable Long id, @Valid @RequestBody Car car) {
        car.setId(id);
        car = carService.save(car);
        return ResponseEntity.ok(assembler.toResource(car));
    }

    /**
     * Removes a vehicle from the system.
     * @param id The ID number of the vehicle to remove.
     * @return response that the related vehicle is no longer in the system
     */
    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) {

        carService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
