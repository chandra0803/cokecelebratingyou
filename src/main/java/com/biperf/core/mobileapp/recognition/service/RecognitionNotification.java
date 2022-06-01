
package com.biperf.core.mobileapp.recognition.service;

import java.util.Map;

public interface RecognitionNotification
{
  public Long getRecognitionId();

  public LandingScreen getLandingScreen();

  public String getMessage();

  public boolean isAllowAddPoints();

  public boolean isMine();

  public Map<ParameterKeys, String> toValues();

  public enum ParameterKeys
  {
    RECOGNITION_ID, LANDING_SCREEN, IS_MINE, ALLOW_ADD_POINTS, MESSAGE, FIRST_NAME, LAST_NAME, PURL_RECIPIENT_ID, MILESTONE, USER_ID, AVATAR_URL, COUNTRY_NAME, COUNTRY_CODE, NODE_ID;
  }
}
