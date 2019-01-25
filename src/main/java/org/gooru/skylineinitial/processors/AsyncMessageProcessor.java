package org.gooru.skylineinitial.processors;

import io.vertx.core.Future;
import org.gooru.skylineinitial.responses.MessageResponse;

/**
 * @author ashish.
 */
public interface AsyncMessageProcessor {

  Future<MessageResponse> process();

}
