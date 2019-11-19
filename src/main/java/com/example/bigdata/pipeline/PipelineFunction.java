package com.example.bigdata.pipeline;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.options.PipelineOptions;

/** The interface Pipeline function. */
public interface PipelineFunction {

  /**
   * Fn pipeline.
   *
   * @param pipeline the pipeline
   * @return the pipeline
   */
Pipeline fn(final Pipeline pipeline);

  /**
   * Validates custom Pipeline validations on the pipeline.
   *
   * @param pipelineOptions the PipelineOptions
   */
void pipelineValidations(final PipelineOptions pipelineOptions);
}
