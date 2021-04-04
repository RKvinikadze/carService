package ge.tsu.spring.lecture3.car.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ge.tsu.spring.lecture3.car.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service("carFileImpl")
public class CarServiceFileImpl implements CarService {

    private static final String JSON_DATA = "src/main/java/files/cars.json";

    @Override
    public void add(AddCar addCar) throws RecordAlreadyExistsException, IOException {
        Reader reader = Files.newBufferedReader(Paths.get(JSON_DATA));

        Type listType = new TypeToken<ArrayList<CarView>>(){}.getType();
        List<CarView> carViews = new Gson().fromJson(reader, listType);
        reader.close();

        Optional<CarView> exists = carViews
                .stream()
                .filter(it -> addCar.getManufacturer().equals(it.getManufacturer()) && it.getModel().equals(addCar.getModel()))
                .findFirst();
        if (exists.isPresent()) {
            throw new RecordAlreadyExistsException(
                    String.format("Car with %s and %s already exists", addCar.getManufacturer(), addCar.getModel()));
        }

        carViews.add(new CarView(
                UUID.randomUUID().toString(),
                addCar.getManufacturer(),
                addCar.getModel(),
                addCar.getSpeed()
        ));

        Writer writer = Files.newBufferedWriter(Paths.get(JSON_DATA));
        new GsonBuilder().setPrettyPrinting().create().toJson(carViews, writer);
        writer.close();
    }

    @Override
    public void update(String id, AddCar addCar) throws RecordAlreadyExistsException, RecordNotFoundException, IOException {
        Reader reader = Files.newBufferedReader(Paths.get(JSON_DATA));

        Type listType = new TypeToken<ArrayList<CarView>>(){}.getType();
        List<CarView> carViews = new Gson().fromJson(reader, listType);
        reader.close();

        for (CarView carView : carViews) {
            if (carView.getId().equals(id)) {
                carView.setManufacturer(addCar.getManufacturer());
                carView.setModel(addCar.getModel());
                carView.setSpeed(addCar.getSpeed());

                Writer writer = Files.newBufferedWriter(Paths.get(JSON_DATA));
                new GsonBuilder().setPrettyPrinting().create().toJson(carViews, writer);
                writer.close();

                return;
            }
        }
        throw new RecordNotFoundException("Unable to find car with specified id");
    }

    @Override
    public List<CarView> getList(String manufacturer, String model) throws IOException{
        Reader reader = Files.newBufferedReader(Paths.get(JSON_DATA));

        Type listType = new TypeToken<ArrayList<CarView>>(){}.getType();
        List<CarView> carViews = new Gson().fromJson(reader, listType);
        reader.close();

        if (manufacturer != null && model != null) {
            return carViews
                    .stream()
                    .filter(it -> it.getManufacturer().contains(manufacturer) && it.getModel().contains(model))
                    .collect(Collectors.toList());
        }
        return carViews;
    }

    @Override
    public CarView getById(String id) throws RecordNotFoundException, IOException{
        Reader reader = Files.newBufferedReader(Paths.get(JSON_DATA));

        Type listType = new TypeToken<ArrayList<CarView>>(){}.getType();
        List<CarView> carViews = new Gson().fromJson(reader, listType);
        reader.close();

        for (CarView carView : carViews) {
            if (carView.getId().equals(id)) {
                return carView;
            }
        }
        throw new RecordNotFoundException("Unable to find car with specified id");
    }

    @Override
    public void delete(String id) throws RecordNotFoundException, IOException {
        Reader reader = Files.newBufferedReader(Paths.get(JSON_DATA));

        Type listType = new TypeToken<ArrayList<CarView>>(){}.getType();
        List<CarView> carViews = new Gson().fromJson(reader, listType);
        reader.close();

        Iterator<CarView> carViewIterator = carViews.iterator();

        while (carViewIterator.hasNext()) {
            CarView carView = carViewIterator.next();
            if (carView.getId().equals(id)) {
                carViewIterator.remove();

                Writer writer = Files.newBufferedWriter(Paths.get(JSON_DATA));
                new GsonBuilder().setPrettyPrinting().create().toJson(carViews, writer);
                writer.close();

                return;
            }
        }
        throw new RecordNotFoundException("Unable to find car with specified id");
    }
}