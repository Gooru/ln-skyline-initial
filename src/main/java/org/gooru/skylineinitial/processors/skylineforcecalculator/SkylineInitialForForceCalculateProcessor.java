package org.gooru.skylineinitial.processors.skylineforcecalculator;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.gooru.skylineinitial.infra.data.EventBusMessage;
import org.gooru.skylineinitial.infra.exceptions.ExceptionResolver;
import org.gooru.skylineinitial.infra.jdbi.DBICreator;
import org.gooru.skylineinitial.processors.AsyncMessageProcessor;
import org.gooru.skylineinitial.responses.MessageResponse;
import org.gooru.skylineinitial.responses.MessageResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish.
 */

public class SkylineInitialForForceCalculateProcessor implements AsyncMessageProcessor {

  private final Vertx vertx;
  private final Message<JsonObject> message;
  private final Future<MessageResponse> result;

  private static final Logger LOGGER = LoggerFactory
      .getLogger(SkylineInitialForForceCalculateProcessor.class);
  private EventBusMessage eventBusMessage;
  private final SkylineInitialForClassWithForceCalculateProcessorService service = new SkylineInitialForClassWithForceCalculateProcessorService(
      DBICreator.getDbiForDefaultDS(), DBICreator.getDbiForDsdbDS()
  );

  public SkylineInitialForForceCalculateProcessor(Vertx vertx, Message<JsonObject> message) {
    this.vertx = vertx;
    this.message = message;
    this.result = Future.future();
  }

  @Override
  public Future<MessageResponse> process() {
    vertx.<MessageResponse>executeBlocking(future -> {
      try {
        this.eventBusMessage = EventBusMessage.eventBusMessageBuilder(message);
        SkylineInitialForClassWithForceCalculateCommand command = SkylineInitialForClassWithForceCalculateCommand
            .build(eventBusMessage);
        service.calculateInitialSkyline(command);
        future.complete(MessageResponseFactory.createOkayResponse(new JsonObject()));
      } catch (Throwable throwable) {
        LOGGER.warn("Encountered exception", throwable);
        future.fail(ExceptionResolver.resolveException(throwable));
      }
    }, asyncResult -> {
      if (asyncResult.succeeded()) {
        result.complete(asyncResult.result());
      } else {
        result.fail(asyncResult.cause());
      }
    });
    return result;
  }
}
