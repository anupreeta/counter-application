package com.example.counterapp.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import java.util.concurrent.atomic.AtomicInteger;


public class Counter {

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getCounterValue() {
    return counterValue;
  }

  public void setCounterValue(Integer counterValue) {
    this.counterValue = counterValue;
  }

  @NotEmpty(message = "name cannot be null or empty")
  private String name;

  @Positive
  @Min(value = 1, message = "Positive value")
  @Max(value = 100, message = "Max value 100")
  private Integer counterValue;




}
