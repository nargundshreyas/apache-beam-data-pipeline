package com.example.bigdata.util;

/** The type TestConstants. */
public class TestConstants {

  /** The constant INGESTION_TOPIC_KEY. */
  public static final String TOPIC_KEY = "topic";
  /** The constant INGESTION_TOPIC. */
  public static final String TOPIC = "projects/pacific-engine-257511/topics/customer-order-topic";

  /** The constant PROJECT_KEY. */
  public static final String PROJECT_KEY = "project";
  /** The constant PROJECT_ID. */
  public static final String PROJECT_ID = "pacific-engine-257511";

  /** The constant SUBSCRIPTION_KEY. */
  public static final String SUBSCRIPTION_KEY = "subscription";

  /** The constant SUBSCRIPTION. */
  public static final String SUBSCRIPTION =
      "projects/pacific-engine-257511/subscriptions/customer-order-topic-sub1";

  /** The constant DLTOPIC_KEY. */
  public static final String DLTOPIC_KEY = "DLTopic";

  /** The constant DLTOPIC. */
  public static final String DLTOPIC = "projects/pacific-engine-257511/topics/dead-letter-topic";

  /** The constant WINDOW_TYPE_KEY. */
  public static final String WINDOW_TYPE_KEY = "windowType";

  /** The constant WINDOW_TYPE_FIXED. */
  public static final String WINDOW_TYPE_FIXED = "FIXED";

  /** The constant WINDOW_TYPE_SLIDING. */
  public static final String WINDOW_TYPE_SLIDING = "SLIDING";

  /** The constant WINDOW_DURATION_KEY. */
  public static final String WINDOW_DURATION_KEY = "windowDuration";

  /** The constant WINDOW_DURATION. */
  public static final String WINDOW_DURATION = "1";

  public static final String OUTPUT_DIRECTORY_PATH_KEY = "outputDirectoryPath";

  public static final String OUTPUT_DIRECTORY_PATH = "D:\\output\\result";

  /** The constant RUNNER_KEY. */
  public static final String RUNNER_KEY = "runner";

  /** The constant RUNNER. */
  public static final String RUNNER = "DirectRunner";

  /** The constant PATTERN. */
  public static final String PATTERN = "--%s=%s";
}
