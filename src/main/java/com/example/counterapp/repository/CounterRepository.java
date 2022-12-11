package com.example.counterapp.repository;

import com.example.counterapp.entity.CounterEntity;
import java.util.List;
import org.springframework.data.repository.CrudRepository;


public interface CounterRepository extends CrudRepository<CounterEntity, Long> {

    List<CounterEntity> findByName(String name);

}
