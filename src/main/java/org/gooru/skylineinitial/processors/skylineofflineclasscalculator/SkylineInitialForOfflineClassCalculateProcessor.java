package org.gooru.skylineinitial.processors.skylineofflineclasscalculator;

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

public class SkylineInitialForOfflineClassCalculateProcessor implements AsyncMessageProcessor {

  private final Vertx vertx;
  private final Message<JsonObject> message;
  private final Future<MessageResponse> result;

  private static final Logger LOGGER = LoggerFactory
      .getLogger(SkylineInitialForOfflineClassCalculateProcessor.class);
  private EventBusMessage eventBusMessage;
  private SkylineInitialForOfflineClassCalculateProcessorService service = new SkylineInitialForOfflineClassCalculateProcessorService(
      DBICreator.getDbiForDefaultDS(), DBICreator.getDbiForDsdbDS()
  );

  public SkylineInitialForOfflineClassCalculateProcessor(Vertx vertx, Message<JsonObject> message) {
    this.vertx = vertx;
    this.message = message;
    this.result = Future.future();
  }

  @Override
  public Future<MessageResponse> process() {
    vertx.<MessageResponse>executeBlocking(future -> {
      try {
        this.eventBusMessage = EventBusMessage.eventBusMessageBuilder(message);
        SkylineInitialForOfflineClassCalculateCommand command = SkylineInitialForOfflineClassCalculateCommand
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
