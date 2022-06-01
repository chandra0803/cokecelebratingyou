
package com.biperf.core.mobileapp.recognition.service;

import java.util.EnumMap;
import java.util.Map;

public abstract class BaseRecognitionNotification implements RecognitionNotification
{
  private final Long recognitionId;
  private final LandingScreen landingScreen;
  private final boolean mine;
  private final boolean allowAddPoints;
  private final String message;

  public BaseRecognitionNotification( Long recognitionId, LandingScreen landingScreen, boolean mine, boolean allowAddPoints, String message )
  {
    this.recognitionId = recognitionId;
    this.landingScreen = landingScreen;
    this.mine = mine;
    this.allowAddPoints = allowAddPoints;
    this.message = message;
  }

  @Override
  public Map<ParameterKeys, String> toValues()
  {
    Map<ParameterKeys, String> values = new EnumMap<ParameterKeys, String>( ParameterKeys.class );
    values.put( ParameterKeys.MESSAGE, message );
    values.put( ParameterKeys.LANDING_SCREEN, LandingScreen.RECOGNITION_DETAIL.toString() );
    values.put( ParameterKeys.IS_MINE, String.valueOf( mine ) );
    values.put( ParameterKeys.ALLOW_ADD_POINTS, String.valueOf( allowAddPoints ) );
    values.put( ParameterKeys.RECOGNITION_ID, recognitionId.toString() );

    return values;
  }

  @Override
  public Long getRecognitionId()
  {
    return recognitionId;
  }

  @Override
  public LandingScreen getLandingScreen()
  {
    return landingScreen;
  }

  @Override
  public boolean isMine()
  {
    return mine;
  }

  @Override
  public boolean isAllowAddPoints()
  {
    return allowAddPoints;
  }

  @Override
  public String getMessage()
  {
    return message;
  }
}
