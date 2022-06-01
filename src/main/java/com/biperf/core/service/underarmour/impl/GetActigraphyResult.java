package com.biperf.core.service.underarmour.impl;

/**
 * Results of the call to get under armor actigraphy data
 */
public class GetActigraphyResult
{
  
  private int numberParticipantsUpdated;

  public int getNumberParticipantsUpdated()
  {
    return numberParticipantsUpdated;
  }

  public void setNumberParticipantsUpdated( int numberParticipantsUpdated )
  {
    this.numberParticipantsUpdated = numberParticipantsUpdated;
  }

}
