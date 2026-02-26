package org.contextmapper.sample.tlas.infrastructure.webapi;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.contextmapper.sample.tlas.application.TlaGroupsApplicationService;
import org.contextmapper.sample.tlas.application.exception.TLAGroupNameAlreadyExists;
import org.contextmapper.sample.tlas.infrastructure.webapi.dtos.TLAGroupDto;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.http.HttpStatusCode;
import tools.jackson.databind.ObjectMapper;

import java.util.function.Function;

import static org.contextmapper.sample.tlas.infrastructure.webapi.mapper.TlaApiDTOMapper.createTlaGroupDtoToTlaGroup;
import static org.contextmapper.sample.tlas.infrastructure.webapi.mapper.TlaApiDTOMapper.tlaGroupToDto;

@Component
public class AddTLAGroupHandler implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final Log logger = LogFactory.getLog(AddTLAGroupHandler.class);

    private final TlaGroupsApplicationService service;
    private final ObjectMapper objectMapper;
    private final ResponseEventFactory responseFactory;

    public AddTLAGroupHandler(final TlaGroupsApplicationService service,
                              final ObjectMapper objectMapper,
                              final ResponseEventFactory responseFactory) {
        this.service = service;
        this.objectMapper = objectMapper;
        this.responseFactory = responseFactory;
    }

    @Override
    public APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent requestEvent) {
        logger.info(AddTLAGroupHandler.class.getName() + " called");
        try {
            var dto = objectMapper.readValue(requestEvent.getBody(), TLAGroupDto.class);
            var tlaGroup = service.addTLAGroup(createTlaGroupDtoToTlaGroup(dto));
            return responseFactory.createResponseEvent(objectMapper.writeValueAsString(tlaGroupToDto(tlaGroup)), HttpStatusCode.CREATED);
        } catch (TLAGroupNameAlreadyExists e) {
            logger.error("TLA group already exists found", e);
            return responseFactory.createErrorResponseEvent(HttpStatusCode.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            logger.error("Internal error has happened", e);
            return new APIGatewayProxyResponseEvent().withStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR)
                    .withBody("Internal error has happened: " + e.getMessage());
        }

    }

}
