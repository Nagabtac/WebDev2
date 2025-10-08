package myuniquesite.blerp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String make;
    private String model;
    private Integer year;
    private String color;
    private String licensePlate;
    private String bodyType;
    private String engineType;

    // Default constructor
    public Car() {}

    // --- FIX FOR HttpMessageNotWritableException ---
    /**
     * This method is causing the serialization error because its implementation
     * throws an UnsupportedOperationException, preventing Jackson from converting
     * the Car object to JSON.
     * * We use @JsonIgnore to instruct Jackson to skip this property during serialization.
     * If 'transmission' is a valid property, you must implement this getter to return 
     * the actual transmission value instead of throwing an exception.
     */
    @JsonIgnore
    public String getTransmission() {
        // The original code here caused: java.lang.UnsupportedOperationException: Unimplemented method 'getTransmission'
        return null; 
    }

    // --- Standard Getters and Setters (Included for completeness) ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public String getEngineType() {
        return engineType;
    }

    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }
}
