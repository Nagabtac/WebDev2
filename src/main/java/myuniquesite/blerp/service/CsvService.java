package myuniquesite.blerp.service;

import org.springframework.stereotype.Service;

import myuniquesite.blerp.model.Car;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;

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
    
    public List<Car> importFromCsv() {
        List<Car> cars = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip header
                }
                
                // Parse CSV line more robustly
                List<String> fields = parseCsvLine(line);
                if (fields.size() >= 8) {
                    try {
                        Car car = new Car();
                        car.setId(Long.parseLong(fields.get(0).trim()));
                        car.setMake(fields.get(1).trim());
                        car.setModel(fields.get(2).trim());
                        car.setYear(Integer.parseInt(fields.get(3).trim()));
                        car.setColor(fields.get(4).trim());
                        car.setBodyType(fields.get(5).trim());
                        car.setEngineType(fields.get(6).trim());
                        car.setLicensePlate(fields.get(7).trim());
                        cars.add(car);
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing car data: " + line);
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cars;
    }
    
    private List<String> parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    // Escaped quote
                    currentField.append('"');
                    i++; // Skip next quote
                } else {
                    // Toggle quote state
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                // End of field
                fields.add(currentField.toString());
                currentField.setLength(0);
            } else {
                currentField.append(c);
            }
        }
        
        // Add the last field
        fields.add(currentField.toString());
        return fields;
    }
}
