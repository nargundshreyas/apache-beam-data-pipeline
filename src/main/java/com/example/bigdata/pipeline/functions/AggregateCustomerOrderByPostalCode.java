package com.example.bigdata.pipeline.functions;

import com.example.bigdata.exception.DataPipelineException;
import com.example.bigdata.pipeline.PipelineFunction;
import com.example.bigdata.pipeline.builder.DataPipelineBuilder;
import com.example.bigdata.pipeline.enums.WindowType;
import com.example.bigdata.pipeline.options.DataPipelineOptions;
import com.example.bigdata.pipeline.transformation.ParseCustomerOrderDataFn;
import com.example.bigdata.pipeline.transformation.WriteWindowedFileFn;
import com.example.bigdata.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.coders.NullableCoder;
import org.apache.beam.sdk.coders.StringUtf8Coder;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.GroupByKey;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.windowing.FixedWindows;
import org.apache.beam.sdk.transforms.windowing.SlidingWindows;
import org.apache.beam.sdk.transforms.windowing.Window;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionTuple;
import org.apache.beam.sdk.values.TupleTagList;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.Duration;

import java.io.Serializable;

/** The type Aggregate customer order by postal code. */
@Slf4j
public class AggregateCustomerOrderByPostalCode implements PipelineFunction, Serializable {

  @Override
  public Pipeline fn(Pipeline pipeline) {
    DataPipelineOptions pipelineOptions = pipeline.getOptions().as(DataPipelineOptions.class);

    // Phase 1 : Read & parse Data from input source : PubSub
    PCollectionTuple customerOrderPCollectionTuple =
        pipeline
            .apply(
                "ReadPubSubMessage",
                PubsubIO.readMessagesWithAttributes()
                    .fromSubscription(pipelineOptions.getSubscription())
                    .withIdAttribute(Constants.CUSTOMER_ORDER_IDENTIFIER))
            // Parse message to CustomerOrder with TupleTag
            .apply(
                "ParsePubSubMessageToCustomerOrder",
                ParDo.of(new ParseCustomerOrderDataFn())
                    .withOutputTags(
                        Constants.VALID_DATA_TUPLE,
                        TupleTagList.of(Constants.INVALID_DATA_TUPLE)));

    // Extract tuples with success tag for further transformations
    PCollection<KV<String, String>> customerOrderPCollection =
        customerOrderPCollectionTuple.get(Constants.VALID_DATA_TUPLE);

    // Phase 2 : Apply Windowing based on type of Window provided via pipeline options
    if (WindowType.FIXED == pipelineOptions.getWindowType()) {
      customerOrderPCollection =
          customerOrderPCollection.apply(
              "Window",
              Window.into(
                  FixedWindows.of(Duration.standardMinutes(pipelineOptions.getWindowDuration()))));
    } else {
      customerOrderPCollection =
          customerOrderPCollection.apply(
              "Window",
              Window.into(
                  SlidingWindows.of(
                      Duration.standardMinutes(pipelineOptions.getWindowDuration()))));
    }

    // Phase 3 : Apply GroupByKey transformation to aggregate CustomerOrder data based on postal
    // code
    customerOrderPCollection
        .apply("GroupByKeyTransformation", GroupByKey.create())
        // Phase 4 : extract output from GroupByKey transformation
        .apply(
            "ExtractOutputFromGroupByKeyTransform",
            ParDo.of(
                new DoFn<KV<String, Iterable<String>>, String>() {
                  @ProcessElement
                  public void processElement(ProcessContext processContext) {
                    long orderCount = 0;
                    for (String orderId : processContext.element().getValue()) orderCount++;
                    log.info(String.format("%s,%d", processContext.element().getKey(), orderCount));
                    processContext.output(
                        String.format("%s,%d", processContext.element().getKey(), orderCount));
                  }
                }))
        // Phase 5 : Emit output of the functions to the files on disk at specified location
        .apply(
            "EmitWindowedOutputToFile",
            new WriteWindowedFileFn(
                pipelineOptions.getOutputDirectoryPath(), Constants.NUMBER_OF_SHARDS));

    // Extract failed message and push failed messages to dead letter topic

    customerOrderPCollectionTuple
        .get(Constants.INVALID_DATA_TUPLE).setCoder(StringUtf8Coder.of())
        .apply("DeadLetterMessages", PubsubIO.writeStrings().to(pipelineOptions.getDLTopic()));

    return pipeline;
  }

  @Override
  public void pipelineValidations(PipelineOptions pipeline) {

    DataPipelineOptions pipelineOptions = pipeline.as(DataPipelineOptions.class);

    // Validate project name
    final String projectName = pipelineOptions.getProject();
    if (StringUtils.isEmpty(projectName)) {
      throw new DataPipelineException("Project is missing from pipeline options.");
    }

    // Validate WindowType before starting pipeline.
    final WindowType windowType = pipelineOptions.getWindowType();
    if (WindowType.FIXED != windowType && WindowType.SLIDING != windowType) {
      throw new DataPipelineException(
          String.format(
              "Invalid WindowType %s.Acceptable WindowType is [%s, %s]",
              windowType, WindowType.FIXED.toString(), WindowType.SLIDING.toString()));
    }
  }
}
