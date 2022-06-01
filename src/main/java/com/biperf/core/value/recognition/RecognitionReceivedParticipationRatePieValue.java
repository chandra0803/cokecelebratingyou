/**
 * 
 */

package com.biperf.core.value.recognition;

/**
 * @author poddutur
 *
 */
public class RecognitionReceivedParticipationRatePieValue
{
  private double haveReceivedPct;
  private double haveNotReceivedPct;

  public RecognitionReceivedParticipationRatePieValue()
  {
    super();
  }

  public RecognitionReceivedParticipationRatePieValue( double haveReceivedPct, double haveNotReceivedPct )
  {
    super();
    this.haveReceivedPct = haveReceivedPct;
    this.haveNotReceivedPct = haveNotReceivedPct;
  }

  public double getHaveReceivedPct()
  {
    return haveReceivedPct;
  }

  public void setHaveReceivedPct( double haveReceivedPct )
  {
    this.haveReceivedPct = haveReceivedPct;
  }

  public double getHaveNotReceivedPct()
  {
    return haveNotReceivedPct;
  }

  public void setHaveNotReceivedPct( double haveNotReceivedPct )
  {
    this.haveNotReceivedPct = haveNotReceivedPct;
  }

}
