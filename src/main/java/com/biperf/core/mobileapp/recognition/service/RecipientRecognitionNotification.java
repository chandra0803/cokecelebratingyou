
package com.biperf.core.mobileapp.recognition.service;

public class RecipientRecognitionNotification extends BaseRecognitionNotification
{
  public RecipientRecognitionNotification( Long recognitionId, LandingScreen landingScreen, boolean mine, boolean allowAddPoints, String message )
  {
    super( recognitionId, landingScreen, mine, allowAddPoints, message );
  }
}
