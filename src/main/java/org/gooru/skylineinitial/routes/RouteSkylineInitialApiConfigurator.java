package org.gooru.skylineinitial.routes;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.gooru.skylineinitial.infra.constants.Constants;
import org.gooru.skylineinitial.infra.constants.Constants.Message;
import org.gooru.skylineinitial.infra.constants.Constants.Route;
import org.gooru.skylineinitial.routes.utils.DeliveryOptionsBuilder;
import org.gooru.skylineinitial.routes.utils.RouteRequestUtility;
import org.gooru.skylineinitial.routes.utils.RouteResponseUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish
 */
public class RouteSkylineInitialApiConfigurator implements RouteConfigurator {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(RouteSkylineInitialApiConfigurator.class);
  private EventBus eb = null;
  private long mbusTimeout;

  @Override
  public void configureRoutes(Vertx vertx, Router router, JsonObject config) {
    eb = vertx.eventBus();
    mbusTimeout = config.getLong(Constants.EventBus.MBUS_TIMEOUT, 30L) * 1_000;
    router.post(Constants.Route.API_SKYLINEINITIAL_FORCE_CALCULATE)
        .handler(this::doSkylineInitialForced);
    router.post(Route.API_SKYLINEINITIAL_STATE).handler(this::fetchState);
  }

  private void fetchState(RoutingContext routingContext) {
    DeliveryOptions options = DeliveryOptionsBuilder
        .buildWithoutApiVersion(routingContext, mbusTimeout, Message.MSG_OP_SKYLINE_INITIAL_STATE);
    eb.<JsonObject>send(Constants.EventBus.MBEP_API_DISPATCHER,
        RouteRequestUtility.getBodyForMessage(routingContext),
        options, reply -> RouteResponseUtility.responseHandler(routingContext, reply, LOGGER));
  }

  private void doSkylineInitialForced(RoutingContext routingContext) {
    DeliveryOptions options = DeliveryOptionsBuilder
        .buildWithoutApiVersion(routingContext, mbusTimeout,
            Message.MSG_OP_SKYLINE_INITIAL_FORCE_CALCULATE);
    eb.<JsonObject>send(Constants.EventBus.MBEP_API_DISPATCHER,
        RouteRequestUtility.getBodyForMessage(routingContext),
        options, reply -> RouteResponseUtility.responseHandler(routingContext, reply, LOGGER));
  }
}
