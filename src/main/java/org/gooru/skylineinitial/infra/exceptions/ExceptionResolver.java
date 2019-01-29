package org.gooru.skylineinitial.infra.exceptions;

import org.gooru.skylineinitial.infra.constants.HttpConstants.HttpStatus;

/**
 * @author ashish.
 */

public final class ExceptionResolver {

  private ExceptionResolver() {
    throw new AssertionError();
  }

  public static Throwable resolveException(Throwable throwable) {
    if (throwable instanceof IllegalArgumentException) {
      return new HttpResponseWrapperException(HttpStatus.BAD_REQUEST, throwable.getMessage());
    }
    return throwable;
  }
}
