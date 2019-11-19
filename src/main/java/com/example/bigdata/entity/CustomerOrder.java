package com.example.bigdata.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/** The type Customer order. */
@Data
public class CustomerOrder implements Serializable {

  private String orderId;

  private List<Order> orderDetails;

  private Customer customer;

  private String time;
}
