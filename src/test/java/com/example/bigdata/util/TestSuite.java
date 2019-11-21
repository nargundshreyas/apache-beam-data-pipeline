package com.example.bigdata.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * The type Test suite.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestSuite {

    /**
     * The constant EVENT_PAYLOAD_JSON.
     */
public static final String EVENT_PAYLOAD_JSON = "src/test/resources/cim-event_payload.json";

    /**
     * The constant EVENT_PAYLOAD_JSON_WITHOUT_CONTENT.
     */
public static final String EVENT_PAYLOAD_JSON_WITHOUT_CONTENT =
            "src/test/resources/mock/cim_event_payload_without_content.json";

    /**
     * The constant EVENT_PAYLOAD_JSON_WITHOUT_KEYS.
     */
public static final String EVENT_PAYLOAD_JSON_WITHOUT_KEYS =
            "src/test/resources/mock/cim_event_payload_without_keys.json";

    /**
     * The constant EVENT_PAYLOAD_JSON_WITHOUT_SAC_NO.
     */
public static final String EVENT_PAYLOAD_JSON_WITHOUT_SAC_NO =
            "src/test/resources/mock/cim_event_payload_without_sacno.json";

    /**
     * The constant EVENT_PAYLOAD_JSON_WITHOUT_SAC_UNIT.
     */
public static final String EVENT_PAYLOAD_JSON_WITHOUT_SAC_UNIT =
            "src/test/resources/mock/cim_event_payload_without_sacunit.json";

    /**
     * The constant EVENT_PAYLOAD_JSON_WITHOUT_COUNTRY_CODE.
     */
public static final String EVENT_PAYLOAD_JSON_WITHOUT_COUNTRY_CODE =
            "src/test/resources/mock/cim_event_payload_without_countrycode.json";

    /**
     * The constant EVENT_PAYLOAD_JSON_WITHOUT_TARGET.
     */
public static final String EVENT_PAYLOAD_JSON_WITHOUT_TARGET =
            "src/test/resources/mock/cim_event_payload_without_target.json";

    /**
     * The constant EVENT_PAYLOAD_JSON_WITH_EMPTY_TARGETS.
     */
public static final String EVENT_PAYLOAD_JSON_WITH_EMPTY_TARGETS =
            "src/test/resources/mock/cim_event_payload_with_empty_targets.json";

    /**
     * The constant EVENT_PAYLOAD_MALFORMED_JSON.
     */
public static final String EVENT_PAYLOAD_MALFORMED_JSON =
            "src/test/resources/mock/cim-event_payload_error.json";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Gets event payload json.
     *
     * @param path the path
     * @return the event payload json
     * @throws IOException the io exception
     */
public static String getEventPayloadJson(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }

}
