package org.gooru.skylineinitial.processors.skylineofflineclasscalculator;

import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish.
 */

class SkylineInitialForOfflineClassCalculateProcessorService {

  private final DBI dbiForDefaultDS;
  private final DBI dbiForDsdbDS;
  private SkylineInitialForOfflineClassCalculateCommand command;
  private static final Logger LOGGER = LoggerFactory
      .getLogger(SkylineInitialForOfflineClassCalculateProcessorService.class);

  SkylineInitialForOfflineClassCalculateProcessorService(DBI dbiForDefaultDS,
      DBI dbiForDsdbDS) {

    this.dbiForDefaultDS = dbiForDefaultDS;
    this.dbiForDsdbDS = dbiForDsdbDS;
  }

  void calculateInitialSkyline(SkylineInitialForOfflineClassCalculateCommand command) {
    this.command = command;
    // TODO: Implement this
    LOGGER.info("Command is : {}", command.toString());
  }
}
