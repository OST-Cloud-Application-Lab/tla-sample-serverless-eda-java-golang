package org.contextmapper.sample.tlas.infrastructure.webapi;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.contextmapper.sample.tlas.application.TlaGroupsApplicationService;
import org.contextmapper.sample.tlas.application.exception.TLAGroupNameDoesNotExist;
import org.contextmapper.sample.tlas.domain.tla.exception.InvalidTLAStateTransitionException;
import org.contextmapper.sample.tlas.domain.tla.exception.TLANameDoesNotExist;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.http.HttpStatusCode;

import java.util.function.Function;

import static software.amazon.awssdk.http.HttpStatusCode.OK;

@Component
public class AcceptTLAHandler implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final Log logger = LogFactory.getLog(AcceptTLAHandler.class);

    private static final String GROUP_NAME_PARAM = "groupName";
    private static final String TLA_NAME_PARAM = "name";

    private final TlaGroupsApplicationService service;
    private final ResponseEventFactory responseFactory;

    public AcceptTLAHandler(final TlaGroupsApplicationService service,
                            final ResponseEventFactory responseFactory) {
        this.service = service;
        this.responseFactory = responseFactory;
    }

    @Override
    public APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent requestEvent) {
        logger.info(AcceptTLAHandler.class.getName() + " called");
        try {
            String groupName = requestEvent.getPathParameters().get(GROUP_NAME_PARAM);
            String tlaName = requestEvent.getPathParameters().get(TLA_NAME_PARAM);
            service.acceptTLA(groupName, tlaName);
            return responseFactory.createEmptyResponseEvent(OK);
        } catch (TLAGroupNameDoesNotExist | TLANameDoesNotExist | InvalidTLAStateTransitionException e) {
            logger.error("TLA group name not found", e);
            return responseFactory.createErrorResponseEvent(HttpStatusCode.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            logger.error("Internal error has happened", e);
            return new APIGatewayProxyResponseEvent().withStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR)
                    .withBody("Internal error has happened: " + e.getMessage());
        }

    }

}
