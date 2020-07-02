package com.udacity.vehicles.service;

import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.Condition;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.CarRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.udacity.vehicles.domain.car.Details;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.persistence.*;
import javax.swing.text.html.Option;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Implements the car service create, read, update or delete
 * information about vehicles, as well as gather related
 * location and price data when desired.
 */
@Service
public class CarService {

    private final CarRepository repository;
    private final MapsClient mapsClient;
    private final PriceClient priceClient;

    public CarService(CarRepository repository, MapsClient mapsClient, PriceClient priceClient) {
        /**
         * Adding the Maps and Pricing Web Clients created
         *   in `VehiclesApiApplication` as arguments and setting them here.
         */
        this.mapsClient = mapsClient;
        this.priceClient = priceClient;
        this.repository = repository;
    }

    /**
     * Gathers a list of all vehicles
     * @return a list of all vehicles in the CarRepository
     */
    public List<Car> list() {

        List<Car> cars = repository.findAll();
        if(cars==null || cars.isEmpty())
            return repository.findAll();
        else
        {
            for(Car car : cars)
            {
                car.setPrice(priceClient.getPrice(car.getId()));
                car.setLocation(mapsClient.getAddress(car.getLocation()));
            }
            return cars;
        }
    }

    /**
     * Gets car information by ID (or throws exception if non-existent)
     * @param id the ID number of the car to gather information on
     * @return the requested car's information, including location and price
     */
    public Car findById(Long id) {
        /**
         * Finding the car by ID from the `repository` if it exists.
         *   If it does not exist, throw a CarNotFoundException
         */
        Car car = repository.findById(id).orElseThrow(CarNotFoundException::new);

        /**
         * Used the Pricing Web client created in `VehiclesApiApplication`
         * to get the price based on the `id` input'
         * Setting the price of the car
         * Note: The car class file uses @transient, meaning you will need to call
         *   the pricing service each time to get the price.
         */
        car.setPrice(priceClient.getPrice(id));

        /**
         * Used the Maps Web client created in `VehiclesApiApplication`
         * to get the address for the vehicle. Accessed the location
         * from the car object and feed it to the Maps service.
         * Setting the location of the vehicle, including the address information
         * Note: The Location class file also uses @transient for the address,
         * meaning the Maps service needs to be called each time for the address.
         */
        car.setLocation(mapsClient.getAddress(car.getLocation()));

        return car;
    }

    /**
     * Either creates or updates a vehicle, based on prior existence of car
     * @param car A car object, which can be either new or existing
     * @return the new/updated car is stored in the repository
     */
    public Car save(Car car) {
        if (car.getId() != null) {
            return repository.findById(car.getId())
                    .map(carToBeUpdated -> {
                        carToBeUpdated.setCondition(car.getCondition());
                        carToBeUpdated.setDetails(car.getDetails());
                        carToBeUpdated.setLocation(car.getLocation());
                        carToBeUpdated.setPrice(car.getPrice());
                        return repository.save(carToBeUpdated);
                    }).orElseThrow(CarNotFoundException::new);
        }
        return repository.save(car);
    }

    /**
     * Deletes a given car by ID
     * @param id the ID number of the car to delete
     */
    public void delete(Long id) {
        /**
         * Finding the car by ID from the `repository` if it exists.
         *   If it does not exist, throw a CarNotFoundException
         */
        Car car = repository.findById(id).orElseThrow(CarNotFoundException::new);
        /**
         * Delete the car from the repository.
         */
        repository.deleteById(id);

    }
}
