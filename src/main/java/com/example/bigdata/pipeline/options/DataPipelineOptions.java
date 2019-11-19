package com.example.bigdata.pipeline.options;

import com.example.bigdata.pipeline.enums.WindowType;
import org.apache.beam.sdk.extensions.gcp.options.GcpOptions;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.Validation;

/** The interface Data pipeline options. */
public interface DataPipelineOptions extends GcpOptions {
  /**
   * Gets window duration.
   *
   * @return the window duration
   */
@Validation.Required
  @Description("Duration of the window for streaming data")
  long getWindowDuration();

  /**
   * Sets window duration.
   *
   * @param duration the duration
   */
void setWindowDuration(final long duration);

  /**
   * Gets window type.
   *
   * @return the window type
   */
@Validation.Required
  @Description("Type of window to read data from stream.")
  WindowType getWindowType();

  /**
   * Sets window type.
   *
   * @param type the type
   */
void setWindowType(final WindowType type);

  /**
   * Gets topic.
   *
   * @return the topic
   */
@Validation.Required
  @Description("Topic name")
  String getTopic();

  /**
   * Sets topic.
   *
   * @param topic the topic
   */
void setTopic(final String topic);

  /**
   * Gets subscription.
   *
   * @return the subscription
   */
@Validation.Required
  @Description("Subscription name")
  String getSubscription();


  /**
   * Sets subscription.
   *
   * @param subscription the subscription
   */
void setSubscription(final String subscription);


  /**
   * Gets output directory path.
   *
   * @return the output directory path
   */
@Validation.Required
  @Description("Directory path for output files")
  String getOutputDirectoryPath();

  /**
   * Sets output directory path.
   *
   * @param outputDirectoryPath the output directory path
   */
void setOutputDirectoryPath(final String outputDirectoryPath);

  /**
   * Gets dl topic.
   *
   * @return the dl topic
   */
@Description("DeadLetter PubSub Topic")
  String getDLTopic();

  /**
   * Sets dl topic.
   *
   * @param topic the topic
   */
void setDLTopic(final String topic);
}
