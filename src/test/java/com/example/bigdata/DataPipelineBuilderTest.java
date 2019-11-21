package com.example.bigdata;

import com.example.bigdata.exception.DataPipelineException;
import com.example.bigdata.pipeline.PipelineFunction;
import com.example.bigdata.pipeline.builder.DataPipelineBuilder;
import com.example.bigdata.pipeline.functions.AggregateCustomerOrderByPostalCode;
import com.example.bigdata.pipeline.options.DataPipelineOptions;
import com.example.bigdata.util.TestConstants;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.testing.TestPipeline;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/** The type Data flow pipeline builder test. */
public class DataPipelineBuilderTest {

  /** The Pipeline. */
  @Rule public final TestPipeline pipeline = TestPipeline.create();

  /** Test data flow pipeline. */
  @Test
  public void testDataFlowPipeline() {

    Map<String, String> arguments = buildPipelineOptions();

    PipelineFunction function = new AggregateCustomerOrderByPostalCode();
    Pipeline actualPipeline =
        DataPipelineBuilder.build(
            arguments.entrySet().stream()
                .map(e -> String.format(TestConstants.PATTERN, e.getKey(), e.getValue()))
                .toArray(String[]::new),
            function);
    Assert.assertNotNull(actualPipeline);
    DataPipelineOptions options = (DataPipelineOptions) actualPipeline.getOptions();
    Assert.assertEquals(arguments.get(TestConstants.PROJECT_KEY), options.getProject());
    Assert.assertEquals(
        arguments.get(TestConstants.RUNNER_KEY), options.getRunner().getSimpleName());
  }

  /** Test data flow pipeline without topic. */
  @Test(expected = IllegalArgumentException.class)
  public void testDataFlowPipelineWithoutTopic() {

    // Pipeline options as program arguments
    Map<String, String> arguments = buildPipelineOptions();

    PipelineFunction function = new AggregateCustomerOrderByPostalCode();

    DataPipelineBuilder.build(
        arguments.entrySet().stream()
            .map(e -> String.format(TestConstants.PATTERN, e.getKey(), e.getValue()))
            .toArray(String[]::new),
        function);
  }

  /** Test data flow pipeline without project. */
  @Test(expected = DataPipelineException.class)
  public void testDataFlowPipelineWithoutProject() {

    Map<String, String> arguments = buildPipelineOptions();

    PipelineFunction function = new AggregateCustomerOrderByPostalCode();
    DataPipelineBuilder.build(
        arguments.entrySet().stream()
            .map(e -> String.format(TestConstants.PATTERN, e.getKey(), e.getValue()))
            .toArray(String[]::new),
        function);
  }

  private Map<String, String> buildPipelineOptions() {
    Map<String, String> arguments = new HashMap<>();
    arguments.put(TestConstants.PROJECT_KEY, TestConstants.PROJECT_ID);
    arguments.put(TestConstants.RUNNER_KEY, TestConstants.RUNNER);
    arguments.put(TestConstants.TOPIC_KEY, TestConstants.TOPIC);
    arguments.put(TestConstants.SUBSCRIPTION_KEY, TestConstants.SUBSCRIPTION);
    arguments.put(TestConstants.DLTOPIC_KEY, TestConstants.DLTOPIC);
    arguments.put(TestConstants.OUTPUT_DIRECTORY_PATH_KEY, TestConstants.OUTPUT_DIRECTORY_PATH);
    arguments.put(TestConstants.WINDOW_TYPE_KEY, TestConstants.WINDOW_TYPE_FIXED);
    arguments.put(TestConstants.WINDOW_DURATION_KEY, TestConstants.WINDOW_DURATION);

    return arguments;
  }
}
