package com.johnverz.webdev1_g1;

import org.springframework.stereotype.Service;
import java.io.*;
import java.util.List;

@Service
public class CsvService {
    
    private static final String CSV_FILE_PATH = "src/main/resources/data/cars.csv";
    
    public void exportToCsv(List<Car> cars) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CSV_FILE_PATH))) {
            // Write header
            writer.println("ID,Make,Model,Year,Color,Body Type,Engine Type,License Plate");
            
            // Write data
            for (Car car : cars) {
                writer.println(String.format("%d,%s,%s,%d,%s,%s,%s,%s",
                    car.getId(),
                    escapeCsvField(car.getMake()),
                    escapeCsvField(car.getModel()),
                    car.getYear(),
                    escapeCsvField(car.getColor()),
                    escapeCsvField(car.getBodyType()),
                    escapeCsvField(car.getEngineType()),
                    escapeCsvField(car.getLicensePlate())
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private String escapeCsvField(String field) {
        if (field == null) {
            return "";
        }
        // If field contains comma, quote, or newline, wrap in quotes and escape internal quotes
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }
}
