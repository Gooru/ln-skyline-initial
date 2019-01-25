package org.gooru.skylineinitial.routes.utils;

import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.ext.web.RoutingContext;
import org.gooru.skylineinitial.infra.constants.Constants;

/**
 * @author ashish
 */
public final class DeliveryOptionsBuilder {

  public static DeliveryOptions buildWithoutApiVersion(RoutingContext context, long timeout,
      String op) {
    return new DeliveryOptions().setSendTimeout(timeout * 1_000)
        .addHeader(Constants.Message.MSG_OP, op);
  }

  public static DeliveryOptions buildWithoutApiVersion(long timeout) {
    return new DeliveryOptions().setSendTimeout(timeout * 1_000);
  }

  private DeliveryOptionsBuilder() {
    throw new AssertionError();
  }
}
