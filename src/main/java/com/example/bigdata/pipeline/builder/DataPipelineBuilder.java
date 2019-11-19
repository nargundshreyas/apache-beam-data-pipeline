package com.example.bigdata.pipeline.builder;

import com.example.bigdata.entity.CustomerOrder;
import com.example.bigdata.exception.DataPipelineException;
import com.example.bigdata.pipeline.PipelineFunction;
import com.example.bigdata.pipeline.enums.WindowType;
import com.example.bigdata.pipeline.options.DataPipelineOptions;
import lombok.extern.slf4j.Slf4j;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.TupleTag;
import org.apache.commons.lang3.StringUtils;

/** The type Data pipeline builder. */
@Slf4j
public class DataPipelineBuilder {
  /**
   * Build pipeline.
   *
   * @param args the args
   * @param function the function
   * @return the pipeline
   */
public static Pipeline build(final String[] args, PipelineFunction function) {
    log.info("Building data pipeline....");
    final DataPipelineOptions pipelineOptions =
        PipelineOptionsFactory.fromArgs(args).withValidation().as(DataPipelineOptions.class);

    // Pipeline validations
    function.pipelineValidations(pipelineOptions);

    final Pipeline pipeline = Pipeline.create(pipelineOptions);

    // invoking pipeline function
    return function.fn(pipeline);
  }
}
