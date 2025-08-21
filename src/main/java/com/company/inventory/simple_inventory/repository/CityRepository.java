package com.company.inventory.simple_inventory.repository;

import com.company.inventory.simple_inventory.model.static_data.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CityRepository extends JpaRepository<City,Long> , JpaSpecificationExecutor<City> {
}
