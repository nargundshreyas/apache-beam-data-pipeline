package com.example.bigdata.pipeline.transformation;

import com.example.bigdata.entity.CustomerOrder;
import com.example.bigdata.exception.DataPipelineException;
import com.example.bigdata.util.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Objects;

/** The type Parse customer order data fn. */
@Slf4j
public class ParseCustomerOrderDataFn extends DoFn<PubsubMessage, KV<String, String>> {

  private ObjectMapper objectMapper;
  /** Creates ObjectMapper object at the time of Setup. */
  @Setup
  public void setup() {
    this.objectMapper = new ObjectMapper();
  }

  /**
   * Process element. Parse PubSubMessage Data and emits KV<String, CustomerOrder>
   *
   * @param pubSubMessage the pub sub message
   * @param processContext the process context
   */
  @ProcessElement
  public void processElement(@Element PubsubMessage pubSubMessage, ProcessContext processContext) {
    // Parse PubSubMessage to entity CustomerOrder
    final String payload = new String(pubSubMessage.getPayload());
    try {
      CustomerOrder customerOrder = objectMapper.readValue(payload, CustomerOrder.class);
      // validate customer order json
      if (!validateCustomerOrder(customerOrder))
        throw new DataPipelineException("Invalid JSON Format for CustomerOrder.");
      processContext.output(
          Constants.VALID_DATA_TUPLE,
          KV.of(
              customerOrder.getCustomer().getAddress().getPostalCode(),
              customerOrder.getOrderId()));
    } catch (DataPipelineException e) {
      log.error(String.format("%s : %s", e.getMessage(), payload));
      processContext.output(Constants.INVALID_DATA_TUPLE, payload);
    } catch (IOException e) {
      log.error("Error occurred while parsing json payload {}.", payload, e);
      processContext.output(Constants.INVALID_DATA_TUPLE, payload);
    }
  }

  private boolean validateCustomerOrder(final CustomerOrder customerOrder) {
    // Check if postal code is not empty
    if ((Objects.nonNull(customerOrder)
        && Objects.nonNull(customerOrder.getCustomer())
        && Objects.nonNull(customerOrder.getCustomer().getAddress()))) {
      return !StringUtils.isEmpty(customerOrder.getCustomer().getAddress().getPostalCode());
    }
    return false;
  }
}
