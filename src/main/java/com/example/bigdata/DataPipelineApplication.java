package com.example.bigdata;

import com.example.bigdata.pipeline.builder.DataPipelineBuilder;
import com.example.bigdata.pipeline.functions.AggregateCustomerOrderByPostalCode;

/** The type Data pipeline application. */
public class DataPipelineApplication {
  /**
   * The entry point of application.
   *
   * @param args the input arguments
   */
  public static void main(String[] args) {
    DataPipelineBuilder.build(args, new AggregateCustomerOrderByPostalCode()).run();
  }
}
