package com.example.counterapp.entity;


import jakarta.persistence.*;

import java.util.concurrent.atomic.AtomicInteger;

@Entity
@Table(name = "counters")
public class CounterEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "counter_value", nullable = false)
  private Integer counterValue;


  public Integer getValue() {
    return counterValue;
  }

  public void setValue(Integer value) {
    this.counterValue = value;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}

