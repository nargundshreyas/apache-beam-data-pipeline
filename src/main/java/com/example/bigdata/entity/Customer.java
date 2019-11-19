package com.example.bigdata.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/** The type Customer. */
@Data
public class Customer implements Serializable {

  private String customerId;

  private String name;

  private Address address;
}
