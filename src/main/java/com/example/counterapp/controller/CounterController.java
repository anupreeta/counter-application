package com.example.counterapp.controller;

import com.example.counterapp.model.Counter;
import com.example.counterapp.service.CounterService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CounterController {

  private final CounterService counterService;

  public CounterController(CounterService counterService) {
      this.counterService = counterService;
  }

  @PostMapping("/counters")
  @ApiOperation(value = "Creates a new Counter")
  @ApiResponses(value = {
          @ApiResponse(code = 201, message = "Counter is created successfully."),
          @ApiResponse(code = 400, message = "Invalid input."),
          @ApiResponse(code = 403, message = "Forbidden since counter already exists")
  })
  public ResponseEntity<Counter> createCounter(@Valid @RequestBody @NotEmpty Counter counter) {
    Counter createdCounter = counterService.createCounter(counter);
    return new ResponseEntity<>(createdCounter, HttpStatus.CREATED);
  }

  @GetMapping("/counter")
  @ApiOperation(value = "Get counter and its value")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Counter is retrieved successfully."),
          @ApiResponse(code = 400, message = "Invalid input."),
          @ApiResponse(code = 404, message = "Counter does not exist")
  })
  public ResponseEntity<Counter> getCounterByName(@RequestParam("name") String name) {
    Counter counter = counterService.getCounterByName(name);
    if(name == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    if(counter == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(counterService.getCounterByName(name), HttpStatus.OK);
  }

  @GetMapping("/counters")
  @ApiOperation(value = "Lists all counters and its values")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Counters are retrieved successfully."),
          @ApiResponse(code = 400, message = "Invalid input.")
  })
  public ResponseEntity<List<Counter>> getAllCounters() {
    log.info("List all Counters Request is triggered");
    return new ResponseEntity<>(counterService.getAllCounters(), HttpStatus.OK);
  }


  @PostMapping("/counters/increment")
  public ResponseEntity<Counter> incrementCounter(String name) {
    return new ResponseEntity<>(counterService.incrementCounter(name), HttpStatus.OK);
  }
}
