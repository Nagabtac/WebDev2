package myuniquesite.blerp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.repository.CrudRepository;

import myuniquesite.blerp.model.Car;


public interface CarRepository extends JpaRepository<Car, Long> {
}
