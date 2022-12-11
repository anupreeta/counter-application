package com.example.counterapp.service;

import com.example.counterapp.entity.CounterEntity;
import com.example.counterapp.model.Counter;
import com.example.counterapp.repository.CounterRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class CounterServiceTest {

    private CounterService target;

    @Autowired
    private CounterRepository counterRepository;

    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        target = new CounterService(counterRepository, modelMapper);
        initData();
    }

    @AfterEach
    void deleteAll() {
        counterRepository.deleteAll();
    }

    @Test
    void shouldCreateNewCounter() {
        Counter counter = new Counter();
        counter.setName("avocados");
        counter.setCounterValue(95);


        Counter result = target.createCounter(counter);
        assertThat(result.getName(), is("avocados"));
        assertThat(result.getCounterValue(), is(95));
    }

    @Test
    void shouldGetCounterByName() {
        var result = target.getCounterByName("apples");
        assertThat(result.getName(),is("apples"));
        assertThat(result.getCounterValue(), is(40));
    }

    @Test
    void shouldIncrementExistingCounter() {
        var result = target.incrementCounter("apples");
        assertThat(result.getName(), is("apples"));
        assertThat(result.getCounterValue(), is(41));
    }

    @Test
    void shouldGetAllCounters() {
        List<Counter> allCounters = target.getAllCounters();
        assertThat(allCounters.size(), is(3));
    }

    @Test
    void shouldFailToGetNonExistingCounter() {
        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> target.getCounterByName("lemons"),
                "Expected target.getCounterByName() to throw, but it didn't");
    }

    @Test
    void shouldNotCreateDuplicateCounter() {
        Counter duplicate = new Counter();
        duplicate.setName("mangoes");
        duplicate.setCounterValue(90);
        EntityExistsException thrown = assertThrows(
                EntityExistsException.class,
                () -> target.createCounter(duplicate),
                "Expected target.createCounter() to throw, but it didn't");
    }

    void initData() {
        CounterEntity counterEntity1 = new CounterEntity();
        counterEntity1.setName("mangoes");
        counterEntity1.setValue(30);
        counterRepository.save(counterEntity1);

        CounterEntity counterEntity2 = new CounterEntity();
        counterEntity2.setName("apples");
        counterEntity2.setValue(40);
        counterRepository.save(counterEntity2);

        CounterEntity counterEntity3 = new CounterEntity();
        counterEntity3.setName("pears");
        counterEntity3.setValue(50);
        counterRepository.save(counterEntity3);
    }

    private CounterEntity counterEntity(String name, Integer counterValue) {
        CounterEntity counterEntity = new CounterEntity();
        counterEntity.setName(name);
        counterEntity.setValue(counterValue);
        return counterEntity;
    }

}
