package ge.tsu.spring.lecture3.car;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class CarController {

    @Autowired
    @Qualifier("carFileImpl")
    private CarService carService;

    @GetMapping("/cars")
    public List<CarView> searchCars(
            @RequestParam(required = false) String manufacturer,
            @RequestParam(required = false) String model) throws IOException {
        return carService.getList(manufacturer, model);
    }

    @GetMapping("/cars/{carId}")
    public CarView getCars(@PathVariable String carId) throws RecordNotFoundException, IOException {
        return carService.getById(carId);
    }

    @PostMapping("/cars")
    public void addCar(@RequestBody AddCar addCar) throws RecordAlreadyExistsException, IOException {
        carService.add(addCar);
    }

    @PutMapping("/cars/{carId}")
    public void updateCar(@RequestBody AddCar addCar, @PathVariable String carId) throws RecordAlreadyExistsException, RecordNotFoundException, IOException {
        carService.update(carId, addCar);
    }

    @DeleteMapping("/cars/{carId}")
    public void deleteCar(@PathVariable String carId) throws RecordNotFoundException, IOException {
        carService.delete(carId);
    }
}
