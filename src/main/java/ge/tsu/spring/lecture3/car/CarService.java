package ge.tsu.spring.lecture3.car;
import java.io.IOException;
import java.util.List;

public interface CarService {
    void add(AddCar addCar) throws RecordAlreadyExistsException, IOException;
    void update(String id, AddCar addCar) throws RecordNotFoundException, RecordAlreadyExistsException, IOException;
    List<CarView> getList(String manufacturer, String model) throws IOException;
    CarView getById(String id) throws RecordNotFoundException, IOException;
    void delete(String Id) throws RecordNotFoundException, IOException;
}
