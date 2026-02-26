package org.contextmapper.sample.tlas.infrastructure.webapi;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.contextmapper.sample.tlas.application.TlaGroupsApplicationService;
import org.contextmapper.sample.tlas.domain.tla.TLAStatus;
import org.contextmapper.sample.tlas.infrastructure.webapi.mapper.TlaApiDTOMapper;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.http.HttpStatusCode;
import tools.jackson.databind.ObjectMapper;

import java.util.function.Function;

import static org.contextmapper.sample.tlas.domain.tla.TLAStatus.ACCEPTED;

@Component
public class GetAllTLAGroupsHandler implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final Log logger = LogFactory.getLog(GetAllTLAGroupsHandler.class);

    private static final String STATUS_PARAM = "status";

    private final TlaGroupsApplicationService service;
    private final ObjectMapper objectMapper;
    private final ResponseEventFactory responseFactory;

    public GetAllTLAGroupsHandler(final TlaGroupsApplicationService service,
                                  final ObjectMapper objectMapper,
                                  final ResponseEventFactory responseFactory) {
        this.service = service;
        this.objectMapper = objectMapper;
        this.responseFactory = responseFactory;
    }

    @Override
    public APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent requestEvent) {
        logger.info(GetAllTLAGroupsHandler.class.getName() + " called");
        try {
            var queryParameters = requestEvent.getQueryStringParameters();
            var status = ACCEPTED;
            if (queryParameters != null && queryParameters.containsKey(STATUS_PARAM)) {
                status = TLAStatus.valueOf(queryParameters.get(STATUS_PARAM));
            }
            var tlaGroups = service.findAllTLAGroups(status).stream()
                    .map(TlaApiDTOMapper::tlaGroupToDto)
                    .toList();
            logger.info(GetAllTLAGroupsHandler.class.getName() + " returning " + tlaGroups.size() + " TLA groups");
            return responseFactory.createResponseEvent(objectMapper.writeValueAsString(tlaGroups));
        } catch (Exception e) {
            logger.error("Internal error has happened", e);
            return new APIGatewayProxyResponseEvent().withStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR)
                    .withBody("Internal error has happened: " + e.getMessage());
        }

    }

}
