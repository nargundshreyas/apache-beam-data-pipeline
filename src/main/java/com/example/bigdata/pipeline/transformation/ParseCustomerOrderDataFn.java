package com.example.bigdata.pipeline.transformation;

import com.example.bigdata.entity.CustomerOrder;
import com.example.bigdata.util.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;

/** The type Parse customer order data fn. */
@Slf4j
public class ParseCustomerOrderDataFn extends DoFn<PubsubMessage, KV<String, String>> {

  private ObjectMapper objectMapper;
  private Schema customerOrderSchema;
  /** Creates ObjectMapper object at the time of Setup. */
  @Setup
  public void setup() {
    this.objectMapper = new ObjectMapper();
    // Load Schema for customer order json validation
    JSONObject jsonObject =
        new JSONObject(
            new JSONTokener(this.getClass().getResourceAsStream(Constants.SCHEMA_FILE_NAME)));
    this.customerOrderSchema = SchemaLoader.load(jsonObject);
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
      customerOrderSchema.validate(customerOrder);
      processContext.output(
          Constants.VALID_DATA_TUPLE,
          KV.of(
              customerOrder.getCustomer().getAddress().getPostalCode(),
              customerOrder.getOrderId()));
    } catch (ValidationException e) {
      log.error(String.format("%s : %s", e.getMessage(), payload));
      processContext.output(Constants.INVALID_DATA_TUPLE, payload);
    } catch (IOException e) {
      log.error("Error occurred while parsing json payload {}.", payload, e);
      processContext.output(Constants.INVALID_DATA_TUPLE, payload);
    }
  }
}
