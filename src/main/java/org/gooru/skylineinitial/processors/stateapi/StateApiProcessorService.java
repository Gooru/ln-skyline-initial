package org.gooru.skylineinitial.processors.stateapi;

import io.vertx.core.json.JsonObject;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish.
 */

class StateApiProcessorService {

  private final DBI dbiForDefaultDS;
  private final DBI dbiForDsdbDS;
  private StateApiProcessorCommand command;
  private static final Logger LOGGER = LoggerFactory.getLogger(StateApiProcessorService.class);

  StateApiProcessorService(DBI dbiForDefaultDS, DBI dbiForDsdbDS) {
    this.dbiForDefaultDS = dbiForDefaultDS;
    this.dbiForDsdbDS = dbiForDsdbDS;
  }

  JsonObject calculateAndFetchState(StateApiProcessorCommand command) {
    this.command = command;
    // TODO: Implement this
    LOGGER.info("Command is : {}", command.toString());
    return new JsonObject().put("dummy", "dummier");
  }
}
