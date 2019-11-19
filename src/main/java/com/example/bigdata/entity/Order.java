package com.example.bigdata.entity;

import lombok.Data;

import java.io.Serializable;

/** The type Order. */
@Data
public class Order implements Serializable {
  private String productId;

  private long quantity;

  private double price;

  private double discount;

  private double amount;
}
