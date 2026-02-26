package org.contextmapper.sample.tlas.infrastructure.webapi;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.http.HttpStatusCode;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

@Component
public class ResponseEventFactory {

    private static final Log logger = LogFactory.getLog(ResponseEventFactory.class);

    private final ObjectMapper objectMapper;
    private final Map<String, String> headers = new HashMap<>();

    public ResponseEventFactory(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
    }

    public APIGatewayProxyResponseEvent createResponseEvent(final String jsonString) {
        return createResponseEvent(jsonString, HttpStatusCode.OK);
    }

    public APIGatewayProxyResponseEvent createResponseEvent(final String jsonString, final int statusCode) {
        return new APIGatewayProxyResponseEvent()
                .withHeaders(headers)
                .withStatusCode(statusCode)
                .withIsBase64Encoded(false)
                .withBody(jsonString);
    }

    public APIGatewayProxyResponseEvent createEmptyResponseEvent(final int statusCode) {
        return new APIGatewayProxyResponseEvent()
                .withHeaders(headers)
                .withStatusCode(statusCode)
                .withIsBase64Encoded(false);
    }

    public APIGatewayProxyResponseEvent createErrorResponseEvent(final int statusCode,
                                                                 final String message) {
        try {
            return new APIGatewayProxyResponseEvent()
                    .withHeaders(headers)
                    .withStatusCode(statusCode)
                    .withIsBase64Encoded(false)
                    .withBody(objectMapper.writeValueAsString(new ErrorResponse(message, statusCode)));
        } catch (JacksonException e) {
            logger.info("Could not serialize error message object", e);
            throw new RuntimeException(e);
        }
    }

    private class ErrorResponse {
        final String message;
        final int status;

        ErrorResponse(final String message, final int status) {
            this.message = message;
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public int getStatus() {
            return status;
        }
    }

}
