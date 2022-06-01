
package com.biperf.core.mobileapp.recognition.service;

public class ManagerRecognitionNotification extends BaseRecognitionNotification
{
  public ManagerRecognitionNotification( Long recognitionId, LandingScreen landingScreen, boolean mine, boolean allowAddPoints, String message )
  {
    super( recognitionId, landingScreen, mine, allowAddPoints, message );
  }
}
