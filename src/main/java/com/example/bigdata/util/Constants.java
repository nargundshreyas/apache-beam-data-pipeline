package com.example.bigdata.util;

import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.TupleTag;

/** The type Constants. */
public class Constants {

  /** The constant CUSTOMER_ORDER_IDENTIFIER. */
  public static final String CUSTOMER_ORDER_IDENTIFIER = "orderId";

  /** The constant NUMBER_OF_SHARDS. */
  public static final int NUMBER_OF_SHARDS = 1;

  /** The constant OUTPUT_FILE_EXTENSION. */
  public static final String OUTPUT_FILE_EXTENSION = ".csv";

  /** The constant VALID_DATA_TUPLE. */
  public static final TupleTag<KV<String, String>> VALID_DATA_TUPLE = new TupleTag<>();

  /** The constant INVALID_DATA_TUPLE. */
  public static final TupleTag<String> INVALID_DATA_TUPLE = new TupleTag<>();

  /** The constant SCHEMA_FILE_NAME. */
  public static final String SCHEMA_FILE_NAME = "customerorder_schema.json";

  private Constants() {}
}
