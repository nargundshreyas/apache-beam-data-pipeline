package com.example.bigdata.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/** The type Address. */
@Data
public class Address implements Serializable {

  private String houseNo;

  private String street;

  private String city;

  private String postalCode;
}
