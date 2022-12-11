package com.example.counterapp.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.counterapp.model.Counter;
import com.example.counterapp.service.CounterService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(CounterController.class)
public class CounterControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CounterService counterService;

  @Test
  void shouldCreateCounter() throws Exception {
    Counter counter = createCounter("nails", 30);

    String requestBody = requestBody();

    when(counterService.createCounter(any(Counter.class))).thenReturn(counter);

    mockMvc.perform(post("/counters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("nails"))
        .andExpect(jsonPath("$.counterValue").value(30));

  }

  @Test
  void shouldGetAllCounters() throws Exception {
    Counter counter1 = createCounter("nails", 30);
    Counter counter2 = createCounter("bolts", 90);

    List<Counter> allCounters = new ArrayList<>();
    allCounters.add(counter1);
    allCounters.add(counter2);
    when(counterService.getAllCounters()).thenReturn(allCounters);

    mockMvc.perform(get("/counters")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("nails"))
            .andExpect(jsonPath("$[0].counterValue").value(30))
            .andExpect(jsonPath("$[1].name").value("bolts"))
            .andExpect(jsonPath("$[1].counterValue").value(90));
  }

  @Test
  void shouldIncrementCounter() throws Exception {
    Counter counter = createCounter("nails",30);

    String requestBody = requestBody();

    when(counterService.createCounter(any(Counter.class))).thenReturn(counter);
    counter.setCounterValue(31);

    when(counterService.incrementCounter("nails")).thenReturn(counter);

    mockMvc.perform(post("/counters")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("nails"))
            .andExpect(jsonPath("$.counterValue").value(31));

  }

  @Test
  void shouldGetCounterByName() throws Exception {
    Counter counter = createCounter("nails",30);

    when(counterService.getCounterByName(any(String.class))).thenReturn(counter);

    mockMvc.perform(get("/counter?name=nails")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("nails"))
            .andExpect(jsonPath("$.counterValue").value(30));
  }

  @Test
  void shouldFailGetCounterByNameForInvalidCounter() throws Exception {
    mockMvc.perform(get("/counter?name=invalid")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
  }

  private String requestBody() throws JsonProcessingException {
    Counter counter = createCounter("nails", 30);
    return new ObjectMapper().writeValueAsString(counter);
  }

  private Counter createCounter(String name, Integer value) {
    Counter counter = new Counter();
    counter.setName(name);
    counter.setCounterValue(value);
    return counter;
  }

}
