package com.example.counterapp.service;

import static java.util.stream.Collectors.toList;

import com.example.counterapp.entity.CounterEntity;
import com.example.counterapp.model.Counter;
import com.example.counterapp.repository.CounterRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.StreamSupport;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class CounterService {
  private final CounterRepository counterRepository;
  private ModelMapper modelMapper;

  public CounterService(CounterRepository counterRepository,
                        ModelMapper modelMapper) {
    this.counterRepository = counterRepository;
    this.modelMapper = modelMapper;
  }

  @Transactional
  public Counter createCounter(Counter counter) {
    CounterEntity existingCounter = counterRepository.findByName(counter.getName()).stream().findFirst().orElse(null);
    if(existingCounter != null) {
      throw new EntityExistsException("Counter exists already");
    }
    CounterEntity counterEntity = toCounterEntity(counter);
    counterEntity.setName(counter.getName());
    CounterEntity saved = counterRepository.save(counterEntity);
    return toCounter(saved);
  }

  @Transactional
  public Counter getCounterByName(String name) {
    CounterEntity counterEntity = counterRepository.findByName(name).stream().findFirst().orElse(null);
    if(counterEntity == null) {
      throw new EntityNotFoundException("Counter does not exist");
    }
    return toCounter(counterEntity);

  }

  public List<Counter> getAllCounters() {
    return StreamSupport.stream(counterRepository.findAll().spliterator(), false)
        .map(this::toCounter)
        .collect(toList());
  }

  @Transactional
  public Counter incrementCounter(String name) {
    CounterEntity counterToBeIncremented = counterRepository.findByName(name).stream().findFirst().orElse(null);
      if(name != null && counterToBeIncremented != null) {
          AtomicInteger counter = new AtomicInteger(counterToBeIncremented.getValue());
          counter.getAndIncrement();
          counterToBeIncremented.setValue(counter.intValue());
          counterRepository.save(counterToBeIncremented);
       } else {
         throw new EntityNotFoundException("Counter does not exist");
       }
       return toCounter(counterToBeIncremented);
  }

  private CounterEntity toCounterEntity(Counter counter) {
    return modelMapper.map(counter, CounterEntity.class);
  }

  private Counter toCounter(CounterEntity counterEntity) {

    return counterEntity !=null ? modelMapper.map(counterEntity, Counter.class) : null;
  }
}
