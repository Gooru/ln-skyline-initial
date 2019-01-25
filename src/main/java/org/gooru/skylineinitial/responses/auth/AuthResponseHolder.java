package org.gooru.skylineinitial.responses.auth;

public interface AuthResponseHolder {

  boolean isAuthorized();

  boolean isAnonymous();

  String getUser();
}
